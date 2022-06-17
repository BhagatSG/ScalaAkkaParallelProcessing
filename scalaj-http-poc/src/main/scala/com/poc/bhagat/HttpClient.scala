package com.poc.bhagat

import com.poc.bhagat.entity.{RequestMessageEntityCompanion, ResponseMessage}
import net.liftweb.json.{DefaultFormats, parse}
import net.liftweb.json.Serialization.write
import scalaj.http.{Http, HttpOptions}

import scala.collection.mutable.ListBuffer

object HttpClient extends App{
  var dm  = new ListBuffer[String]()
  dm += "This is for Testing"
  dm += "One more Line added"
  val requestMessageEntity = new RequestMessageEntityCompanion().getRequestMessageEntity()
  requestMessageEntity.id = "job-"+1
  requestMessageEntity.lines=dm

  //Scala Object to Json String Conversion
  implicit val formats = DefaultFormats
  val jsonString = write(requestMessageEntity)
  println(jsonString)

  //Http Hashing Service call
  val result = Http("http://localhost:8080/api/service").postData(jsonString)
    .header("Content-Type", "application/json")
    .header("Charset", "UTF-8")
    .option(HttpOptions.readTimeout(10000)).asString

  val jsonAst = result.body

  //Json String Body conversion to Scala Object
  val jValue = parse(jsonAst)
  val mailServer = jValue.extract[ResponseMessage]

  val linesList = mailServer.lines

  //List to ListBuffer
  val linesBuffer = linesList.to[ListBuffer]

  for(element<-linesBuffer) {
    println(element)
  }
  print("completed")

}
