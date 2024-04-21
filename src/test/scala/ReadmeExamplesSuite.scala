package different.pkg.to.force.imports

class ReadmeExamplesSuite extends munit.FunSuite {
  test("Enable programming for the happy path and dealing with errors later") {
    val _ = {
      import java.time.LocalDate

      object StringsToDatesExample {
        def toDates(list: List[String]): List[LocalDate] =
          list.map(LocalDate.parse)
      }
    }

    val _ = {
      import com.github.xplosunn.expected.XList
      import java.time.LocalDate

      object StringsToDatesExample {
        def toDates(list: XList[String]): XList[LocalDate] =
          list.map(LocalDate.parse)
      }

      assertEquals(
        StringsToDatesExample.toDates(XList("not a date")).unwrap().left.map(_.message),
        Left("Text 'not a date' could not be parsed at index 0")
      )
    }

    val _ = () => {
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
    }
  }

  test("Having verifications over assumptions the programmer was making when transforming data") {
    val _ = {
      object ByIdExample {
        case class Person(id: String, nickname: String)

        val peopleList = List(Person("1", "me"), Person("1", "you"))

        val foundIt: Option[Person] = peopleList.find(_.id == "1")
      }
    }

    val _ = {
      import com.github.xplosunn.expected.XList
      import com.github.xplosunn.expected.XOption

      object ByIdExample {
        case class Person(id: String, nickname: String)

        val peopleList = XList(Person("1", "me"), Person("1", "you"))

        val containsAnErrorInside: XOption[Person] = peopleList.findAtMostOne(_.id == "1")
      }
    }
  }
}