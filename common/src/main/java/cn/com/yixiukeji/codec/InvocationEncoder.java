package cn.com.yixiukeji.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现将 Invocation 序列化，并写入到 TCP Socket 中。
 */

@Slf4j
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation invocation, ByteBuf out) throws Exception {
        byte[] content = JSON.toJSONBytes(invocation);
        out.writeInt(content.length);
        out.writeBytes(content);
        log.info("[encode][连接({}) 编码了一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
