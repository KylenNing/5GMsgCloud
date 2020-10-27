package cc.htdf.msgcloud.msgcenter.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgH5ProductPageDTO {

    private Integer totolSize;
    private List<MsgH5ProductDTO> data;
}
