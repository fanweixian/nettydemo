package cn.com.yixiukeji.client.handler;

import cn.com.yixiukeji.client.NettyClient;
import cn.com.yixiukeji.message.heartbeat.HeartbeatRequest;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public   class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private NettyClient nettyClient;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyClient.reconnect();
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }

    /**
     * 在客户端在空闲时，向服务端发送一次心跳，即心跳机制。
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler) {
            log.info("[userEventTriggered][发起一次心跳]");
            HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            ctx.writeAndFlush(new cn.com.yixiukeji.codec.Invocation(HeartbeatRequest.TYPE, JSON.toJSONString(heartbeatRequest)))
                    .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}