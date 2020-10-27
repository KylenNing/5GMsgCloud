package cc.htdf.msgcloud.executor.base.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @Author: ningyq
 * @Date: 2020/6/9
 * @Description: TODO
 */
@Data
@TableName("b_area")
public class BAreaPO {
    private Integer id;
    private String areaname;
    private Integer parentid;
    private String shortname;
    private String lng;
    private String lat;
    private Integer level;
    private String position;
    private Integer sort;


}