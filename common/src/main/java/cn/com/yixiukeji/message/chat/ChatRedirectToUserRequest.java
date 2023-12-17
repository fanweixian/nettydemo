package cn.com.yixiukeji.message.chat;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ChatRedirectToUserRequest implements Message {

    public static final String TYPE = "ChatRedirectToUserRequest";
    private String msgId;
    private String content;
    private String fromUser ;
}
