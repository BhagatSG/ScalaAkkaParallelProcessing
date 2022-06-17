package com.poc.bhagat.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, TestKit}
import com.poc.bhagat.actors.Ingestion._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class IngestionSpec extends TestKit(ActorSystem("IngestionSpec"))
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {
  import com.poc.bhagat.MainSpec._
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Ingestion" should {
    "Return initialized when parent sends StartIngestion message" in {
      EventFilter.info(
        message = "[Ingestion] Childs are initialized. Getting List of lines from Input File and sending to parentNode ..."
      ) intercept {
        val ingestion = system.actorOf(Props[Ingestion], "ingestion")
        ingestion ! StartIngestion(nodesLinesMap, inputFilePath, outputFilePath)
      }

    }

    "Return the right aggregate result" in {
      EventFilter.info(
        start = "[Ingestion] total status Code: "
      ) intercept {
        val ingestion = system.actorOf(Props[Ingestion])
        ingestion ! StartIngestion(nodesLinesMap, inputFilePath, outputFilePath)
      }

    }
  }

}
