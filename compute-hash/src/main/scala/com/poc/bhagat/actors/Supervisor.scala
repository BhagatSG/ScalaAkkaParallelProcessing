package com.poc.bhagat.actors

import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}
import Supervisor.{Start, Stop}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume}

import scala.concurrent.duration.DurationInt

object Supervisor {
  case object Start
  case object Stop
  def props(nodesLinesMap: Map[String, Int], inputFilePath : String, outputFilePath : String) = Props(new Supervisor(nodesLinesMap, inputFilePath, outputFilePath))
}

class Supervisor(nodesLinesMap: Map[String, Int], inputFilePath : String, outputFilePath : String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Start =>
      /*
      Creating Ingestion actor and sending message to Ingestion actor
      */
      val ingestion = context.actorOf(Props[Ingestion], "ingestion")
      log.info("[Supervisor] Ingestion actor created and message sending to Ingestion actor")
      try {
        ingestion ! Ingestion.StartIngestion(nodesLinesMap, inputFilePath, outputFilePath)
      } catch {
        case ex : Exception =>
          println("Exception Occured in [Supervisor] start: "+ex.getMessage)
          ex.printStackTrace()
      }

    case Stop =>
      log.info("[Supervisor] All things are done, stopping the system")
      println("Completed. Voila!! It worked....")
      context.system.terminate()
  }
}
