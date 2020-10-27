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
public class MsgMaterialLabelVO {

    private String id;

    private String materialType;

    private String labelName;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
