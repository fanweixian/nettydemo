package cn.com.yixiukeji.client;


import cn.com.yixiukeji.client.handler.NettyClientHandlerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Invocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClient {

    private static final Integer RECONNECT_SECONDS = 20;

    @Value("${netty.server.host}")
    private String serverHost;
    @Value("${netty.server.port}")
    private Integer serverPort;

    @Resource
    private NettyClientHandlerInitializer nettyClientHandlerInitializer;

    //线程组，用于客户端对服务端的连接，数据读写
    public EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private volatile Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                // 设置两个 EventLoopGroup 对象
                .group(eventLoopGroup)
                //指定 Channel 为服务端 NioServerSocketChannel
                .channel(NioSocketChannel.class)
                //设置 Netty Server 的端口
                .remoteAddress(serverHost, serverPort)
                //TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .option(ChannelOption.SO_KEEPALIVE, true)
                //允许较小的数据包的发送，降低延迟
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(nettyClientHandlerInitializer);

        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                log.error("[start][Netty Client 连接服务器({}:{}) 失败]", serverHost, serverPort);
                reconnect();
                return;
            }
            channel = future.channel();
            log.info("[start][Netty Client 连接服务器({}:{}) 成功]", serverHost, serverPort);
        });
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        eventLoopGroup.shutdownGracefully();
    }

    public void send(Invocation invocation) {
        if (channel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        channel.writeAndFlush(invocation);
    }

    public void reconnect() {
        eventLoopGroup.schedule(() -> {
            log.info("[reconnect][开始重连]");
            try {
                start();
            } catch (InterruptedException e) {
                log.error("[reconnect][重连失败]", e);
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);
        log.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }
}
