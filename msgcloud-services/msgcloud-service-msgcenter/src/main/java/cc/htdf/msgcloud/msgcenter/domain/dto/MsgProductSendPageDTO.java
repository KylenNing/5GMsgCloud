package cc.htdf.msgcloud.msgcenter.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgProductSendPageDTO {

    private Integer totolSize;
    private List<MsgProductSendDTO> data;
}
