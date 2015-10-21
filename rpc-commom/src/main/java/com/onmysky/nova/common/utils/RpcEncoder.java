package com.onmysky.nova.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by mark on 15/10/21.
 */
public class RpcEncoder extends MessageToByteEncoder<Object>{

  private Class<?> genericClass;

  public RpcEncoder(Class<?> genericClass) {
    this.genericClass = genericClass;
  }

  @Override
  public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
    if (genericClass.isInstance(in)) {
      byte[] data = SerializationUtil.serialize(in);
      out.writeInt(data.length);
      out.writeBytes(data);
    }
  }
}
