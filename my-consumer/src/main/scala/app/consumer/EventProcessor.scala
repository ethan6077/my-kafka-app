package app.consumer

import io.cloudevents.CloudEvent
import io.cloudevents.kafka.CloudEventDeserializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}
import scala.language.postfixOps

object EventProcessor {
  def build(): KafkaConsumer[String, CloudEvent] = {
    val consumerProps = new Properties()
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-topic-group")
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val newConsumer: KafkaConsumer[String, CloudEvent] =
      new KafkaConsumer[String, CloudEvent](consumerProps, new StringDeserializer, new CloudEventDeserializer)

    newConsumer.subscribe(List("my-topic-output").asJavaCollection)

    newConsumer
  }

  def receive(kafkaConsumer: KafkaConsumer[String, CloudEvent]): List[CloudEvent] = {
    val records: ConsumerRecords[String, CloudEvent] = kafkaConsumer.poll(Duration.ofSeconds(10))

    printRecordsInfo(records)

    records.asScala.toList.map(_.value())
  }

  private def printRecordsInfo(records: ConsumerRecords[String, CloudEvent]): Unit = {
    println(s"Just polled ${records.count()} records.")

    val recordList: List[ConsumerRecord[String, CloudEvent]] = records.asScala.toList

    recordList.foreach {
      r => {
        println("--------------------- record info ----------------------------")
        println(s"record key: ${r.key()}")
        println(s"record offset: ${r.offset()}")
        println(s"record topic: ${r.topic()}")
      }
    }
  }
}
