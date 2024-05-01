package com.github.xplosunn.expected

import scala.util.{Failure, Success, Try}

object XOption {
  def apply[A](a: => A): XOption[A] =
    new XOption[A](Try(Option(a)))

  def empty[A](): XOption[A] =
    new XOption[A](Try(None))

  def from[A](a: => Option[A]): XOption[A] =
    new XOption[A](Try(a))

  def from[A](a: Try[Option[A]]): XOption[A] =
    new XOption[A](a)
}

case class XOption[A] private (private val underlying: Try[Option[A]]) {
  def map[B](f: A => B): XOption[B] =
    new XOption(underlying.map(_.map(f)))

  def flatMap[B](f: A => XOption[B]): XOption[B] =
    new XOption(Try(underlying.get.flatMap(a => f(a).underlying.get)))

  def nonEmpty(): XValue[A] =
    XValue.from(underlying.flatMap {
      case None => Failure(Unexpected("empty"))
      case Some(value) => Success(value)
    })

  def toList(): XList[A] =
    XList.from(underlying.map(_.toList))

  def unwrap(): Either[Unexpected, Option[A]] =
    underlying match {
      case Failure(unexpected: Unexpected) => Left(unexpected)
      case Failure(exception) => Left(Unexpected(exception.getMessage, Some(exception)))
      case Success(value) => Right(value)
    }

  def asXValue(): XValue[Option[A]] =
    XValue(underlying)
}
