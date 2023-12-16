package cn.com.yixiukeji.config;

import cn.com.yixiukeji.dispacher.MessageDispatcher;
import cn.com.yixiukeji.dispacher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyClientConfig {
    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }


}
