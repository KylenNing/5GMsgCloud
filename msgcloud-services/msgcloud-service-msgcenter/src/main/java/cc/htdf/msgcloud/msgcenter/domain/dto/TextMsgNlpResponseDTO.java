package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * author: JT
 * date: 2020/8/14
 * title:
 */
@Setter
@Getter
public class TextMsgNlpResponseDTO {

    private String clientId;

    private String messageId;

    @JSONField(name = "result")
    private List actions;
}
