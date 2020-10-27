package cc.htdf.msgcloud.msgcenter.domain.po;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/10/9
 * @Description: TODO
 */
@Data
@TableName("b_h5product_tour")
public class BH5ProductTourPO {

    @TableId(type = IdType.AUTO)
    private int id;

    private String name;

    private String ticketPrice;

    private String province;

    private String city;

    private String area;

    private String address;

    private String openDay;

    private String businessTime;

    private String mobilePhone;

    private String telephone;

    private String discount;

    private String introduce;

    private String recommendReason;

    private Integer isRecommend;

    private Integer relatedArticle;

    private Integer status;

    private String reason;

    private Integer createdOrg;

    private Integer createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}