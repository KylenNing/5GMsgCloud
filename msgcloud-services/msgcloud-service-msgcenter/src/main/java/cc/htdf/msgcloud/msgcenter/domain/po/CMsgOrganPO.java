package cc.htdf.msgcloud.msgcenter.domain.po;

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
@TableName("c_msg_organ")
public class CMsgOrganPO {

    private int id;

    private Integer organArea;

    private Integer organType;

    private String organName;

    private String organDescribe;

    private Integer parentId;

    private Integer level;

    private Integer sort;

    private Integer isAvailable;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
