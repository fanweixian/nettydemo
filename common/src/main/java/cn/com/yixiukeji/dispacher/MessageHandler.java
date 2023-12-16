package cn.com.yixiukeji.dispacher;

import io.netty.channel.Channel;

public interface MessageHandler<T extends Message> {

    void execute(Channel channel, T message);

    String getType();
}
