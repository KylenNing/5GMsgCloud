package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgMenuTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgMenuVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
public interface CMsgMenuService {

    String createMenu(CMsgUserPO user, CMsgMenuVO cMsgMenuVO);

    String updateMenu(CMsgUserPO user, CMsgMenuVO cMsgMenuVO);

    List<CMsgMenuTreeDTO> getMenuTree(CMsgUserPO user);

    List<CMsgMenuTreeDTO> findMenuTreeAllByUserId(CMsgUserPO user);

    List<Map> getButtonTypeList();

    List<Map> getButtonDisPlayList(CMsgUserPO user, String menuId);
}
