package com.poc.bhagat.entity

import scala.collection.mutable.ListBuffer

case class RequestMessage(var id: String, var lines: ListBuffer[String])

case class RequestMessageEntityCompanion(){
  def getRequestMessageEntity() : RequestMessage = {
    var RequestMessageEntity = new RequestMessage("",new ListBuffer[String]())
    RequestMessageEntity
  }
}
