package cc.htdf.msgcloud.msgcenter.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: ningyq
 * @Date: 2020/10/9
 * @Description: TODO
 */
@Data
public class BH5ProductTourVO {

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

    private boolean recommendFlag;

    private Integer relatedArticle;

    private Integer status;

    private String reason;

    private String publishOrg;

    private Integer createdOrg;

    private String orgName;

    private Integer createdBy;

    private String createdName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
}