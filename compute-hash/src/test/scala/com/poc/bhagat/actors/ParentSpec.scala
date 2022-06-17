package com.poc.bhagat.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.poc.bhagat.actors.Ingestion.DataHashing
import com.poc.bhagat.actors.Parent.{Aggregate, ChildInitialized, InitializeChild}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{Assertion, BeforeAndAfterAll}

import scala.concurrent.duration._

class ParentSpec extends TestKit(ActorSystem("ParentSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {
  import com.poc.bhagat.MainSpec._
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Master Node" should {
    "Initialized Child Actor and reply ChildInitialized" in {
      val parentRef = system.actorOf(Parent.props(testActor))
      parentRef ! InitializeChild(nodesLinesMap, inputFilePath, outputFilePath)
      expectMsg(ChildInitialized(nodesLinesMap, inputFilePath, outputFilePath))
    }

    "Delegate task to worker that is initialized" in {
          val parentRef = system.actorOf(Parent.props(testActor))
          parentRef ! InitializeChild(nodesLinesMap, inputFilePath, outputFilePath)
          parentRef ! DataHashing(sampleData1, outputFilePath)
    }

    "Wait for all workers done working on its task" in {
        val parentRef = system.actorOf(Parent.props(testActor))
        parentRef ! InitializeChild(nodesLinesMap, inputFilePath, outputFilePath)
        parentRef ! DataHashing(sampleData1, outputFilePath)
        parentRef ! DataHashing(sampleData2, outputFilePath)
      }

    "Reply to the correct ResultMap" in {
        val parent = TestProbe("parent")
        val parentRef = system.actorOf(Parent.props(parent.ref))
        parentRef ! InitializeChild(nodesLinesMap, inputFilePath, outputFilePath)
        parentRef ! DataHashing(sampleData1, outputFilePath)
        parentRef ! DataHashing(sampleData2, outputFilePath)

        val assertions: Seq[Assertion] = parent.receiveWhile(max = 1 second) {
          case Aggregate(numberOfStatus,outputFilePath) => {
            numberOfStatus.values.head.length must equal(2)
          }
          case ChildInitialized(nodesLinesMap, inputFilePath, outputFilePath) => assert(true)
        }
        assertions.forall(_ == assert(true))
      }
  }
}
