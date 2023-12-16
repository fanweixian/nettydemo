package cn.com.yixiukeji.client.handler;

import cn.com.yixiukeji.codec.InvocationDecoder;
import cn.com.yixiukeji.codec.InvocationEncoder;
import cn.com.yixiukeji.dispacher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {
    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 60;

    @Resource
    private MessageDispatcher messageDispatcher;
    @Resource
    private NettyClientHandler nettyClientHandler;

    /**
     * @param ch 此时创建的客户端 Channel
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline
                // 空闲检测
                .addLast(new IdleStateHandler(READ_TIMEOUT_SECONDS, 0, 0))
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS * 3))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                //                // 消息分发器
                .addLast(messageDispatcher)
                // 服务端处理器
                .addLast(nettyClientHandler)
        ;
    }

}