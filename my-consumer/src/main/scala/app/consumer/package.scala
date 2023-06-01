package app

import app.schema.Book
import io.circe.parser.decode
import io.circe.{Error => CirceError}
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventDeserializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsJava
import scala.language.postfixOps

package object consumer {
  def build(): KafkaConsumer[String, CloudEvent] = {
    val consumerProps = new Properties()
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-topic-group")
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val newConsumer: KafkaConsumer[String, CloudEvent] =
      new KafkaConsumer[String, CloudEvent](consumerProps, new StringDeserializer, new CloudEventDeserializer)

    newConsumer.subscribe(List("my-topic").asJavaCollection)

    newConsumer
  }

  def saveEvents(events: List[CloudEvent]): Unit = {
    println("printing events ...")

    events.foreach {
      event => {
        val maybeBook = getBook(event)
        maybeBook match {
          case Left(_) => println("decoding book json error!")
          case Right(book) => {
            println(s"--------------- Saving Book: ${book.title} -------------------")
            db.saveBook(book)
          }
        }
      }
    }
  }

  private def getBook(event: CloudEvent): Either[CirceError, Book] = {
    val jsonString = event.getData.toBytes.map(_.toChar).mkString
    decode[Book](jsonString)
  }
}
