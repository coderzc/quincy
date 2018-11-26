package com.protocol7.nettyquick.protocol.frames;

import com.google.common.base.Charsets;
import com.protocol7.nettyquick.protocol.Varint;
import io.netty.buffer.ByteBuf;

/*
   0                   1                   2                   3
   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  |           Error Code (16)     |
  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  |                         Frame Type (i)                      ...
  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  |                    Reason Phrase Length (i)                 ...
  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
  |                        Reason Phrase (*)                    ...
  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
*/
public class ConnectionCloseFrame extends Frame {

  public static ConnectionCloseFrame parse(ByteBuf bb) {
    byte type = bb.readByte();
    if (type != 0x1c && type != 0x1d) {
      throw new IllegalArgumentException("Illegal frame type");
    }

    boolean application = type == 0x1d;

    int errorCode = bb.readShort();
    int frameType = 0;
    if (!application) {
      frameType = Varint.readAsInt(bb);
    }

    int reasonPhraseLength = Varint.readAsInt(bb);

    byte[] reasonPhraseBytes = new byte[reasonPhraseLength];
    bb.readBytes(reasonPhraseBytes);

    return new ConnectionCloseFrame(
            application, errorCode, frameType, new String(reasonPhraseBytes, Charsets.UTF_8));
  }

  public static ConnectionCloseFrame connection(int errorCode, int frameType, String reasonPhrase) {
    return new ConnectionCloseFrame(false, errorCode, frameType, reasonPhrase);
  }

  public static ConnectionCloseFrame application(int errorCode, String reasonPhrase) {
    return new ConnectionCloseFrame(true, errorCode, 0, reasonPhrase);
  }

  private final boolean application;
  private final int errorCode;
  private final int frameType;
  private final String reasonPhrase;

  private ConnectionCloseFrame(boolean application, int errorCode, int frameType, String reasonPhrase) {
    super(FrameType.CONNECTION_CLOSE);
    this.application = application;
    this.errorCode = errorCode;
    this.frameType = frameType;
    this.reasonPhrase = reasonPhrase;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public int getFrameType() {
    return frameType;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  @Override
  public void write(final ByteBuf bb) {
    if (application) {
      bb.writeByte(0x1d);
    } else {
      bb.writeByte(0x1c);
    }

    bb.writeShort(errorCode);
    if (!application) {
      Varint.write(frameType, bb);
    }

    byte[] reasonPhraseBytes = reasonPhrase.getBytes(Charsets.UTF_8);

    Varint.write(reasonPhraseBytes.length, bb);
    bb.writeBytes(reasonPhraseBytes);
  }
}
