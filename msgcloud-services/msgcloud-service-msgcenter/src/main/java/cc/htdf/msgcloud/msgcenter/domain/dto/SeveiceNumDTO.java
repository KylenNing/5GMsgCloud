package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: renxh
 * @Date: 2020/8/12
 * @Description:
 */
@Data
public class SeveiceNumDTO {

    private String id;
    private String chatbotId;
    private String channelId;
    private String chatbotName;
    private String appId;
    private String appSecret;
    private String cspCode;
    private String logoUrl;
    private String createdOrg;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    private String updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

}
