package cn.com.yixiukeji.mssagehandler.heartbeat;

import cn.com.yixiukeji.codec.Invocation;
import cn.com.yixiukeji.dispacher.MessageHandler;
import cn.com.yixiukeji.message.heartbeat.HeartbeatRequest;
import cn.com.yixiukeji.message.heartbeat.HeartbeatResponse;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatResponse.TYPE,  response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}
