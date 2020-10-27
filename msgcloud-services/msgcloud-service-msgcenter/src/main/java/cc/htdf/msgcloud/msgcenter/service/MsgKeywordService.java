package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.KeyWordPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordDictPO;
import cc.htdf.msgcloud.msgcenter.domain.po.BMsgKeywordPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: ningyq
 * @Date: 2020/8/11
 * @Description: TODO
 */
public interface MsgKeywordService {

    Map getAllKeyWord(CMsgUserPO user,String keyword, Integer currentPage, Integer pageSize,String serviceId);

    String addKeyWord(CMsgUserPO user,KeyWordPageDTO dto);

    void deleteKeyWordById(Integer id);

    KeyWordPageDTO getKeyWordInfoById(Integer keyWordId);

    List<BMsgKeywordPO> findByKeyworkAndServiceId(String text, String id);

    Set<BKeywordDictPO> getAllKeyWordByServiceId(String serviceId);

}
