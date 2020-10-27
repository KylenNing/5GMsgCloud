package cc.htdf.msgcloud.msgcenter.domain.dto;

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
public class TextMsgNlpDTO {

    private String messageId;

    private String clientId;

    private String encrypt = "false";

    private List<String> text;
}
