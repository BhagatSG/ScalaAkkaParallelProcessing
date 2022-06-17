package com.poc.bhagat.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.poc.bhagat.actors.Child.{Execute, Result}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class ChildSpec extends TestKit(ActorSystem("ChildSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {
  import com.poc.bhagat.MainSpec._
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Child" should {
    "reply with a Result to an execution command" in {
          val child = system.actorOf(Props[Child])
          val taskId = 1
          child ! Execute(taskId = taskId, sampleData1, outputFilePath , testActor)

          val expectedResult = Result(taskId, expectedResultData, outputFilePath)
          expectMsg(expectedResult)
        }
      }

}
