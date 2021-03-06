package com.protocol7.quincy.netty;

import static java.util.Objects.requireNonNull;

import com.protocol7.quincy.Configuration;
import com.protocol7.quincy.protocol.Version;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import java.security.PrivateKey;
import java.util.List;

public class QuicBuilder {

  private Version version = Version.DRAFT_18;
  private int initialMaxStreamDataBidiLocal = 32768;
  private int initialMaxData = 49152;
  private int initialMaxBidiStreams = 100;
  private int idleTimeout = 30;
  private int maxPacketSize = 1452;
  private int ackDelayExponent = 3;
  private int initialMaxUniStreams = 100;
  private boolean disableMigration = true;
  private int initialMaxStreamDataBidiRemote = 32768;
  private int initialMaxStreamDataUni = 32768;
  private int maxAckDelay = 100; // TODO verify

  private List<byte[]> certificates;
  private PrivateKey privateKey;

  public QuicBuilder withVersion(final Version version) {
    this.version = version;
    return this;
  }

  public QuicBuilder withInitialMaxStreamDataBidiLocal(final int initialMaxStreamDataBidiLocal) {
    this.initialMaxStreamDataBidiLocal = initialMaxStreamDataBidiLocal;
    return this;
  }

  public QuicBuilder withInitialMaxData(final int initialMaxData) {
    this.initialMaxData = initialMaxData;
    return this;
  }

  public QuicBuilder withInitialMaxBidiStreams(final int initialMaxBidiStreams) {
    this.initialMaxBidiStreams = initialMaxBidiStreams;
    return this;
  }

  public QuicBuilder withIdleTimeout(final int idleTimeout) {
    this.idleTimeout = idleTimeout;
    return this;
  }

  public QuicBuilder withMaxPacketSize(final int maxPacketSize) {
    this.maxPacketSize = maxPacketSize;
    return this;
  }

  public QuicBuilder withAckDelayExponent(final int ackDelayExponent) {
    this.ackDelayExponent = ackDelayExponent;
    return this;
  }

  public QuicBuilder withInitialMaxUniStreams(final int initialMaxUniStreams) {
    this.initialMaxUniStreams = initialMaxUniStreams;
    return this;
  }

  public QuicBuilder withDisableMigration(final boolean disableMigration) {
    this.disableMigration = disableMigration;
    return this;
  }

  public QuicBuilder withInitialMaxStreamDataBidiRemote(final int initialMaxStreamDataBidiRemote) {
    this.initialMaxStreamDataBidiRemote = initialMaxStreamDataBidiRemote;
    return this;
  }

  public QuicBuilder withInitialMaxStreamDataUni(final int initialMaxStreamDataUni) {
    this.initialMaxStreamDataUni = initialMaxStreamDataUni;
    return this;
  }

  public QuicBuilder withMaxAckDelay(final int maxAckDelay) {
    this.maxAckDelay = maxAckDelay;
    return this;
  }

  public QuicBuilder withCertificates(final List<byte[]> certificates) {
    this.certificates = certificates;
    return this;
  }

  public QuicBuilder withPrivateKey(final PrivateKey privateKey) {
    this.privateKey = privateKey;
    return this;
  }

  public Configuration configuration() {
    return new Configuration(
        version,
        initialMaxStreamDataBidiLocal,
        initialMaxData,
        initialMaxBidiStreams,
        idleTimeout,
        maxPacketSize,
        ackDelayExponent,
        initialMaxUniStreams,
        disableMigration,
        initialMaxStreamDataBidiRemote,
        initialMaxStreamDataUni,
        maxAckDelay);
  }

  public ChannelHandler serverChannelInitializer(final ChannelHandler handler) {
    requireNonNull(certificates);
    requireNonNull(privateKey);

    return new QuicServerInitializer(configuration(), handler, certificates, privateKey);
  }

  public ChannelInitializer<DatagramChannel> clientChannelInitializer(
      final ChannelHandler handler) {
    return new QuicClientInitializer(configuration(), handler);
  }
}
