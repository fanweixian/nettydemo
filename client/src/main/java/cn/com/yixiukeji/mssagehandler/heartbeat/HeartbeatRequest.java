package cn.com.yixiukeji.mssagehandler.heartbeat;


import cn.com.yixiukeji.dispacher.Message;
import lombok.Data;

@Data
public class HeartbeatRequest implements Message {
    public final static String TYPE = "HeartbeatRequest";
}
