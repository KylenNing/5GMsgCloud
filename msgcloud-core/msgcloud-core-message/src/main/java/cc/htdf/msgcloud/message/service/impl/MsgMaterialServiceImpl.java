package cc.htdf.msgcloud.message.service.impl;

import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.message.mapper.MsgMaterialMapper;
import cc.htdf.msgcloud.message.service.MsgMaterialService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
@Service
public class MsgMaterialServiceImpl extends ServiceImpl<MsgMaterialMapper, MsgMaterialPO> implements MsgMaterialService {


    @Override
    public MsgMaterialPO findById(String templateImageId) {
        Condition condition = new Condition();
        condition.eq("ID", templateImageId);
        return this.selectOne(condition);
    }
}
