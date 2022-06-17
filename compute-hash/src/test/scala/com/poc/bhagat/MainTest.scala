package com.poc.bhagat

import java.io.File

import akka.actor.ActorSystem
import com.poc.bhagat.actors.Supervisor
import com.poc.bhagat.utilities.{FileUtility, ParallelismHelper}

object MainTest extends App{
  val inputFile  : File = new File("./InputMain.txt")
  val outputFile : File = new File("./OutputMain.txt")
  if(inputFile.exists() && inputFile.isFile){
    println("inputFile Passed                : "+inputFile)
    println("inputFile Absolute Path         : "+inputFile.getAbsolutePath)
    println("inputFile Passed Size(in kb)    : "+ FileUtility.getFileSizeKiloBytes(inputFile))
    println("inputFile Passed Size(in mb)    : "+ FileUtility.getFileSizeMegaBytes(inputFile))
    println("outputFile Passed               : "+outputFile)
    println("outputFile Absolute Path        : "+outputFile.getAbsolutePath)
    val nodesLinesMap = ParallelismHelper.getNodesLinesConfig(FileUtility.getFileSizeKiloBytes(inputFile))
    val system = ActorSystem("DataProcessing")
    val supervisorActor = system.actorOf(Supervisor.props(nodesLinesMap, inputFile.getAbsolutePath, outputFile.getAbsolutePath), "supervisor")
    supervisorActor ! Supervisor.Start
  } else {
    val inputFilePath = inputFile.getAbsolutePath
    println(s"InputFile Path passed: $inputFilePath is not valid/doesn't exists.Please pass the correct one")
  }
}
