package com.poc.akka

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object Akka3ActorLookUp extends App {

  println("Step 1: Create an actor system")
  val system = ActorSystem("DonutStoreActorSystem")

  println("\nStep 2: Define the message passing protocol for our DonutInfoActor")
  object DonutStoreProtocol {
    case class Info(name : String)
  }

  println("\nStep 3: Define DonutInfoActor")
  class DonutInfoActor extends Actor with ActorLogging {
    import Akka3ActorLookUp.DonutStoreProtocol._
    override def receive: Receive = {
      case Info(name) =>
        log.info(s"Found $name donut")
    }
  }

  println("\nStep 4: Create DonutInfoActor")
  val donutInfoActor = system.actorOf(Props[DonutInfoActor],name="DonutInfoActor")

  println("\nStep 5: Akka Tell Pattern")
  import Akka3ActorLookUp.DonutStoreProtocol._
  donutInfoActor ! Info("vanilla")

  println("\nStep 6: Find Actor using actorSelection() method")
  system.actorSelection("/user/*") ! Info("vanilla and chocolate")

  println("\nStep 7: close the actor system")
  val isTerminated = system.terminate()
}
