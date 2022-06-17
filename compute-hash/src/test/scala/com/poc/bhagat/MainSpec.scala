package com.poc.bhagat

import java.io.File
import com.poc.bhagat.utilities.{FileUtility, ParallelismHelper}

import scala.collection.mutable.ListBuffer

object MainSpec {
  lazy val inputFilePath = getClass.getResource("/inputTest.txt").getPath
  lazy val outputFilePath = getClass.getResource("/outputTest.txt").getPath
  lazy val nodesLinesMap = ParallelismHelper.getNodesLinesConfig(FileUtility.getFileSizeKiloBytes(new File("inputFilePath")))

  lazy val nChild = nodesLinesMap.getOrElse("nodes",3)

  var sampleData1  = new ListBuffer[String]()
  sampleData1 += "This is sampleData1 for Testing"
  sampleData1 += "One more Line added"

  var sampleData2  = new ListBuffer[String]()
  sampleData2 += "This is sampleData2 for Testing"
  sampleData2 += "Second Sample Data Created"

  var expectedResultData = new ListBuffer[String]()
  expectedResultData += "THIS IS SAMPLEDATA1 FOR TESTING"
  expectedResultData += "ONE MORE LINE ADDED"
}
