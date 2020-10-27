package cc.htdf.msgcloud.msgcenter.domain.dto;

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
public class CMsgRoleDTO {

    private int id;

    private Integer organId;

    private String organName;

    private String roleName;

    private Integer roleType;

    private String roleDescribe;

    private Boolean isAvailable;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    private List<Integer> menuId;
}
