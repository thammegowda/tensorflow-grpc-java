/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usc.irds.tensorflow.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import edu.usc.irds.tensorflow.grpc.InceptionInference.InceptionRequest;
import edu.usc.irds.tensorflow.grpc.InceptionInference.InceptionResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Thamme Gowda
 */
public class Main {

  public static void main(String[] args) throws IOException {

    if (args.length != 2){
      System.out.println("Invalid args");
      System.out.println("Usage: <host:port> <image>");
      System.out.println("\tExample: localhost:9090 ~/Pictures/cat.jpg");
      System.exit(1);
    }

    String[] parts = args[0].split(":");
    String server = parts[0];
    int port = Integer.parseInt(parts[1]);
    String imagePath = args[1];

    System.out.println("Connecting to GRPC service at " + args[0]);
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(server, port)
        .usePlaintext(true)
        .build();
    InceptionBlockingStub stub = new InceptionBlockingStub(channel);

    System.out.println("Image = " + imagePath);
    InputStream jpegStream = new FileInputStream(imagePath);
    ByteString jpegData = ByteString.readFrom(jpegStream);

    InceptionRequest inceptReq = InceptionRequest
        .newBuilder().setJpegEncoded(jpegData).build();

    InceptionResponse resp = stub.classify(inceptReq);

    System.out.println(resp);
    System.out.println("Shutting down");
    channel.shutdown();
  }

}
