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
public class MsgProductDTO {

    private int id;

    private String ProductTitle;

    private String ProductStyle;

    private Integer ProductArea;

    private String ProductAreaName;

    private String ProductDescribe;

    private Integer messageStatus;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}
