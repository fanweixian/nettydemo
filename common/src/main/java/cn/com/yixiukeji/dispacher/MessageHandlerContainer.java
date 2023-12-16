package cn.com.yixiukeji.dispacher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MessageHandlerContainer implements InitializingBean {

    private final Map<String, MessageHandler> handlers = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MessageHandler.class).values()
                .forEach(m -> handlers.put(m.getType(), m));
        log.info("[afterPropertiesSet] [消息处理器数量：{}]", handlers.size());
    }

    public MessageHandler getMessageHandler(String type) {
        return Optional.ofNullable(handlers.get(type))
                .orElseThrow(() -> new IllegalArgumentException("找不到"));
    }

    public static Class<? extends Message> getMessageClass(MessageHandler hanler) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(hanler);
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();

        while ((Objects.isNull(interfaces) || interfaces.length == 0) && Objects.nonNull(superclass)) {
            //此处，是以父类接口为准
            interfaces = superclass.getGenericInterfaces();
            superclass = targetClass.getSuperclass();
        }
        if (Objects.nonNull(interfaces)) {
            for (Type type : interfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0) {
                            return (Class<Message>) actualTypeArguments[0];
                        } else {
                            throw new IllegalStateException("类型不对");
                        }
                    }

                }
            }
        }
        throw new IllegalStateException("类型不对");
    }
}
