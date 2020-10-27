package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.dto.CMsgOrganTreeDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgOrganPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.po.MsgAreaPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.CMsgOrganVO;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgOrganMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgRoleMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgUserMapper;
import cc.htdf.msgcloud.msgcenter.mapper.MsgAreaMapper;
import cc.htdf.msgcloud.msgcenter.service.CMsgOrganService;
import com.baomidou.mybatisplus.mapper.Condition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: guozx
 * @Date: 2020/8/7
 * @Description:
 */
@Service
public class CMsgOrganServiceImpl implements CMsgOrganService {

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private CMsgRoleMapper cMsgRoleMapper;

    @Resource
    private MsgAreaMapper msgAreaMapper;

    @Override
    public String createOrgan(CMsgUserPO user, CMsgOrganVO cMsgOrganVO) {
        int count = cMsgOrganMapper.getOrganNameCount(cMsgOrganVO.getLabel());
        if (count != 0) {
            return "组织名重复";
        }
        CMsgOrganPO cMsgOrganPO = new CMsgOrganPO();
        cMsgOrganPO.setOrganArea(cMsgOrganVO.getOrganArea());
        cMsgOrganPO.setOrganType(cMsgOrganVO.getOrganType());
        cMsgOrganPO.setCreatedOrg(String.valueOf(user.getOrganId()));
        cMsgOrganPO.setCreatedBy(String.valueOf(user.getId()));
        cMsgOrganPO.setCreatedTime(new Date());
        cMsgOrganMapper.insert(makeCMsgOrganPO(cMsgOrganPO, cMsgOrganVO));
        return "创建成功";
    }

    private CMsgOrganPO makeCMsgOrganPO(CMsgOrganPO cMsgOrganPO, CMsgOrganVO cMsgOrganVO) {
        cMsgOrganPO.setOrganName(cMsgOrganVO.getLabel());
        cMsgOrganPO.setOrganDescribe(cMsgOrganVO.getOrganDescribe());
        cMsgOrganPO.setParentId(cMsgOrganVO.getParentId());
        cMsgOrganPO.setLevel(cMsgOrganVO.getLevel());
        cMsgOrganPO.setSort(cMsgOrganVO.getSort());
        if (cMsgOrganVO.getIsAvailable() == true) {
            cMsgOrganPO.setIsAvailable(1);
        } else {
            cMsgOrganPO.setIsAvailable(0);
        }
        return cMsgOrganPO;
    }

    @Override
    public String updateOrgan(CMsgUserPO user, CMsgOrganVO cMsgOrganVO) {
        int count = cMsgOrganMapper.getOrganNameCountAndId(cMsgOrganVO.getLabel(), String.valueOf(cMsgOrganVO.getId()));
        if (count != 0) {
            return "组织名重复";
        }
        if (cMsgOrganVO.getIsAvailable() == false) { // 更新时将有效置为无效
            int userCount = cMsgUserMapper.getUserCountByOrganId(String.valueOf(cMsgOrganVO.getId()));
            if (userCount != 0) {
                return "该组织下存在用户,不能删除";
            }
            int roleCount = cMsgRoleMapper.getRoleCountByOrganId(String.valueOf(cMsgOrganVO.getId()));
            if (roleCount != 0) {
                return "该组织下存在角色,不能删除";
            }
        }
        CMsgOrganPO cMsgOrganPO = new CMsgOrganPO();
        cMsgOrganPO.setId(cMsgOrganVO.getId());
        cMsgOrganPO.setOrganArea(cMsgOrganVO.getOrganArea());
        cMsgOrganPO.setOrganType(cMsgOrganVO.getOrganType());
        cMsgOrganPO.setUpdatedBy(String.valueOf(user.getId()));
        cMsgOrganPO.setUpdatedTime(new Date());
        cMsgOrganPO.setCreatedOrg(cMsgOrganVO.getCreatedOrg());
        cMsgOrganPO.setCreatedBy(cMsgOrganVO.getCreatedBy());
        cMsgOrganPO.setCreatedTime(cMsgOrganVO.getCreatedTime());
        cMsgOrganMapper.updateById(makeCMsgOrganPO(cMsgOrganPO, cMsgOrganVO));
        return "更新成功";
    }

    @Override
    public List<CMsgOrganTreeDTO> getOrganTree(CMsgUserPO user) {
        List<CMsgOrganPO> CMsgOrganPOList = cMsgOrganMapper.getOrganTree(String.valueOf(user.getOrganId()));
        List<CMsgOrganTreeDTO> treeNodes = new ArrayList<>();
        CMsgOrganTreeDTO cMsgOrganTreeDTO;
        for (CMsgOrganPO cMsgOrganPO : CMsgOrganPOList) {
            if (cMsgOrganPO.getIsAvailable() == 0) {
                continue;
            }
            cMsgOrganTreeDTO = makeCMsgOrganTreeDTO(cMsgOrganPO);
            treeNodes.add(cMsgOrganTreeDTO);
        }
        return makeTree(treeNodes, String.valueOf(user.getOrganId()));
    }

