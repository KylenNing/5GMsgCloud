package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgOrganTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgOrganPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgOrganVO;

import java.util.List;
import java.util.Map;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface CMsgOrganService {

    String createOrgan(CMsgUserPO user, CMsgOrganVO cMsgOrganVO);

    String updateOrgan(CMsgUserPO user, CMsgOrganVO cMsgOrganVO);

    List<CMsgOrganTreeDTO> getOrganTree(CMsgUserPO user);

    List<CMsgOrganPO> findOrgs();

    List<Integer> findOrgsByOrganId(Integer organId);

    CMsgOrganVO findOrganAreaAndType(Integer organId);

    List<Map> getOrganType(CMsgUserPO user);
}
