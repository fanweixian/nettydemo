package cn.com.yixiukeji.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Invocation;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class NettyChannelManager {

    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");

    //channel 的映射
    private final ConcurrentHashMap<ChannelId, Channel> channels = new ConcurrentHashMap<>();

    //用户与channel的映射
    private final ConcurrentHashMap<String, Channel> userChannels = new ConcurrentHashMap<>();


    public void add(Channel channel) {
        channels.put(channel.id(), channel);
        log.info("[add][一个连接({})加入]", channel.id());
    }

    public void addUser(Channel channel, String user) {
        Channel existChannel = channels.get(channel.id());
        if (existChannel == null) {
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        channel.attr(CHANNEL_ATTR_KEY_USER).set(user);
        userChannels.put(user, channel);
    }

    public void remove(Channel channel) {
        channels.remove(channel.id());
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)) {
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    public void send(String user, Invocation invocation) {
        Channel channel = userChannels.get(user);
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

    public void sendAll(Invocation invocation) {
        for (Channel channel : channels.values()) {
            if (!channel.isActive()) {
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            channel.writeAndFlush(invocation);
        }
    }
}
