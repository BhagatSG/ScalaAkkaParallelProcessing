package com.poc.bhagat

import java.util.concurrent.Callable
import java.io.File
import picocli.CommandLine
import picocli.CommandLine.{Command, Parameters}

@Command(name = "checksum", mixinStandardHelpOptions = true,
  description = Array("Prints the Hashing of input file data to Output file."))
class CheckSum extends Callable[Int] {
  @Parameters(index = "0", description = Array("Input file whose Hashing of Data is to calculate."))
  private val inputfile : File = null

  @Parameters(index = "1", description = Array("Output file on which Hashing is to be written."))
  private val outputfile : File = null

  override def call(): Int = {
    if(inputfile != null && outputfile !=null){
      println("inputfile "+inputfile)
      println("outputfile "+outputfile)
      0
    } else {
      1
    }
  }
}

object CheckSum extends App {
  System.exit(new CommandLine(new CheckSum()).execute(args: _*))
}
