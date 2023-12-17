package cn.com.yixiukeji.codec;

import cn.com.yixiukeji.dispacher.Message;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通信协议的消息体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invocation {
    /**
     * 类型，用于匹配对应的消息处理器。如果类比 HTTP 协议，type 属性相当于请求地址。
     */
    private String type;
    /**
     * json格式
     */
    private String message;

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }
}
