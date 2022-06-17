package com.poc.bhagat.utilities

import org.scalatest.flatspec.AnyFlatSpec

class ParallelismHelperSpec extends AnyFlatSpec {
  var nodesLinesMap = collection.immutable.Map.empty[String, Int]

  it should "return Map of Nodes & Lines with count as 10 & 2" in {
    nodesLinesMap = ParallelismHelper.getNodesLinesConfig(1000)
    assert(nodesLinesMap.get("nodes") === Some(10))
    assert(nodesLinesMap.get("lines") === Some(2))
  }

  it should "return Map of Nodes & Lines with count as 30 & 5" in {
    nodesLinesMap = ParallelismHelper.getNodesLinesConfig(2048)
    assert(nodesLinesMap.get("nodes") === Some(30))
    assert(nodesLinesMap.get("lines") === Some(5))
  }

  it should "return Map of Nodes & Lines with count as 80 & 15" in {
    nodesLinesMap = ParallelismHelper.getNodesLinesConfig(20480)
    assert(nodesLinesMap.get("nodes") === Some(80))
    assert(nodesLinesMap.get("lines") === Some(15))
  }

  it should "return Map of Nodes & Lines with count as 500 & 50" in {
    nodesLinesMap = ParallelismHelper.getNodesLinesConfig(513000)
    assert(nodesLinesMap.get("nodes") === Some(500))
    assert(nodesLinesMap.get("lines") === Some(50))
  }
}