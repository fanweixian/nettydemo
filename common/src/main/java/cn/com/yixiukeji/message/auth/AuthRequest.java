package cn.com.yixiukeji.message.auth;

import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class AuthRequest implements Message {
    public static final String TYPE = "AuthRequest";

    /**
     * 认证 Token
     */
    private String accessToken;
}
