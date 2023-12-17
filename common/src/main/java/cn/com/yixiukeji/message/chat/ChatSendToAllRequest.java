package cn.com.yixiukeji.message.chat;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ChatSendToAllRequest implements Message{

    public static final String TYPE = "ChatSendToAllRequest";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;
}
