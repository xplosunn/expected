package com.github.xplosunn.expected

import scala.util.{Failure, Success, Try}

object XList {
  def apply[A](as: A*): XList[A] =
    new XList[A](Try(as.toList))

  def from[A](as: => List[A]): XList[A] =
    new XList(Try(as))

  def from[A](as: Try[List[A]]): XList[A] =
    new XList(as)
}

case class XList[A] private (private val underlying: Try[List[A]]) {
  def map[B](f: A => B): XList[B] =
    new XList(underlying.map(_.map(f)))

  def flatMap[B](f: A => XList[B]): XList[B] =
    new XList(Try(underlying.get.flatMap(a => f(a).underlying.get)))

  def toXMap[K, V]()(implicit ev: A <:< (K, V)): XMap[K, V] =
    XMap.from(underlying.map(_.toMap))

  def findAtMostOne(predicate: A => Boolean): XOption[A] =
    XOption.from(underlying.map(_.filter(predicate)).flatMap {
      case Nil => Success(None)
      case head :: Nil => Success(Some(head))
      case _ => Failure(Unexpected("there was more than one element satisfying the predicate"))
    })

  def atMostOne(): XOption[A] =
    XOption.from(underlying.flatMap {
      case Nil => Success(None)
      case head :: Nil => Success(Some(head))
      case _ => Failure(Unexpected("there was more than one element"))
    })

  def exactlyOne(): XValue[A] =
    XValue.from(underlying.flatMap {
      case Nil => Failure(Unexpected("empty"))
      case head :: Nil => Success(head)
      case _ => Failure(Unexpected("there was more than one element"))
    })

  def unwrap(): Either[Unexpected, List[A]] =
    underlying match {
      case Failure(unexpected: Unexpected) => Left(unexpected)
      case Failure(exception) => Left(Unexpected(exception.getMessage, Some(exception)))
      case Success(value) => Right(value)
    }

  def asXValue(): XValue[List[A]] =
    XValue(underlying)
}
