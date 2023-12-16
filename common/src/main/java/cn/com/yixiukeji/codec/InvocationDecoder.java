package cn.com.yixiukeji.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 实现从 TCP Socket 读取字节数组，反序列化成 Invocation。
 */
@Slf4j
public class InvocationDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //标记当前读的位置
        in.markReaderIndex();
        //判断是否能偶读取length长度
        if (in.readableBytes() <= 4) {
            return;
        }
        int length = in.readInt();
        if (length < 0) {
            throw new CorruptedFrameException("负长度： " + length);
        }
        //如果message 不够可读，则退回到原读取位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        //读取内容
        byte[] content = new byte[length];
        in.readBytes(content);
        Invocation invocation = JSON.parseObject(content, Invocation.class);
        out.add(invocation);
        log.info("[decode][连接({}) 解析了一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
