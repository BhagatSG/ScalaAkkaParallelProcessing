package com.poc.bhagat.actors

import java.io.FileInputStream
import akka.actor.{Actor, ActorLogging, ActorRef}
import Parent._
import com.poc.bhagat.utilities.FileUtility
import scala.io.Source
import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer

object Ingestion {
  case class StartIngestion(nodesLinesMap: Map[String, Int], inputFilePath : String, outputFilePath : String)
  case class DataHashing(linesList: ListBuffer[String], outputFilePath : String)
}


class Ingestion extends Actor with ActorLogging{
  import Ingestion._
  /*
    Initializing the parent node
  */
  var parentNode: ActorRef = context.actorOf(Parent.props(context.self), "parentNode")

  override def receive: Receive = {
    case StartIngestion(nodesLinesMap, inputFilePath, outputFilePath ) =>
      log.info("[Ingestion] Initializing Child ...")
      parentNode ! Parent.InitializeChild(nodesLinesMap, inputFilePath, outputFilePath)

    /*
      After child are initialized, then get List of lines from the file, and send them to the parent node
    */
    case ChildInitialized(nodesLinesMap, inputFilePath, outputFilePath) =>
      log.info("[Ingestion] Childs are initialized. Getting List of lines from Input File and sending to parentNode ...")
      val lineCount = nodesLinesMap.getOrElse("lines",5)
      var linesList  = new ListBuffer[String]()
      Source.fromInputStream(new FileInputStream(inputFilePath)).getLines().toList.foreach{
        line =>
          linesList += line
          if(linesList.length == lineCount) {
            parentNode ! DataHashing(linesList, outputFilePath)
            linesList  = ListBuffer.empty[String]
          }
      }

    case Aggregate(result, outputFilePath) =>
      val sortResultMpa = ListMap(result.toSeq.sortBy(_._1):_*)
      val outputList = new ListBuffer[String]()
      sortResultMpa.values.toList.foreach {
        x => x.foreach(y => outputList += y)
      }

      val len = outputList.length
      log.info(s"[Ingestion] outputList Size $len")
      FileUtility.writeToOutputFile(outputList.toList.iterator, outputFilePath)
      context.parent ! Supervisor.Stop
  }
}
