package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class CMsgOrganTreeDTO {

    private int id;

    private Integer organArea;

    private Integer organType;

    private String label;

    private String areaName;

    private String organName;

    private String organDescribe;

    private Integer parentId;

    private Integer level;

    private Integer sort;

    private Boolean isAvailable;

    private String createdOrg;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<CMsgOrganTreeDTO> children = new ArrayList<CMsgOrganTreeDTO>();

    public void add(CMsgOrganTreeDTO node) {
        children.add(node);
    }

}
