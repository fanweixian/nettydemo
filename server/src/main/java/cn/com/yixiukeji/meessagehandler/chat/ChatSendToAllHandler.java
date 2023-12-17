package cn.com.yixiukeji.meessagehandler.chat;

import cn.com.yixiukeji.codec.Invocation;
import cn.com.yixiukeji.dispacher.MessageHandler;
import cn.com.yixiukeji.message.chat.ChatRedirectToUserRequest;
import cn.com.yixiukeji.message.chat.ChatSendResponse;
import cn.com.yixiukeji.message.chat.ChatSendToAllRequest;
import cn.com.yixiukeji.server.NettyChannelManager;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChatSendToAllHandler implements MessageHandler<ChatSendToAllRequest>{
    @Resource
    private NettyChannelManager nettyChannelManager;
    @Override
    public void execute(Channel channel, ChatSendToAllRequest message) {

        //假装直接成功
        ChatSendResponse sendResponse = new ChatSendResponse().setCode(0);
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE, sendResponse));

        //创建转发的消息
        ChatSendToAllRequest chatRedirectToUserRequest = new ChatSendToAllRequest()
                .setMsgId(message.getMsgId())
                .setContent(message.getContent());
        nettyChannelManager.sendAll(new Invocation(ChatRedirectToUserRequest.TYPE, chatRedirectToUserRequest));
    }

    @Override
    public String getType() {
        return ChatSendToAllRequest.TYPE;
    }
}
