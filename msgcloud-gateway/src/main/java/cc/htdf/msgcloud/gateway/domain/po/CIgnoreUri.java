package cc.htdf.msgcloud.gateway.domain.po;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
@Setter
@Getter
@TableName("c_ignore_uri")
public class CIgnoreUri {

    @TableId
    private Integer id;

    private String ignoreUri;
}
