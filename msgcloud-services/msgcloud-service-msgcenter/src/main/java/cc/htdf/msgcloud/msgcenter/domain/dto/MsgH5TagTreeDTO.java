package cc.htdf.msgcloud.msgcenter.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Data
public class MsgH5TagTreeDTO {

    private int id;

    private String label;

    private Integer parentId;

    private Integer level;

    private Integer type;

    private boolean disabled;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<MsgH5TagTreeDTO> children = new ArrayList<>();

    public void add(MsgH5TagTreeDTO node) {
        children.add(node);
    }
}
