package cc.htdf.msgcloud.executor.base.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @Author: ningyq
 * @Date: 2020/6/9
 * @Description: TODO
 */
@Data
@TableName("b_area_nine")
public class BAreaNinePO {
    private Integer areaid;
    private String nameen;
    private String namecn;
    private String districten;
    private String districtcn;
    private String proven;
    private String provcn;
    private String nationen;
    private String nationcn;
    private Integer areaidsix;



}