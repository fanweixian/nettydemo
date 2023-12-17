package cn.com.yixiukeji.controller;

import cn.com.yixiukeji.client.NettyClient;
import cn.com.yixiukeji.codec.Invocation;
import cn.com.yixiukeji.message.auth.AuthRequest;
import cn.com.yixiukeji.message.chat.ChatSendToOneRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private NettyClient nettyClient;

    @GetMapping("/auth")
    public String auth(  @RequestParam  String user) {
        AuthRequest authRequest = new AuthRequest().setAccessToken(user);
        nettyClient.send(new Invocation(AuthRequest.TYPE,  authRequest) );
        return "success";

    }

    @GetMapping("/chat")
    public String mock(  @RequestParam  String toUser,@RequestParam String content) {
        ChatSendToOneRequest authRequest = new ChatSendToOneRequest()
                .setToUser(toUser)
                .setContent(content)
                .setMsgId(UUID.randomUUID().toString())
                ;
        nettyClient.send(new Invocation(ChatSendToOneRequest.TYPE,  authRequest) );
        return "success";

    }
}
