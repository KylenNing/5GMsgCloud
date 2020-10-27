package cc.htdf.msgcloud.message.domain.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/9/15
 * @Description: TODO
 */
@Data
@TableName("d_5g_life_index")
public class D5gLifeIndexPO {

    private int id;

    private Integer areacode;

    private String  areaname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishDate;

    private String indexName;

    private String indexLevel;

    private String indexContent;

    private String indexType;

    private Integer dataSource;
}