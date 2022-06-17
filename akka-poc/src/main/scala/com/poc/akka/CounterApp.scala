package com.poc.akka

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object CounterApp extends App{
  println("Step 1: Create an actor system")
  val system = ActorSystem("Counter")

  println("Step 2: Define the message passing protocol for our CounterProtocol")
  object CounterProtocol {
      case object Increment
  }

  println("Step 3: Define CounterActor")
  class CounterActor extends Actor with ActorLogging {
    import CounterProtocol._

    var count = 0
    override def receive: Receive = {
      case Increment => count += 1
        println("Increased counter " + count);
    }
  }

  println("Step 4: Create CounterActor")
  val counterActor = system.actorOf(Props[CounterActor],name="CounterActor")

  println("Step 5: Akka Tell Pattern")
  import CounterProtocol._
  (1 to 5).foreach(_ => counterActor ! Increment)

  println("\nStep 6: close the actor system")
  val isTerminated = system.terminate()

}
