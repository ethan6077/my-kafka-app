package app.schema

import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, DecodingFailure, Encoder, Json, Error => CirceError}
import io.cloudevents.CloudEvent

import java.time.LocalDate
import scala.util.{Failure, Success, Try}

case class Book(author: String, title: String, `type`: BookType, pages: Int, releaseDate: LocalDate)

object Book {
  def buildBookFromEvent(event: CloudEvent): Either[CirceError, Book] = {
    val jsonString = event.getData.toBytes.map(_.toChar).mkString
    decode[Book](jsonString)
  }

  def printTitle(book: Book): Unit  = {
    println(s"-----------------Book Title: ${book.title} ---------------------")
  }

  implicit val bookEncoder: Encoder[Book] =
    Encoder { field =>
      Json.obj(
        "author" -> field.author.asJson,
        "title" -> field.title.asJson,
        "type" -> field.`type`.value.asJson,
        "pages" -> field.pages.asJson,
        "releaseDate" -> field.releaseDate.toString.asJson
      )
    }

  implicit val bookDecoder: Decoder[Book] =
    Decoder {
      cursor => {
        for {
          author <- cursor.get[String]("author")
          title <- cursor.get[String]("title")
          rawType <- cursor.get[String]("type")
          myType <- {
            rawType match {
              case "TECH" => Right(Tech)
              case "COMIC" => Right(Comic)
              case "NOVEL" => Right(Novel)
              case "ROMANCE" => Right(Romance)
              case "OTHER" => Right(Other)
              case _ => Left(DecodingFailure("error decoding type field", List()))
            }
          }
          pages <- cursor.get[Int]("pages")
          rawReleaseDate <- cursor.get[String]("releaseDate")
          releaseDate <- {
            val parsedDate = Try(LocalDate.parse(rawReleaseDate))
            parsedDate match {
              case Success(v) => Right(v)
              case Failure(_) => Left(DecodingFailure("error decoding releaseDate field", List()))
            }
          }
        } yield Book(author, title, myType, pages, releaseDate)
      }
    }
}
