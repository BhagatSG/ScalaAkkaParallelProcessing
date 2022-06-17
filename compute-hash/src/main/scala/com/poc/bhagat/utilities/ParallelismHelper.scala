package com.poc.bhagat.utilities

object ParallelismHelper {
    def getNodesLinesConfig(fileSize : Long) : Map[String, Int] = {
      val nodesLinesMap = collection.mutable.Map.empty[String, Int]
      //Nodes for Parallelism
      var nodes = 10
      //Lines to process from files in one go
      var lines = 2
      //< 1 MB
      if (fileSize < 1024) {
        nodes = 10
        lines = 2
      }
      //< 5 MB
      else if (fileSize < 5120){
        nodes = 30
        lines = 5
      }
      //< 10 MB
      else if (fileSize < 10240){
        nodes = 50
        lines = 8
      }
      //< 50 MB
      else if (fileSize < 51200){
        nodes = 80
        lines = 15
      }
      //< 100 MB
      else if (fileSize < 102400){
        nodes = 150
        lines = 20
      }
      //< 500 MB
      else if (fileSize < 512000){
        nodes = 300
        lines = 30
      }
      //< 1 GB
      else if (fileSize < 1024000){
        nodes = 500
        lines = 50
      }
      // > 1 GB
      else {
        nodes = 1000
        lines = 100
      }

      nodesLinesMap.put("nodes",nodes)
      nodesLinesMap.put("lines", lines)

      nodesLinesMap.toMap
    }
}
