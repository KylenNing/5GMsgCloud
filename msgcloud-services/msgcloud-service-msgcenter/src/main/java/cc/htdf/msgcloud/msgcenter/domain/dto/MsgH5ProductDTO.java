package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description: TODO
 */
@Data
public class MsgH5ProductDTO {

    private int id;

    private String title;

    private Integer organId;

    private String organName;

    private Integer modular;

    private List<String> tagId;

    private List<Map> tagName;

    private Integer type;

    private String mediaUrl;

    private String mediaSlUrl;

    private String mediaRcUrl;

    private String mediaRcSlUrl;

    private String describe;

    private List<String> suitable;

    private List<String> avoid;

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
