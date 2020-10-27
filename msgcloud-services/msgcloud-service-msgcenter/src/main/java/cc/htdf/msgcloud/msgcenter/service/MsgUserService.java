package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.MsgAreaTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgUserDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgUserLabelDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserLabelVO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgUserVO;

import java.util.List;
import java.util.Map;


/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface MsgUserService {

    MsgPageDTO getUserLabelList(CMsgUserPO user, String userLabelName, Integer pageNum, Integer pageSize);

    MsgPageDTO getUserList(String userLabelId, String userTel, CMsgUserPO user, String userName, Integer pageNum, Integer pageSize);

    String createUserLabel(CMsgUserPO user, MsgUserLabelVO msgUserLabelVO);

    String createUser(CMsgUserPO user, MsgUserVO msgUserVO);

    String deleteUserLabelByIds(String userLabelIds);

    String deleteUserByIds(String userIds);

    MsgUserLabelDTO getUserLabelById(String userLabelId);

    MsgUserDTO getUserById(String userId);

    List<MsgUserLabelDTO> getUserLabel(CMsgUserPO user);

    List<MsgAreaTreeDTO> getAreaList();

    Map getSexList();

    Map getWorkList();

    Map getTravelList();

    Map getActiveList();

}
