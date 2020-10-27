package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.BH5ProductTourMobileDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.BH5ProductTourPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;

import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/10/9
 * @Description: TODO
 */
public interface H5ProductTourService {

    String createTourProduct(CMsgUserPO user, BH5ProductTourPO po);

    void deleteTourProduct(Integer tourId);

    String updateTourProduct(CMsgUserPO user, BH5ProductTourPO p);

    Map getAllTourProduct(CMsgUserPO user, Integer currentPage, Integer pageSize, String tourName, Integer isRecommend);

    Map getAllTourProductMobile(Integer organId, Integer currentPage, Integer pageSize, String tourName, Integer isRecommend, Integer tagId);

    BH5ProductTourPO getTourById(Integer tourId);

    BH5ProductTourMobileDTO getTourByIdMobile(Integer tourId);

    String setRecommandProduct(Integer tourId, Integer isIRecommend);

    String downProduct(Integer tourId);
    
}
