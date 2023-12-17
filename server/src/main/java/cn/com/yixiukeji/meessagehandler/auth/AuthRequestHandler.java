package cn.com.yixiukeji.meessagehandler.auth;

import cn.com.yixiukeji.codec.Invocation;
import cn.com.yixiukeji.dispacher.MessageHandler;
import cn.com.yixiukeji.message.auth.AuthRequest;
import cn.com.yixiukeji.message.auth.AuthResponse;
import cn.com.yixiukeji.server.NettyChannelManager;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Resource
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest message) {
        if (StringUtils.isEmpty(message.getAccessToken())) {
            AuthResponse authResponse = new AuthResponse()
                    .setCode(1)
                    .setMessage("认证accessToken 未传入");
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }

        //todo 校验

        nettyChannelManager.addUser(channel, message.getAccessToken());

        AuthResponse authResponse = new AuthResponse().setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));

    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
