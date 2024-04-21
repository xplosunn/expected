package com.github.xplosunn.expected

case class Unexpected(message: String, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)
