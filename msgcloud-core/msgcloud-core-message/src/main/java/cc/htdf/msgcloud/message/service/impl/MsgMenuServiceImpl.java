package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.BMsgMenuPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplateButtonPO;
import cc.htdf.msgcloud.message.mapper.MsgMenuMapper;
import cc.htdf.msgcloud.message.service.MsgMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: JT
 * date: 2020/8/17
 * title:
 */
@Service
public class MsgMenuServiceImpl implements MsgMenuService {

    @Resource
    private MsgMenuMapper msgMenuMapper;

    @Override
    public List<BMsgMenuPO> findSuspensionMenusByKeyword(String keyword, String serviceId) {
        return msgMenuMapper.findSuspensionMenusByKeyword(keyword, serviceId);
    }

    @Override
    public List<BMsgMenuPO> findSuspensionMenusByGroupId(Integer groupId) {
        return msgMenuMapper.findSuspensionMenusByGroupId(groupId);
    }

    @Override
    public List<MsgTemplateButtonPO> findTemplateButtonsByTemplateId(String templateId) {
        return msgMenuMapper.findTemplateButtonsByTemplateId(templateId);
    }
}
