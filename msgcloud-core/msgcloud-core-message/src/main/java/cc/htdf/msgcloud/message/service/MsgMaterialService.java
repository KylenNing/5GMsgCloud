package cc.htdf.msgcloud.message.service;

import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import com.baomidou.mybatisplus.service.IService;

/**
 * author: JT
 * date: 2020/8/20
 * title:
 */
public interface MsgMaterialService extends IService<MsgMaterialPO> {

    MsgMaterialPO findById(String templateImageId);

}
