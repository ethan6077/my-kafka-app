package app

import app.schema.Book
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import doobie.util.query.Query0
import io.cloudevents.CloudEvent

package object db {

  private val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/postgres",
    "postgres",
    "postgres"
  )

  private def saveBook(book: Book): Int = {
    val bookType: String = book.`type`.value
    val bookReleaseDate: String = book.releaseDate.toString

    val sql: Update0 =
      sql"""
           |INSERT INTO books (author, title, type, pages, release_date)
           |VALUES (${book.author}, ${book.title}, $bookType, ${book.pages}, TO_DATE($bookReleaseDate, 'YYYY-MM-DD'))
           |"""
        .stripMargin
        .update

    sql.run.transact(xa).unsafeRunSync()
  }

  def saveEvents(events: List[CloudEvent]): Unit = {
    println("printing events ...")

    events.foreach {
      event => {
        val maybeBook = Book.buildBookFromEvent(event)
        maybeBook match {
          case Left(_) => println("decoding book json error!")
          case Right(book) => {
            println(s"--------------- Saving a Book: -------------------")
            Book.print(book)
            saveBook(book)
          }
        }
      }
    }
  }

  def findBookTitle(id: Int): String = {
    val sql: Query0[String] =
      sql"""
           |SELECT title
           |FROM books
           |WHERE id = $id
           |"""
        .stripMargin
        .query[String]

    sql.unique.transact(xa).unsafeRunSync()
  }
}
