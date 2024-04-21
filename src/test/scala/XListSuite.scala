package com.github.xplosunn.expected

import java.time.LocalDate

class XListSuite extends munit.FunSuite {
  test("map") {
    assertEquals(
      XList(true, false).map(!_),
      XList(false, true)
    )

    assertEquals(
      XList("").map(LocalDate.parse).unwrap().left.map(_.message),
      Left("Text '' could not be parsed at index 0")
    )
  }

  test("flatMap") {
    assertEquals(
      XList(true, false).flatMap(elem => XList(elem, elem)),
      XList(true, true, false, false)
    )
  }

  test("toXMap") {
    assertEquals(
      XList(true -> false).toXMap(),
      XMap(true -> false)
    )
  }

  test("findAtMostOne") {
    assertEquals(
      XList(false, false, false).findAtMostOne(identity),
      XOption.empty[Boolean]()
    )

    assertEquals(
      XList(false, true, false).findAtMostOne(identity),
      XOption(true)
    )

    assertEquals(
      XList(false, true, true).findAtMostOne(identity).unwrap().left.map(_.message),
      Left("there was more than one element satisfying the predicate")
    )
  }

  test("atMostOne") {
    assertEquals(
      XList[Boolean]().atMostOne(),
      XOption.empty[Boolean]()
    )

    assertEquals(
      XList(true).atMostOne(),
      XOption(true)
    )

    assertEquals(
      XList(true, true).atMostOne().unwrap().left.map(_.message),
      Left("there was more than one element")
    )
  }

  test("exactlyOne") {
    assertEquals(
      XList[Boolean]().exactlyOne().unwrap().left.map(_.message),
      Left("empty")
    )

    assertEquals(
      XList(true).exactlyOne(),
      XValue(true)
    )

    assertEquals(
      XList(true, true).exactlyOne().unwrap().left.map(_.message),
      Left("there was more than one element")
    )
  }


}