package com.protocol7.quincy;

import com.protocol7.quincy.protocol.Version;
import com.protocol7.quincy.tls.extensions.TransportParameters;

public class Configuration {

  private final Version version;
  private final int initialMaxStreamDataBidiLocal;
  private final int initialMaxData;
  private final int initialMaxBidiStreams;
  private final int idleTimeout;
  private final int maxPacketSize;
  private final int ackDelayExponent;
  private final int initialMaxUniStreams;
  private final boolean disableMigration;
  private final int initialMaxStreamDataBidiRemote;
  private final int initialMaxStreamDataUni;
  private final int maxAckDelay;

  public Configuration(
      final Version version,
      final int initialMaxStreamDataBidiLocal,
      final int initialMaxData,
      final int initialMaxBidiStreams,
      final int idleTimeout,
      final int maxPacketSize,
      final int ackDelayExponent,
      final int initialMaxUniStreams,
      final boolean disableMigration,
      final int initialMaxStreamDataBidiRemote,
      final int initialMaxStreamDataUni,
      final int maxAckDelay) {
    this.version = version;
    this.initialMaxStreamDataBidiLocal = initialMaxStreamDataBidiLocal;
    this.initialMaxData = initialMaxData;
    this.initialMaxBidiStreams = initialMaxBidiStreams;
    this.idleTimeout = idleTimeout;
    this.maxPacketSize = maxPacketSize;
    this.ackDelayExponent = ackDelayExponent;
    this.initialMaxUniStreams = initialMaxUniStreams;
    this.disableMigration = disableMigration;
    this.initialMaxStreamDataBidiRemote = initialMaxStreamDataBidiRemote;
    this.initialMaxStreamDataUni = initialMaxStreamDataUni;
    this.maxAckDelay = maxAckDelay;
  }

  public Version getVersion() {
    return version;
  }

  public int getInitialMaxStreamDataBidiLocal() {
    return initialMaxStreamDataBidiLocal;
  }

  public int getInitialMaxData() {
    return initialMaxData;
  }

  public int getInitialMaxBidiStreams() {
    return initialMaxBidiStreams;
  }

  public int getIdleTimeout() {
    return idleTimeout;
  }

  public int getMaxPacketSize() {
    return maxPacketSize;
  }

  public int getAckDelayExponent() {
    return ackDelayExponent;
  }

  public int getInitialMaxUniStreams() {
    return initialMaxUniStreams;
  }

  public boolean isDisableMigration() {
    return disableMigration;
  }

  public int getInitialMaxStreamDataBidiRemote() {
    return initialMaxStreamDataBidiRemote;
  }

  public int getInitialMaxStreamDataUni() {
    return initialMaxStreamDataUni;
  }

  public int getMaxAckDelay() {
    return maxAckDelay;
  }

  public TransportParameters toTransportParameters() {
    return TransportParameters.newBuilder()
        .withInitialMaxStreamDataBidiLocal(initialMaxStreamDataBidiLocal)
        .withInitialMaxData(initialMaxData)
        .withInitialMaxBidiStreams(initialMaxBidiStreams)
        .withIdleTimeout(idleTimeout)
        .withMaxPacketSize(maxPacketSize)
        .withInitialMaxUniStreams(initialMaxUniStreams)
        .withDisableMigration(disableMigration)
        .withInitialMaxStreamDataBidiRemote(initialMaxStreamDataBidiRemote)
        .withInitialMaxStreamDataUni(initialMaxStreamDataUni)
        .build();
  }
}
