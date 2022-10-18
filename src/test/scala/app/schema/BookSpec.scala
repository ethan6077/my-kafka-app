package app.schema

import java.time.LocalDate
import io.circe.literal.JsonStringContext
import io.circe.syntax.EncoderOps
import org.specs2.mutable.Specification

class BookSpec extends Specification {
  "BookEncoder" should {
    "encode book to json" in {
      val expectedJson =
        json"""{
          "author": "ethan",
          "title": "Magic Book",
          "type": "COMIC",
          "pages": 100,
          "releaseDate": "2022-10-01"
        }"""

      val releaseDate = LocalDate.parse("2022-10-01")
      val myBook = Book("ethan", "Magic Book", Comic, 100, releaseDate)
      myBook.asJson should beEqualTo(expectedJson)
    }
  }
}
