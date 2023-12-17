package cn.com.yixiukeji.message.auth;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AuthResponse  implements Message {

    public static final String TYPE = "AuthResponse";

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;
}
