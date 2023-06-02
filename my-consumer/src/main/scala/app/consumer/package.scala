package app


import app.schema.Book
import io.cloudevents.CloudEvent

import scala.language.postfixOps

package object consumer {
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
            db.saveBook(book)
          }
        }
      }
    }
  }

}
