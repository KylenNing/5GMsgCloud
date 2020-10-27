package cc.htdf.msgcloud.msgcenter.domain.vo;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgMenuDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/8/14
 * @Description: TODO
 */
@Data
public class MsgMessageVO {

    private String serviceId;

    private Integer groupId;

    private List<Map<String,String>> templateList;

    private List<String> labelList;

    private List<MsgMenuDTO> menuList;

    private Integer sendToAll;

    private String sendTime;

    private String createdBy;

    private String createdOrg;
}