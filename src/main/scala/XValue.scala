package com.github.xplosunn.expected

import scala.util.{Failure, Success, Try}

object XValue {
  def apply[A](a: => A): XValue[A] =
    new XValue(Try(a))

  def from[A](a: Try[A]): XValue[A] =
    new XValue(a)
}

case class XValue[A] private (underlying: Try[A]) {
  def map[B](f: A => B): XValue[B] =
    new XValue(underlying.map(f))

  def flatMap[B](f: A => XValue[B]): XValue[B] =
    underlying.map(f) match {
      case Failure(exception) => new XValue[B](Failure(exception))
      case Success(value) => value
    }

  def unwrap(): Either[Unexpected, A] =
    underlying match {
      case Failure(unexpected: Unexpected) => Left(unexpected)
      case Failure(exception) => Left(Unexpected(exception.getMessage, Some(exception)))
      case Success(value) => Right(value)
    }
}
