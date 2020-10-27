package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgUserDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgUserVO;
import com.baomidou.mybatisplus.service.IService;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface CMsgUserService extends IService<CMsgUserPO> {

    String createUser(CMsgUserPO user, CMsgUserVO cMsgUserVO);

    String updateUser(CMsgUserPO user, CMsgUserVO cMsgUserVO);

    MsgPageDTO getUserListByOrganId(String organId, Integer pageNum, Integer pageSize);

    CMsgUserPO findByUseracountAndPass(String userAcount, String password);

    CMsgUserDTO getUserByUserId(String userId);

    CMsgUserPO findByUseraccount(String userAccount);
}
