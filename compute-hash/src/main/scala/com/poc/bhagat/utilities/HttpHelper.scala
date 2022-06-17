package com.poc.bhagat.utilities

import com.poc.bhagat.entity.{RequestMessageEntityCompanion, ResponseMessage}
import net.liftweb.json.{DefaultFormats, parse}
import net.liftweb.json.Serialization.write
import scalaj.http.{Http, HttpOptions}

import scala.collection.mutable.ListBuffer

object HttpHelper {
    def hashConverter(id : Int, linesList: ListBuffer[String]) : ListBuffer[String] = {
      implicit val formats = DefaultFormats
      val requestMessageEntity = new RequestMessageEntityCompanion().getRequestMessageEntity()
      requestMessageEntity.id = "job-"+id
      requestMessageEntity.lines=linesList
      val jsonString = write(requestMessageEntity)

      val result = Http("http://localhost:8080/api/service").postData(jsonString)
        .header("Content-Type", "application/json")
        .header("Charset", "UTF-8")
        .option(HttpOptions.readTimeout(10000)).asString

      val jsonAst = result.body
      val jValue = parse(jsonAst)
      val responseMessage = jValue.extract[ResponseMessage]
      responseMessage.lines.to[ListBuffer]
    }

    def convertToHash(lines: ListBuffer[String]): ListBuffer[String] = {
      var dm: ListBuffer[String] = ListBuffer.empty[String]
      lines.foreach{x => dm += String.valueOf(x.toUpperCase)}
      dm
    }
 }
