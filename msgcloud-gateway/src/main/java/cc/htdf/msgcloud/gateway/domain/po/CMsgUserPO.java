package cc.htdf.msgcloud.gateway.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
@TableName("c_msg_user")
public class CMsgUserPO {

    private int id;

    private Integer roleId;

    private Integer organId;

    private String userName;

    private String userDescribe;

    private String userAccount;

    private String userPassword;

    private String userTel;

    private String userMail;

    private Integer isAvailable;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}
