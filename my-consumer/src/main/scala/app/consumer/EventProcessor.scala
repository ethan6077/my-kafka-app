package app.consumer

import io.cloudevents.CloudEvent
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

import java.time.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.language.postfixOps

object EventProcessor {
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
