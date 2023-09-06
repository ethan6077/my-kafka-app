package app

import app.schema._
import cats.effect.{ExitCode, IO, IOApp}

import java.time.LocalDate
import java.util.UUID

object Main extends IOApp {

  private val RELEASE_DATE = LocalDate.parse("2022-10-01")
  private val MY_TECH_BOOK = Book("Mark", "Intelligent Tech Book", Tech, 100, RELEASE_DATE)
  private val MY_COMIC_BOOK = Book("Elon", "Funny Comic Book", Comic, 100, RELEASE_DATE)
  private val MY_NOVEL_BOOK = Book("John", "Exciting Novel Book", Novel, 100, RELEASE_DATE)
  private val MY_OTHER_BOOK = Book("Lala", "A Random Book", Other, 100, RELEASE_DATE)

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("starting producer ..."))
      bookType <- parseArgs(args)
      _ <- startProducer(bookType)
      _ <- IO(println("sent a message ..."))
    } yield ExitCode.Success
  }

  private def parseArgs(args: List[String]): IO[BookType] = {
    if (args.length < 1)
      IO.raiseError(new IllegalArgumentException("Need Book Type: TECH or COMIC"))
    else
      args(0) match {
        case Tech.value => IO.pure(Tech)
        case Comic.value => IO.pure(Comic)
        case Novel.value => IO.pure(Novel)
        case Romance.value => IO.pure(Romance)
        case Other.value => IO.pure(Other)
        case _ => IO.raiseError(new IllegalArgumentException("Invalid BookType"))
      }
  }

  private def selectBook(bookType: BookType): Book = {
    bookType match {
      case Tech => MY_TECH_BOOK
      case Comic => MY_COMIC_BOOK
      case Novel => MY_NOVEL_BOOK
      case _ => MY_OTHER_BOOK
    }
  }

  private def startProducer(bookType: BookType): IO[Unit] = {
    val producerResource = producer.buildKafkaProducerResource

    producerResource.use {
      producerClient => {
        val key = UUID.randomUUID().toString
        val book = selectBook(bookType)
        val value = producer.consCloudEvent(book)
        IO(producer.send(producerClient, key, value))
      }
    }
  }

}
