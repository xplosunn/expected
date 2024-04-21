package com.github.xplosunn.expected

class XValueSuite extends munit.FunSuite {
  test("for comprehension combining all") {
    val obtained = for {
      list <- XList(1, 2).insideXValue()
      option <- XOption("").insideXValue()
      map <- XMap(true -> false).insideXValue()
      value <- XValue(1d)
    } yield (list, option, map, value)

    val expected = XValue((XList(1, 2), XOption(""), XMap(true -> false), 1d))
    assertEquals(obtained, expected)
  }
}