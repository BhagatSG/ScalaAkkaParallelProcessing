package com.poc.bhagat.actors

import akka.actor.{ActorSystem, Terminated}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class SupervisorSpec extends TestKit(ActorSystem("SupervisorSpec"))
  with AnyWordSpecLike
  with ImplicitSender
  with BeforeAndAfterAll {
  import com.poc.bhagat.MainSpec._
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Supervisor" should {
    "start Ingestion successfully" in {
      EventFilter.info("[Ingestion] Initializing Child ...") intercept {
        val supervisorRef = system.actorOf(Supervisor.props(nodesLinesMap,inputFilePath,outputFilePath))
        supervisorRef ! Supervisor.Start
      }
    }

    "stopping the system once everything completed" in {
      val supervisorRef = system.actorOf(Supervisor.props(nodesLinesMap,inputFilePath,outputFilePath))
      watch(supervisorRef)
      supervisorRef ! Supervisor.Start
      val terminated = expectMsgType[Terminated]
      assert(terminated.actor == supervisorRef)
    }
  }
}
