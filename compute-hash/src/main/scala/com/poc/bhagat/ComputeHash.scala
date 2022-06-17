package com.poc.bhagat

import java.util.concurrent.Callable

import com.poc.bhagat.actors.Supervisor
import com.poc.bhagat.utilities.{FileUtility, ParallelismHelper}

import java.io.File

import akka.actor.ActorSystem
import picocli.CommandLine
import picocli.CommandLine.{Command, Parameters}

object ComputeHash extends App {
  System.exit(new CommandLine(new ComputeHash()).execute(args: _*))
}

@Command(name = "computehash", mixinStandardHelpOptions = true,
  description = Array("Prints the Hashing of input file data to Output file."))
class ComputeHash extends Callable[Int] {
  @Parameters(index = "0", description = Array("Input file whose Hashing of Data is to calculate."))
  private val inputFile : File = null

  @Parameters(index = "1", description = Array("Output file on which Hashing is to be written."))
  private val outputFile : File = null

  override def call(): Int = {
    if(inputFile.exists() && inputFile.isFile){
        println("inputFile Passed                : " + inputFile)
        println("inputFile Absolute Path         : " + inputFile.getAbsolutePath)
        println("inputFile Passed Size(in kb)    : " + FileUtility.getFileSizeKiloBytes(inputFile))
        println("inputFile Passed Size(in mb)    : " + FileUtility.getFileSizeMegaBytes(inputFile))
        println("outputFile Passed               : " + outputFile)
        println("outputFile Absolute Path        : " + outputFile.getAbsolutePath)
        val nodesLinesMap = ParallelismHelper.getNodesLinesConfig(FileUtility.getFileSizeKiloBytes(inputFile))
        val system = ActorSystem("DataProcessingAkka")
        val supervisorActor = system.actorOf(Supervisor.props(nodesLinesMap, inputFile.getAbsolutePath, outputFile.getAbsolutePath), "supervisor")
        supervisorActor ! Supervisor.Start
        println("Completed. Voila!! It worked....")
        0
    } else {
      val inputFilePath = inputFile.getAbsolutePath
      println(s"InputFile Path passed: $inputFilePath is not valid/doesn't exists.Please pass the correct one")
      1
    }
  }
}


