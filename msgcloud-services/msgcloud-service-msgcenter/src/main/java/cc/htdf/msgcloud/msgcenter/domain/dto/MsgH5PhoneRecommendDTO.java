package cc.htdf.msgcloud.msgcenter.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description: TODO
 */
@Data
public class MsgH5PhoneRecommendDTO {

    private List<MsgH5ProductDTO> recommend;

    private List<MsgH5ProductDTO> selected;

}
