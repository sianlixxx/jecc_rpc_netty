package com.jecc.frameworke.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.jecc.frameworke.rpc.util.SerializableUtils;

public class ProtoEncoder extends MessageToByteEncoder {

    private Class<?> clazz;

    public ProtoEncoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf byteBuf) throws Exception {
        // 只对属于clazz的类型的实例进行编码
        if (clazz.isInstance(obj)) {
            byte[] data = SerializableUtils.serialize(obj);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}