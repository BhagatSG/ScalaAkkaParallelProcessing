package com.poc.bhagat.actors

import java.net.ConnectException

import akka.actor.{Actor, ActorRef}
import Child.{Execute, Result}
import akka.actor.Status.{Failure, Success}
import com.poc.bhagat.actors.Parent.ChildFailedException
import com.poc.bhagat.utilities.HttpHelper

import scala.collection.mutable.ListBuffer


object Child {
  case class Execute(taskId: Int, linesList: ListBuffer[String], outputFilePath: String, replyTo: ActorRef)
  case class Result(childId: Int, resultList: ListBuffer[String], outputFilePath: String)
}

class Child extends Actor {
  override def receive: Receive = {
    case Execute(id, linesList, outputFilePath, sender) =>
      /*
        Http Service to call hashing on data.
        Remember: Run & make Hashing Service Up.
       */
      var outputListBuffer = ListBuffer.empty[String]
      try {
        outputListBuffer = HttpHelper.hashConverter(id, linesList)
        sender ! Result(id, outputListBuffer, outputFilePath)
      } catch {
        case ex : ConnectException =>
          /*
            Alternative Method for Testing if services are not up
            Method in Http Helper converting input data to UpperCase
          */
          outputListBuffer = HttpHelper.convertToHash(linesList)
          sender ! Result(id, outputListBuffer, outputFilePath)
        case ex : Exception =>
          throw new ChildFailedException(s"id $id Error Message: "+ex.getMessage)
      }
  }

}
