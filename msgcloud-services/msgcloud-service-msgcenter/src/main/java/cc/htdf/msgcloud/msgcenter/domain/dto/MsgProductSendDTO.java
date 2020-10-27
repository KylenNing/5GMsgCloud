package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description: TODO
 */
@Data
public class MsgProductSendDTO {

    private int id;

    private String productTitle;

    private String productDescribe;

    private String pictureName;

    private String pictureLocalUrl;

    private String productSendAreaName;

    private String serviceName;

    private String userLabelName;

    private Integer messageStatus;

    private String reason;

    private String createdOrg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    private String updatedBy;
}
