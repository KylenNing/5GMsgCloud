package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/31
 * @Description:
 */
@Data
public class MsgAreaTreeDTO {

    private int id;

    private String areaName;

    private Integer parentId;

    private boolean disabled;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<MsgAreaTreeDTO> children = new ArrayList<MsgAreaTreeDTO>();

    public void add(MsgAreaTreeDTO node) {
        children.add(node);
    }
}
