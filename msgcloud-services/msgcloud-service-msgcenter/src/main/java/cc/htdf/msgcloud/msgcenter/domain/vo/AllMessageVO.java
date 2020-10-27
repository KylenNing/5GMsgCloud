package cc.htdf.msgcloud.msgcenter.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/8/22
 * @Description: TODO
 */
@Data
public class AllMessageVO {

    private int id;

    private Integer sendToAll;

    private String serviceId;

    private String sendTime;

    private Integer messageStatus;

    private String messageType;

    private Integer createdOrg;

    private Integer createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedTime;

    private String createdUserName;
}