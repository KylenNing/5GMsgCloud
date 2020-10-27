package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgUserDTO {

    private String id;
    private String userLableId;
    private String userLableName;
    private String userName;
    private String userTel;

    private String userAttributeId;
    private String sex;
    private String age;
    private String work;
    private String travel;
    private String attributeRemarks;
    private String userBehaviorId;
    private String area;
    private String active;
    private String address;
    private String changeAddress;

    private String createdOrg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    private String createdUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    private String updatedUser;
}
