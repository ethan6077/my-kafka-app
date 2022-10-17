# My Kafka App

This is a scala app to test out basic kafka functions.

## Dev

start kafka

```shell
docker-compose up
```


### Kafka diagnosis - could skip

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

To confirm the topic was created successfully we can run the following command to see a list of created topics.

```bash
kafka-topics --bootstrap-server broker:9092 --list
```

### Publishing events to a topic

Run the below command

```bash
kafka-console-producer --broker-list broker:9092 --topic my-topic
```

This will put us in a kafka streaming terminal. To send messages to this topic write some text and press enter.

### Running the app

Run `./sbt` and then `run` to start the app.
