package com.poc.bhagat.actors

import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import Ingestion.DataHashing
import Parent.{Aggregate, ChildFailedException, ChildInitialized, InitializeChild}
import Child._
import akka.actor.SupervisorStrategy.{Escalate, Restart}

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.DurationInt

object Parent {
  case class InitializeChild(nodesLinesMap: Map[String, Int], inputFilePath : String, outputFilePath : String)
  case class ChildInitialized(nodesLinesMap: Map[String, Int], inputFilePath : String, outputFilePath : String)
  case class Aggregate(numberOfStatus: scala.collection.mutable.Map[Int, ListBuffer[String]], outputFilePath : String)
  case class ChildFailedException(error: String) extends Exception(error)
  def props(parent: ActorRef): Props = Props(new Parent(parent))
}

/*
  Parent Actor:
  1. Initialized Child Actor
  2. Assign Execute task to Child Actor
  3. Aggregate the result and send it to Ingestion
 */
class Parent(parent: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
        case InitializeChild(nodesLinesMap, inputFilePath, outputFilePath) =>
        val nChild = nodesLinesMap.getOrElse("nodes",20)
        log.info(s"[Parent] Initializing $nChild child(s)...")
        val childs = (0 to nChild).toVector.map(id => context.actorOf(Props[Child], s"${id}-child"))
        sender() ! ChildInitialized(nodesLinesMap, inputFilePath, outputFilePath)
        context.become(
          childInitialized(
            currentChildId = 0,
            currentTaskId = 0,
            childs = childs,
            Set.empty[Int],
            resultMap = scala.collection.mutable.Map.empty[Int, ListBuffer[String]]
          )
        )
  }

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5 seconds) {
      case _: ChildFailedException =>
        log.error("Child failed exception, will restart.")
        Restart

      case _: Exception =>
        log.error("Child failed, will need to escalate up the hierarchy")
        Escalate
    }

  def childInitialized(
                        currentChildId: Int,
                        currentTaskId: Int,
                        childs: Vector[ActorRef],
                        taskIdSet: Set[Int],
                        resultMap: scala.collection.mutable.Map[Int, ListBuffer[String]]
                      ): Receive = {
    case DataHashing(linesList, outputFilePath) =>
      //log.info(s"[Parent] received $linesList assigning taskId $currentTaskId to child $currentChildId")
      val currentChild = childs(currentChildId)
      val newTaskIdSet = taskIdSet + currentTaskId

      currentChild ! Execute(currentTaskId, linesList, outputFilePath, context.self)

      val newTaskId = currentTaskId + 1
      val newChildId = (currentChildId + 1) % childs.length

      context.become(childInitialized(newChildId, newTaskId, childs, newTaskIdSet, resultMap))

    case Result(id, resultList, outputFilePath) =>
      //log.info(s"[Parent] Received result $resultList from taskId $id")
      val newTaskIdSet = taskIdSet - id

      resultMap.put(id, resultList)

      if (newTaskIdSet.isEmpty) {
        log.info(s"[Parent] All task are done, sending result back to ${parent.path}")
        parent ! Aggregate(resultMap, outputFilePath)
      } else {
        //log.info(s"[Parent] Task is not yet all done, waiting for other childs to send back results")
        context.become(childInitialized(currentChildId, currentTaskId, childs, newTaskIdSet, resultMap))
      }
  }
}
