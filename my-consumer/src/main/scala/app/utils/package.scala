package app

import io.cloudevents.CloudEvent
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords}

import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsScala

package object utils {
  def initProps(): Properties = {
    val consumerProps = new Properties()
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-topic-group")
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    consumerProps
  }

  def printRecordsInfo(records: ConsumerRecords[String, CloudEvent]): Unit = {
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

  def printStringRecordsInfo(records: ConsumerRecords[String, String]): Unit = {
    println(s"Just polled ${records.count()} records.")

    val recordList: List[ConsumerRecord[String, String]] = records.asScala.toList

    recordList.foreach {
      r => {
        println("--------------------- string record info ----------------------------")
        println(s"string record key: ${r.key()}")
        println(s"string record offset: ${r.offset()}")
        println(s"string record topic: ${r.topic()}")
        println(s"string record value: ${r.value()}")
      }
    }
  }
}
