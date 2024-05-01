package com.github.xplosunn.expected

import scala.util.{Failure, Success, Try}

object XMap {
  def apply[K, V](kvs: (K, V)*): XMap[K, V] =
    new XMap(Try(Map.from(kvs)))

  def from[K, V](kvs: => Map[K, V]): XMap[K, V] =
    new XMap(Try(kvs))

  def from[K, V](kvs: Try[Map[K, V]]): XMap[K, V] =
    new XMap(kvs)
}

case class XMap[K, V] private(underlying: Try[Map[K, V]]) {
  def mapKeys[New](f: K => New): XMap[New, V] =
    new XMap(Try {
      val oldMap = underlying.get
      var newMap = Map[New, V]()
      oldMap.foreach {
        case (k, v) =>
          val newKey = f(k)
          if (newMap.contains(newKey)) {
            throw Unexpected(s"Key is repeated: ${newKey}")
          }
          newMap = newMap + (newKey -> v)
      }
      newMap
    })

  def mapValues[New](f: V => New): XMap[K, New] =
    new XMap(underlying.map(_.view.mapValues(f).toMap))

  def unwrap(): Either[Unexpected, Map[K, V]] =
    underlying match {
      case Failure(unexpected: Unexpected) => Left(unexpected)
      case Failure(exception) => Left(Unexpected(exception.getMessage, Some(exception)))
      case Success(value) => Right(value)
    }

  def asXValue(): XValue[Map[K, V]] =
    XValue(underlying)
}
