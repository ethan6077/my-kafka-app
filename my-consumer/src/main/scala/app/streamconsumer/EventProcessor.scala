package app.streamconsumer

import app.utils
import cats.effect.{IO, Resource}
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}
import scala.language.postfixOps

object EventProcessor {
  private def build: KafkaConsumer[String, String] = {
    val props = utils.initProps()
    val newConsumer: KafkaConsumer[String, String] =
      new KafkaConsumer[String, String](props, new StringDeserializer, new StringDeserializer)

    newConsumer.subscribe(List("my-books-output").asJavaCollection)

    newConsumer
  }

  def buildResource: Resource[IO, KafkaConsumer[String, String]] = {
    Resource.make {
      IO(build)
    } { consumer =>
      IO {
        println("Resource is closing ...")
        consumer.close()
      }
    }
  }

  def receiveStringEvents(kafkaConsumer: KafkaConsumer[String, String]): List[String] = {
    val records: ConsumerRecords[String, String] = kafkaConsumer.poll(Duration.ofSeconds(10))

    utils.printStringRecordsInfo(records)

    records.asScala.toList.map(_.value())
  }

}
