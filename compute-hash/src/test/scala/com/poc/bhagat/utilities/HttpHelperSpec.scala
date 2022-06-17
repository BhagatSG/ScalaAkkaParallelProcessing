package com.poc.bhagat.utilities

import java.net.ConnectException
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable.ListBuffer

class HttpHelperSpec extends AnyFlatSpec with BeforeAndAfter{
  var sampleData  = new ListBuffer[String]()
  sampleData += "This is sampleData for Testing"
  sampleData += "One more Line added"

  var expectedResultData = ListBuffer[String]()

  before {
    expectedResultData = ListBuffer[String]()
  }

  it should "return ListBuffer converting input data to Uppercase" in {
    expectedResultData += "THIS IS SAMPLEDATA FOR TESTING"
    expectedResultData += "ONE MORE LINE ADDED"
    val outputListBuffer = HttpHelper.convertToHash(sampleData)
    assert(outputListBuffer === expectedResultData)
  }

  it should "return ListBuffer converting input data to Hashcode" in {
    expectedResultData += "-126833471"
    expectedResultData += "347506117"
    var outputListBuffer = ListBuffer.empty[String]
    try {
      outputListBuffer = HttpHelper.hashConverter(1, sampleData)
    } catch {
      case ex : ConnectException =>
        /*
          Alternative Method for Testing if services are not up
          Method in Http Helper converting input data to UpperCase
        */
        outputListBuffer = HttpHelper.convertToHash(sampleData)
    }
    assert(outputListBuffer === expectedResultData)
  }
}
