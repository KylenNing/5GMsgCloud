package cc.htdf.msgcloud.common.domain;

import com.feinno.msgctenter.sdk.dto.Receiver;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Map;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Setter
@Getter
public class MsgDownInfo {


    private String serviceId;

    private Integer messageId;

    private LinkedList<String> templateList;

    private LinkedList<Map<String,Object>> menuList;

    private Integer sendToAll;

    /**
     * 接收者
     */
    private Receiver receiver;

}
