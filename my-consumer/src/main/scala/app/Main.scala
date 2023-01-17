package app

import java.time.LocalDate
import scala.util.Try
import app.schema.{Book, Tech}

object Main extends App {
  println("starting consumer ...")
  val myConsumer = consumer.build()

  while (true) {
    println("starting polling ...")
    val receivedEvents = consumer.receive(myConsumer)
    consumer.saveEvents(receivedEvents)
    // commit to kafka
    myConsumer.commitSync()

//    println("starting db ...")
//    val firstBookTitle: String = db.findBookTitle(1)
//    println(s"First Book Title: ${firstBookTitle}")
//
//    println("Create and save a book ...")
//    val releaseDate: LocalDate = LocalDate.parse("2022-11-02")
//    val techBook = Book("Paul", "Functional Programming in Scala", Tech, 200, releaseDate)
//    db.saveBook(techBook)
  }

  println("closing consumer ...")
  Try(myConsumer.close()).recover {
    case _ => println("Failed to close the kafka consumer")
  }
}
