package app

import java.util.Properties
import java.time.Duration
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventDeserializer
import io.circe.parser.decode
import io.circe.{Error => CirceError}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}
import scala.language.postfixOps
import app.schema.Book

package object consumer {
  def build(): KafkaConsumer[String, CloudEvent] = {
    val consumerProps = new Properties()
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-topic-group")

    val newConsumer: KafkaConsumer[String, CloudEvent] =
      new KafkaConsumer[String, CloudEvent](consumerProps, new StringDeserializer, new CloudEventDeserializer)

    newConsumer.subscribe(List("my-topic").asJavaCollection)

    newConsumer
  }

  def receive(kafkaConsumer: KafkaConsumer[String, CloudEvent]): List[CloudEvent] = {
    val records: ConsumerRecords[String, CloudEvent] = kafkaConsumer.poll(Duration.ofSeconds(5))

    println(s"Just polled ${records.count()} records.")

    records.asScala.toList.map(_.value())
  }

  private def getBook(event: CloudEvent): Either[CirceError, Book] = {
    val jsonString = event.getData.toBytes.map(_.toChar).mkString
    decode[Book](jsonString)
  }

  def printEvents(events: List[CloudEvent]): Unit = {
    println("printing events ...")

    events.foreach {
      event => {
        val maybeBook = getBook(event)
        maybeBook match {
          case Left(_) => println("decoding book json error!")
          case Right(book) => println(s"--------------- Book Title: ${book.title} -------------------")
        }
      }
    }
  }
}
