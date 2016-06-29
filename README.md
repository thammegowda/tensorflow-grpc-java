# Tensorflow Image recognition using Grpc Client

This project includes java code example for making use of tensorflow
image recognition over GRPC.



## Build and run this project

```
mvn clean compile exec:java -Dexec.args="localhost:9000 example.jpg"
```
This assumes that tensorflow inception is being served at `localhost:9000` and
 aslo the `example.jpg` file exists.


To build this jar as an addon to tika, run

```mvn clean compile assembly:single```

and then use the jar `target/tensorflow-java-1.0-jar-with-dependencies.jar`

## Setup Tensorflow serving on localhost:9000

Requires Docker


```
# pull and start the prebuilt container, forward port 9000
docker run -it -p 9000:9000 tgowda/inception_serving_tika

# Inside the container, start tensorflow service
root@8311ea4e8074:/# /serving/server.sh

```
If you want to use a different port

May modify the `/serving/server.sh`, which has
`/serving/bazel-bin/tensorflow_serving/example/inception_inference --port=9000 /serving/inception-export/  &> /serving/inception_log &`
