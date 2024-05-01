package com.github.xplosunn.expected

class XValueSuite extends munit.FunSuite {
  test("for comprehension combining all") {
    val obtained = for {
      list <- XList(1, 2).asXValue()
      option <- XOption("").asXValue()
      map <- XMap(true -> false).asXValue()
      value <- XValue(1d)
    } yield (list, option, map, value)

    val expected = XValue((List(1, 2), Option(""), Map(true -> false), 1d))
    assertEquals(obtained, expected)
  }
}