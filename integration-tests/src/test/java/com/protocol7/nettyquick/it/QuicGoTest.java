package com.protocol7.nettyquick.it;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.InternetProtocol;
import com.protocol7.nettyquic.client.ClientConnection;
import com.protocol7.nettyquic.client.QuicClient;
import com.protocol7.nettyquic.streams.Stream;
import com.protocol7.nettyquic.streams.StreamListener;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class QuicGoTest {

  private Logger log = LoggerFactory.getLogger("quic-go");

  @Rule
  public GenericContainer quicGo =
      new GenericContainer<>(
              new ImageFromDockerfile()
                      .withFileFromClasspath("Dockerfile", "Dockerfile"))
          .withExposedPorts(6121)
          .waitingFor(Wait.forLogMessage(".*server Listening for udp connections on.*\\n", 1));

  private int getUdpPort() {
    return Integer.valueOf(
        quicGo
            .getContainerInfo()
            .getNetworkSettings()
            .getPorts()
            .getBindings()
            .get(new ExposedPort(6121, InternetProtocol.UDP))[0]
            .getHostPortSpec());
  }

  @Test
  public void cont() throws ExecutionException, InterruptedException {
    Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
    quicGo.followOutput(logConsumer);

    InetSocketAddress server = new InetSocketAddress(quicGo.getContainerIpAddress(), getUdpPort());

    QuicClient client =
        QuicClient.connect(
                server,
                new StreamListener() {
                  @Override
                  public void onData(Stream stream, byte[] data) {
                    System.out.println(new String(data));
                  }

                  @Override
                  public void onDone() {}

                  @Override
                  public void onReset(Stream stream, int applicationErrorCode, long offset) {}
                })
            .get();

    client.openStream().write("Hello world".getBytes(), true);

    client.close();
  }
}