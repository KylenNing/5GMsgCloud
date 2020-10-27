package cc.htdf.msgcloud.msgcenter.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgUserVO {

    private String id;

    private String userName;

    private String userTel;

    private String labelId;

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

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
