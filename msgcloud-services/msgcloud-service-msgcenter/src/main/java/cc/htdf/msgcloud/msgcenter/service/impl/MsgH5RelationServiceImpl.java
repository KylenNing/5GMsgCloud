package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.mapper.MsgH5RelationMapper;
import cc.htdf.msgcloud.msgcenter.service.MsgH5RelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: guozx
 * @Date: 2020/10/21
 * @Description:
 */
@Service
public class MsgH5RelationServiceImpl implements MsgH5RelationService {

    @Resource
    private MsgH5RelationMapper msgH5RelationMapper;
}
