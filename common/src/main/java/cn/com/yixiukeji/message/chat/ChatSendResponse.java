package cn.com.yixiukeji.message.chat;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ChatSendResponse implements Message {
    public static final String TYPE = "ChatSendResponse";

    private String msgId;

    private Integer code;
    private String message;
}
