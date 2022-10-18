package app.schema

import java.time.LocalDate
import io.circe.literal.JsonStringContext
import io.circe.syntax.EncoderOps
import io.circe.parser.decode
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

  "BookDecoder" should {
    "decode stringified json to Book" in {
      val rawJson =
        json"""{
          "author": "ethan",
          "title": "Magic Book",
          "type": "COMIC",
          "pages": 100,
          "releaseDate": "2022-10-01"
        }"""

      val releaseDate = LocalDate.parse("2022-10-01")
      decode[Book](rawJson.noSpaces) should beRight(
        Book("ethan", "Magic Book", Comic, 100, releaseDate)
      )
    }
  }
}
