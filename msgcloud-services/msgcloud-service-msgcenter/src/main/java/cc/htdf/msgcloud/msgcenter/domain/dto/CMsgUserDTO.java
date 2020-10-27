package cc.htdf.msgcloud.msgcenter.domain.dto;

import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRolePO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class CMsgUserDTO {

    private int id;

    private Integer organId;

    private String userName;

    private String userDescribe;

    private String userAccount;

    private String userPassword;

    private String userTel;

    private String userMail;

    private Boolean isAvailable;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    private CMsgRolePO cMsgRolePO;
}
