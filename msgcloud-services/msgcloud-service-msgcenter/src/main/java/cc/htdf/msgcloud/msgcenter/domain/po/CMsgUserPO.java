package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    @TableField(exist = false)
    private List<Integer> orgs;

    @TableField(exist = false)
    private Integer organArea;

    @TableField(exist = false)
    private String organType;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
