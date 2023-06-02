package app

import java.net.URI
import java.time.{LocalDate, OffsetDateTime}
import java.util.{Properties, UUID}
import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.kafka.CloudEventSerializer
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import app.schema.{Book, Comic}
import io.circe.syntax.EncoderOps

package object producer {
  def build(): KafkaProducer[String, CloudEvent] = {
    val producerProps = new Properties()
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT")
    producerProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 20000)

    val newProducer = new KafkaProducer[String, CloudEvent](producerProps, new StringSerializer, new CloudEventSerializer)

    newProducer
  }

  def send(kafkaProducer: KafkaProducer[String, CloudEvent], key: String, value: CloudEvent): Unit = {
    println("sending a new message ...")

    val record = new ProducerRecord("my-books-topic", key, value)

    kafkaProducer.send(record).get()
  }

  def consCloudEvent(): CloudEvent = {
    val releaseDate = LocalDate.parse("2022-10-01")
    val myBook = Book("ethan", "Magic Book", Comic, 100, releaseDate)
    val payload = myBook.asJson.noSpaces.getBytes()

    CloudEventBuilder
      .v1()
      .withId(UUID.randomUUID().toString)
      .withType("au.com.rea.V1")
      .withSource(URI.create("urn:rea:events:v1:source:rea-source-1"))
      .withTime(OffsetDateTime.now())
      .withData(payload)
      .build();
  }
}
