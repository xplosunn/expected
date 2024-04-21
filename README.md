# expected

(not published yet, will do if there's interest)

Scala collections library with built-in error scenarios.

Goals:
* Enable programming for the happy path and dealing with errors later
* Catch unexpected thrown exceptions
* Alleviate type-golfing for error scenarios
* Having verifications over assumptions the programmer was making when transforming data
* Error handling provides an Exception with a good message and a stack trace you can inspect for knowing exactly where things went wrong

## Enable programming for the happy path and dealing with errors later

Let's say you have a list of strings you believe contain dates. How do you implement the conversion?

```scala
import java.time.LocalDate

object StringsToDatesExample {
  def toDates(list: List[String]): List[LocalDate] =
    list.map(LocalDate.parse)
}
```

Unless you are comfortable throwing an Exception when `LocalDate.parse` fails, you probably wouldn't do the implementation above. You might change the return type to `Try[List[LocalDate]]` but then you have to find a way to handle the error on each call site or propagate `Try` throughout your code.

Let's look at the same example using this library (code is nearly the same):

```scala
import com.github.xplosunn.expected.XList
import java.time.LocalDate

object StringsToDatesExample {
  def toDates(list: XList[String]): XList[LocalDate] =
    list.map(LocalDate.parse)
}
```

Now your program can continue as if the value is there and you can pick the appropriate place in your architecture for error handling.

```scala
import com.github.xplosunn.expected.XList
import com.github.xplosunn.expected.Unexpected

object ErrorHandlingExample {
  val list = XList("")

  list.unwrap() match {
    case Left(Unexpected(message, cause)) =>
      println(s"Processing failed due to ${message}")
      cause.foreach(_.printStackTrace())
    case Right(scalaStandardLibraryList) =>
      println(s"we got our list: ${scalaStandardLibraryList}")
  }
}
```

## Catch unexpected thrown exceptions

Previous example applies if you forgot that `LocalDate.parse` can throw an exception 😱

## Alleviate type-golfing for error scenarios

Previous example applies if you wanted to return `Either[Throwable, List[LocalDate]]`. You can bring in a library with `traverse` or hand-roll it there.

## Having verifications over assumptions the programmer was making when transforming data

Let's say you have a list and you expect to only find one element at the most that satisfies a condition:

```scala
object ByIdExample {
  case class Person(id: String, nickname: String)

  val peopleList = List(Person("1", "me"), Person("1", "you"))

  val foundIt: Option[Person] = peopleList.find(_.id == "1")
}
```

If you are wrong in your assumption that the list only contains one element satisfying, you might now have an issue that's hard to debug. This might be due to a wrong assumption or a newly introduced bug somewhere else. If that assumption were actually checked it might spare you some debugging time:

```scala
import com.github.xplosunn.expected.XList
import com.github.xplosunn.expected.XOption

object ByIdExample {
  case class Person(id: String, nickname: String)

  val peopleList = XList(Person("1", "me"), Person("1", "you"))

  val containsAnErrorInside: XOption[Person] = peopleList.findAtMostOne(_.id == "1")
}
```

## Error handling provides an Exception with a good message and a stack trace you can inspect for knowing exactly where things went wrong

TODO
