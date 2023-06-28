package app.eventconsumer

import app.utils
import cats.effect.{IO, Resource}
import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventDeserializer
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}

object EventConsumerClient {
  private def build: KafkaConsumer[String, CloudEvent] = {
    val props = utils.initProps()
    val newConsumer: KafkaConsumer[String, CloudEvent] =
      new KafkaConsumer[String, CloudEvent](props, new StringDeserializer, new CloudEventDeserializer)

    newConsumer.subscribe(List("my-books-topic").asJavaCollection)

    newConsumer
  }

  def buildResource: Resource[IO, KafkaConsumer[String, CloudEvent]] = {
    Resource.make {
      IO(build)
    } { consumer =>
      IO {
        println("Resource is closing ...")
        consumer.close()
      }
    }
  }

  def receiveCloudEvents(kafkaConsumer: KafkaConsumer[String, CloudEvent]): List[CloudEvent] = {
    val records: ConsumerRecords[String, CloudEvent] = kafkaConsumer.poll(Duration.ofSeconds(10))

    utils.printRecordsInfo(records)

    records.asScala.toList.map(_.value())
  }

}
