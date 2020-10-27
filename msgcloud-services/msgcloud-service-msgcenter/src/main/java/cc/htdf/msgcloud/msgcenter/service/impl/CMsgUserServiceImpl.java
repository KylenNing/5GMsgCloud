package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.common.utils.PageUtil;
import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgUserDTO;
import cc.htdf.msgcloud.msgcenter.domain.dto.MsgPageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgOrganPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgRolePO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgUserVO;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgOrganMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgRoleMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgUserMapper;
import cc.htdf.msgcloud.msgcenter.service.CMsgUserService;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class CMsgUserServiceImpl extends ServiceImpl<CMsgUserMapper, CMsgUserPO> implements CMsgUserService {

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private CMsgRoleMapper cMsgRoleMapper;


    @Override
    public String createUser(CMsgUserPO user, CMsgUserVO cMsgUserVO) {
        int count = cMsgUserMapper.getUserAccountCount(cMsgUserVO.getUserAccount());
        if (count != 0) {
            return "账号重复";
        }
        CMsgUserPO cMsgUserPO = new CMsgUserPO();
        cMsgUserPO.setOrganId(cMsgUserVO.getOrganId());
        cMsgUserPO.setRoleId(cMsgUserVO.getRoleId());
        cMsgUserPO.setUserName(cMsgUserVO.getUserName());
        cMsgUserPO.setUserDescribe(cMsgUserVO.getUserDescribe());
        cMsgUserPO.setUserAccount(cMsgUserVO.getUserAccount());
        String password = DigestUtils.md5Hex(cMsgUserVO.getUserPassword());
        cMsgUserPO.setUserPassword(password);
        cMsgUserPO.setUserTel(cMsgUserVO.getUserTel());
        cMsgUserPO.setUserMail(cMsgUserVO.getUserMail());
        if (cMsgUserVO.getIsAvailable() == true) {
            cMsgUserPO.setIsAvailable(1);
        } else {
            cMsgUserPO.setIsAvailable(0);
        }
        cMsgUserPO.setCreatedOrg(String.valueOf(user.getOrganId()));
        cMsgUserPO.setCreatedBy(String.valueOf(user.getId()));
        cMsgUserPO.setCreatedTime(new Date());
        cMsgUserMapper.insert(cMsgUserPO);
        return "创建成功";
    }

    @Override
    public String updateUser(CMsgUserPO user, CMsgUserVO cMsgUserVO) {
        CMsgUserPO cMsgUserPO = new CMsgUserPO();
        cMsgUserPO.setId(cMsgUserVO.getId());
        cMsgUserPO.setUpdatedBy(String.valueOf(user.getId()));
        cMsgUserPO.setUpdatedTime(new Date());
        cMsgUserPO.setCreatedOrg(cMsgUserVO.getCreatedOrg());
        cMsgUserPO.setCreatedBy(cMsgUserVO.getCreatedBy());
        cMsgUserPO.setCreatedTime(cMsgUserVO.getCreatedTime());
        cMsgUserPO.setOrganId(cMsgUserVO.getOrganId());
        cMsgUserPO.setRoleId(cMsgUserVO.getRoleId());
        cMsgUserPO.setUserName(cMsgUserVO.getUserName());
        cMsgUserPO.setUserDescribe(cMsgUserVO.getUserDescribe());
        cMsgUserPO.setUserAccount(cMsgUserVO.getUserAccount());
        cMsgUserPO.setUserPassword(cMsgUserVO.getUserPassword());
        cMsgUserPO.setUserTel(cMsgUserVO.getUserTel());
        cMsgUserPO.setUserMail(cMsgUserVO.getUserMail());
        if (cMsgUserVO.getIsAvailable() == true) {
            cMsgUserPO.setIsAvailable(1);
        } else {
            cMsgUserPO.setIsAvailable(0);
        }
        cMsgUserMapper.updateById(cMsgUserPO);
        return "更新成功";
    }

    @Override
    public MsgPageDTO getUserListByOrganId(String organId, Integer pageNum, Integer pageSize) {
        List<CMsgUserDTO> cMsgUserDTOList = new ArrayList<>();
        List<CMsgOrganPO> CMsgOrganPOList = cMsgOrganMapper.getOrganTree(organId);
        for (CMsgOrganPO cMsgOrganPO : CMsgOrganPOList) {
            List<CMsgUserPO> CMsgUserPOList = cMsgUserMapper.getUserByOrganId(String.valueOf(cMsgOrganPO.getId()));
            for (CMsgUserPO cMsgUserPO : CMsgUserPOList) {
                CMsgUserDTO CMsgUserDTO = makeCMsgUserDTO(cMsgUserPO);
                CMsgRolePO cMsgRolePO = cMsgRoleMapper.selectById(cMsgUserPO.getRoleId());
                CMsgUserDTO.setCMsgRolePO(cMsgRolePO);
                cMsgUserDTOList.add(CMsgUserDTO);
            }
        }
        MsgPageDTO pageDTO = new MsgPageDTO();
        pageDTO.setTotolSize(cMsgUserDTOList.size());
        pageDTO.setData(PageUtil.startPage(cMsgUserDTOList, pageNum, pageSize));
        return pageDTO;
    }

    @Override
    public CMsgUserPO findByUseracountAndPass(String userAcount, String password) {
        Condition condition = new Condition();
        condition
                .eq("USER_ACCOUNT", userAcount)
                .and().eq("USER_PASSWORD", password);
        return this.selectOne(condition);
    }

    @Override
    public CMsgUserDTO getUserByUserId(String userId) {
        CMsgUserPO cMsgUserPO = cMsgUserMapper.selectById(userId);
        CMsgUserDTO CMsgUserDTO = makeCMsgUserDTO(cMsgUserPO);
        CMsgRolePO cMsgRolePO = cMsgRoleMapper.selectById(cMsgUserPO.getRoleId());
        CMsgUserDTO.setCMsgRolePO(cMsgRolePO);
        return CMsgUserDTO;
    }

    @Override
    public CMsgUserPO findByUseraccount(String userAccount) {
        Condition condition = new Condition();
        condition
                .eq("USER_ACCOUNT", userAccount);
        return this.selectOne(condition);
    }

    private CMsgUserDTO makeCMsgUserDTO(CMsgUserPO cMsgUserPO) {
        CMsgUserDTO cMsgUserDTO = new CMsgUserDTO();
        cMsgUserDTO.setId(cMsgUserPO.getId());
        cMsgUserDTO.setOrganId(cMsgUserPO.getOrganId());
        cMsgUserDTO.setUserName(cMsgUserPO.getUserName());
        cMsgUserDTO.setUserDescribe(cMsgUserPO.getUserDescribe());
        cMsgUserDTO.setUserAccount(cMsgUserPO.getUserAccount());
        cMsgUserDTO.setUserPassword(cMsgUserPO.getUserPassword());
        cMsgUserDTO.setUserTel(cMsgUserPO.getUserTel());
        cMsgUserDTO.setUserMail(cMsgUserPO.getUserMail());
        if (cMsgUserPO.getIsAvailable() == 0) {
            cMsgUserDTO.setIsAvailable(false);
        } else {
            cMsgUserDTO.setIsAvailable(true);
        }
        cMsgUserDTO.setCreatedOrg(cMsgUserPO.getCreatedOrg());
        cMsgUserDTO.setCreatedBy(cMsgUserPO.getCreatedBy());
        cMsgUserDTO.setCreatedTime(cMsgUserPO.getCreatedTime());
        cMsgUserDTO.setUpdatedBy(cMsgUserPO.getUpdatedBy());
        cMsgUserDTO.setUpdatedTime(cMsgUserPO.getUpdatedTime());
        return cMsgUserDTO;
    }
}
