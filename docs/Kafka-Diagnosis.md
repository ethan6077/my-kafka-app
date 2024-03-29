# Kafka diagnosis

check connection to kafka
```shell
nc -z localhost 29092
```

check kafka logs
```shell
docker-compose logs broker | grep -i started
```

### Logging into Broker container

Run the below command to login to the broker container.

```bash
docker exec -it broker bash
```

### Creating a topic

```bash
kafka-topics --create --bootstrap-server broker:9092  --replication-factor 1 --partitions 1 --topic my-topic
```

list topics

```bash
kafka-topics --bootstrap-server broker:9092 --list
```

delete a topic
```bash
kafka-topics --bootstrap-server broker:9092 --delete --topic my-topic
```

### Publishing events to a topic

Run the below command

```bash
kafka-console-producer --broker-list broker:9092 --topic my-topic
```

This will put us in a kafka streaming terminal. To send messages to this topic write some text and press enter.

### Consuming events from a topic

```bash
kafka-console-consumer --bootstrap-server broker:9092 --topic my-topic --from-beginning
```