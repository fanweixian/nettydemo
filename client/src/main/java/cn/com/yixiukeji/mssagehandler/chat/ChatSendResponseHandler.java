package cn.com.yixiukeji.mssagehandler.chat;

import cn.com.yixiukeji.dispacher.MessageHandler;
import cn.com.yixiukeji.message.chat.ChatSendResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse>{
    @Override
    public void execute(Channel channel, ChatSendResponse message) {
        log .info("[execute][发送结果：{}]", message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }
}