    @Override
    public List<CMsgOrganPO> findOrgs() {
        return cMsgOrganMapper.selectList(new Condition());
    }

    @Override
    public List<Integer> findOrgsByOrganId(Integer organId) {
        List<Integer> orgs = new ArrayList<>();
        List<CMsgOrganPO> CMsgOrganPOList = cMsgOrganMapper.getOrganTree(String.valueOf(organId));
        for (CMsgOrganPO cMsgOrganPO : CMsgOrganPOList) {
            if (cMsgOrganPO.getIsAvailable() != 0) {
                orgs.add(cMsgOrganPO.getId());
            }
        }
        return orgs;
    }

    @Override
    public CMsgOrganVO findOrganAreaAndType(Integer organId) {
        CMsgOrganVO cMsgOrganVO = new CMsgOrganVO();
        CMsgOrganPO cMsgOrganPO = cMsgOrganMapper.selectById(organId);
        cMsgOrganVO.setOrganArea(cMsgOrganPO.getOrganArea());
        cMsgOrganVO.setOrganType(cMsgOrganPO.getOrganType());
        return cMsgOrganVO;
    }

    @Override
    public List<Map> getOrganType(CMsgUserPO user) {
        List<Map> mapList = new ArrayList<>();
        String type = user.getOrganType();
        String[] types = type.split(",");
        List<String> lists = Arrays.asList(types);
        for (String list : lists) {
            Map map = new HashMap();
            if ("1".equals(list)) {
                map.put("id", 1);
                map.put("name", "气象局");
            } else {
                map.put("id", 2);
                map.put("name", "公服中心");
            }
            mapList.add(map);
        }
        return mapList;
    }

    private List<CMsgOrganTreeDTO> makeTree(List<CMsgOrganTreeDTO> treeNodes, String organId) {
        List<CMsgOrganTreeDTO> trees = new ArrayList<>();
        for (CMsgOrganTreeDTO cMsgOrganTreeDTO : treeNodes) {
            if (organId.equals(String.valueOf(cMsgOrganTreeDTO.getId()))) {
                trees.add(findChildren(cMsgOrganTreeDTO, treeNodes));
            }
        }
        return trees;
    }

    private CMsgOrganTreeDTO findChildren(CMsgOrganTreeDTO DTO, List<CMsgOrganTreeDTO> DTOList) {
        for (CMsgOrganTreeDTO cMsgOrganTreeDTO : DTOList) {
            if (DTO.getId() == cMsgOrganTreeDTO.getParentId()) {
                if (DTO.getChildren() == null) {
                    DTO.setChildren(new ArrayList<>());
                }
                DTO.add(findChildren(cMsgOrganTreeDTO, DTOList));
            }

        }
        return DTO;
    }

    private CMsgOrganTreeDTO makeCMsgOrganTreeDTO(CMsgOrganPO cMsgOrganPO) {
        CMsgOrganTreeDTO cMsgOrganTreeDTO = new CMsgOrganTreeDTO();
        cMsgOrganTreeDTO.setId(cMsgOrganPO.getId());
        cMsgOrganTreeDTO.setOrganArea(cMsgOrganPO.getOrganArea());
        cMsgOrganTreeDTO.setOrganType(cMsgOrganPO.getOrganType());
        cMsgOrganTreeDTO.setLabel(cMsgOrganPO.getOrganName());
        if (cMsgOrganPO.getOrganArea() != null) {
            MsgAreaPO msgAreaPO = msgAreaMapper.selectById(cMsgOrganPO.getOrganArea());
            cMsgOrganTreeDTO.setAreaName(msgAreaPO.getAreaName());
        }
        if (cMsgOrganPO.getOrganType() != null) {
            if (cMsgOrganPO.getOrganType() == 1) {
                cMsgOrganTreeDTO.setOrganName("气象局");
            } else if (cMsgOrganPO.getOrganType() == 2) {
                cMsgOrganTreeDTO.setOrganName("公服中心");
            }
        }
        cMsgOrganTreeDTO.setOrganDescribe(cMsgOrganPO.getOrganDescribe());
        cMsgOrganTreeDTO.setParentId(cMsgOrganPO.getParentId());
        cMsgOrganTreeDTO.setLevel(cMsgOrganPO.getLevel());
        cMsgOrganTreeDTO.setSort(cMsgOrganPO.getSort());
        if (cMsgOrganPO.getIsAvailable() == 0) {
            cMsgOrganTreeDTO.setIsAvailable(false);
        } else {
            cMsgOrganTreeDTO.setIsAvailable(true);
        }
        cMsgOrganTreeDTO.setCreatedOrg(cMsgOrganPO.getCreatedOrg());
        cMsgOrganTreeDTO.setCreatedBy(cMsgOrganPO.getCreatedBy());
        cMsgOrganTreeDTO.setCreatedTime(cMsgOrganPO.getCreatedTime());
        cMsgOrganTreeDTO.setUpdatedBy(cMsgOrganPO.getUpdatedBy());
        cMsgOrganTreeDTO.setUpdatedTime(cMsgOrganPO.getUpdatedTime());
        return cMsgOrganTreeDTO;
    }
}
