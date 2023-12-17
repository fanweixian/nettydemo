package cn.com.yixiukeji.dispacher;

import cn.com.yixiukeji.codec.Invocation;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    private final ExecutorService executorService = Executors.newFixedThreadPool(200);
    @Resource
    private MessageHandlerContainer messageHandlerContainer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) throws Exception {
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());

        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);

        Message message = JSON.parseObject(invocation.getMessage(), messageClass);

        executorService.submit(() -> messageHandler.execute(ctx.channel(), message));

    }
}
