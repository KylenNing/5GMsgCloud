package cc.htdf.msgcloud.msgcenter.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Data
public class MsgH5ProductVO {

    private int id;

    private String title;

    private Integer organId;

    private Integer modular;

    private String tagId;

    private Integer type;

    private String mediaUrl;

    private String mediaSlUrl;

    private String mediaRcUrl;

    private String mediaRcSlUrl;

    private String describe;

    private String suitable;

    private String avoid;

    private Integer status;

    private String reason;

    private Integer viewCount;

    private Integer upCount;

    private Integer browseCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date releaseTime;

    private String createdOrg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    private String updatedBy;
}
