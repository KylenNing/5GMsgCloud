package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.BMsgKeywordPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplatePO;
import cc.htdf.msgcloud.message.mapper.MsgKeywordMapper;
import cc.htdf.msgcloud.message.mapper.MsgTemplateMapper;
import cc.htdf.msgcloud.message.service.MsgTemplateService;
import com.baomidou.mybatisplus.mapper.Condition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Service
public class MsgTemplateServiceImpl implements MsgTemplateService {

    @Resource
    private MsgTemplateMapper msgTemplateMapper;

    @Resource
    private MsgKeywordMapper msgKeywordMapper;

    @Override
    public LinkedList<MsgTemplatePO> findByChannelIdAndAction(Long channelId, String action) {
        return msgTemplateMapper.findByChannelIdAndKeyword(channelId, action);
    }

    @Override
    public LinkedList<MsgTemplatePO> findByGroupId(Integer groupId) {
        return msgTemplateMapper.findByGroupId(groupId);
    }

    @Override
    public BMsgKeywordPO findGroupByChannelIdAndAction(Long channelId, String action) {
        return msgKeywordMapper.selectByNameAndServiceId(channelId, action);
    }

    @Override
    public Map<String, MsgTemplatePO> findByTemplateIds(LinkedList<String> templateIds) {
        Condition condition = new Condition();
        condition.in("ID", templateIds);
        List<MsgTemplatePO> templateList = msgTemplateMapper.selectList(condition);
        return templateList.stream().collect(
                Collectors.toMap(
                        template -> template.getId(),
                        Function.identity(),
                        (existing, replace) -> existing
                )
        );
    }
}
