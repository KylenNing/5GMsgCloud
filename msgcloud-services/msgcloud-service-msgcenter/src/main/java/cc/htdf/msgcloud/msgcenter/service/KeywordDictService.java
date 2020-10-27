package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordDictPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;

import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/10/7
 * @Description: TODO
 */
public interface KeywordDictService {

    String createKeywordDict(CMsgUserPO user, BKeywordDictPO dict);

    void deleteKeywordDict(Integer dictId);

    String updateKeywordDict(CMsgUserPO user,BKeywordDictPO dict);

    Map getAllKeyWordDict(CMsgUserPO user, Integer currentPage, Integer pageSize, String dictName);
}