package cn.com.yixiukeji.message.chat;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ChatSendToOneRequest implements Message {

    public static final String TYPE = "ChatSendToOneRequest";

    private String toUser;
    private String msgId;
    private String content;
}
