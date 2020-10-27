package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.dto.BH5ProductTourMobileDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.BH5ProductTourPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.po.MsgH5RelationPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.BH5ProductTourVO;
import cc.htdf.msgcloud.msgcenter.mapper.*;
import cc.htdf.msgcloud.msgcenter.service.H5ProductTourService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: ningyq
 * @Date: 2020/10/9
 * @Description: TODO
 */
@Service
public class H5ProductTourServiceImpl implements H5ProductTourService {

    @Resource
    private BH5ProductTourMapper bh5ProductTourMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Resource
    private CMsgUserMapper cMsgUserMapper;

    @Resource
    private MsgH5ProductMapper msgH5ProductMapper;

    @Resource
    private MsgH5RelationMapper msgH5RelationMapper;

    @Override
    public String createTourProduct(CMsgUserPO user, BH5ProductTourPO po) {
        EntityWrapper<BH5ProductTourPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        entityWrapper.eq("name", po.getName());
        int count = bh5ProductTourMapper.selectCount(entityWrapper);
        if (count == 0) {
            int createdBy = user.getId();
            int createdOrg = user.getOrganId();
            po.setCreatedBy(createdBy);
            po.setCreatedOrg(createdOrg);
            po.setCreatedTime(new Date());
            bh5ProductTourMapper.insert(po);
            return "成功创建景点信息！";
        }
        {
            return "该景点名称已存在，请更换名称！";
        }

    }

    @Override
    public void deleteTourProduct(Integer tourId) {
        EntityWrapper<BH5ProductTourPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id", tourId);
        bh5ProductTourMapper.delete(entityWrapper);
    }

    @Override
    public String updateTourProduct(CMsgUserPO user, BH5ProductTourPO tour) {
        BH5ProductTourPO po = bh5ProductTourMapper.selectById(tour.getId());
        EntityWrapper<BH5ProductTourPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        entityWrapper.eq("name", tour.getName());
        int count = bh5ProductTourMapper.selectCount(entityWrapper);
        if (count == 0 || tour.getName().equals(po.getName())) {
            tour.setUpdatedTime(new Date());
            bh5ProductTourMapper.updateById(tour);
            // 已发布的数据插入到H5产品关系表中
            if (tour.getStatus() == 3) {
                MsgH5RelationPO msgH5RelationPO = new MsgH5RelationPO();
                msgH5RelationPO.setModular(8);
                msgH5RelationPO.setType(1);// 全模块先写死插入类型为1
                msgH5RelationPO.setProductId(tour.getId());
                msgH5RelationMapper.insert(msgH5RelationPO);
            }
            // 如果H5产品关系表里关联了此条数据则删除
            if (tour.getStatus() == 2) {
                MsgH5RelationPO msgH5RelationPO = msgH5RelationMapper.selectByProductId(Integer.valueOf(tour.getId()));
                if (msgH5RelationPO != null) {
                    msgH5RelationMapper.deleteById(msgH5RelationPO.getId());
                }
            }
            return "成功更新景点数据！";
        } else {
            return "该景点名称已经存在，请更换名称！";
        }

    }

    @Override
    public Map getAllTourProduct(CMsgUserPO user, Integer currentPage, Integer pageSize, String tourName, Integer isRecommend) {
        EntityWrapper<BH5ProductTourPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        if (Objects.nonNull(tourName) && !tourName.equals("")) {
            entityWrapper.like("name", tourName);
        }
        if (Objects.nonNull(isRecommend)) {
            entityWrapper.eq("is_recommend", isRecommend);
            entityWrapper.eq("status", 3);
        }
        entityWrapper.orderBy("created_time", false);
        Map resMap = new HashMap();
        if (Objects.isNull(currentPage) && Objects.isNull(pageSize)) {
            List<BH5ProductTourPO> poList = bh5ProductTourMapper.selectList(entityWrapper);
            List<BH5ProductTourVO> voList = new ArrayList<>();
            for (BH5ProductTourPO po : poList) {
                BH5ProductTourVO vo = new BH5ProductTourVO();
                BeanUtils.copyProperties(po, vo);
                Integer organId = msgH5ProductMapper.selectById(po.getRelatedArticle()).getOrganId();
                String orgName = cMsgOrganMapper.selectById(organId).getOrganName();
                vo.setOrgName(orgName);
                vo.setCreatedName(cMsgUserMapper.selectById(po.getCreatedBy()).getUserName());
                vo.setPublishOrg(cMsgOrganMapper.getOrganNameByTourId(po.getId()));
                vo.setRecommendFlag(po.getIsRecommend() == 0 ? false : true);
                voList.add(vo);
            }
            resMap.put("DATA", voList);
        } else {
            int totalSize = bh5ProductTourMapper.selectCount(entityWrapper);
            Page<BH5ProductTourPO> page = new Page<>(currentPage, pageSize);
            List<BH5ProductTourPO> poList = bh5ProductTourMapper.selectPage(page, entityWrapper);
            List<BH5ProductTourVO> voList = new ArrayList<>();
            for (BH5ProductTourPO po : poList) {
                BH5ProductTourVO vo = new BH5ProductTourVO();
                BeanUtils.copyProperties(po, vo);
                Integer organId = msgH5ProductMapper.selectById(po.getRelatedArticle()).getOrganId();
                String orgName = cMsgOrganMapper.selectById(organId).getOrganName();
                vo.setOrgName(orgName);
                vo.setCreatedName(cMsgUserMapper.selectById(po.getCreatedBy()).getUserName());
                vo.setPublishOrg(cMsgOrganMapper.getOrganNameByTourId(po.getId()));
                vo.setRecommendFlag(po.getIsRecommend() == 0 ? false : true);
                voList.add(vo);
            }
            resMap.put("DATA", voList);
            resMap.put("TotalSize", totalSize);
        }

        return resMap;
    }


    @Override
    //@TargetDataSource(DBName.DB_HTDF_MSGCLOUD_TEST)
    public Map getAllTourProductMobile(Integer organId, Integer currentPage, Integer pageSize, String tourName, Integer isRecommend, Integer tagId) {
        EntityWrapper<BH5ProductTourPO> entityWrapper = new EntityWrapper<>();
        if (Objects.nonNull(tourName) && !tourName.equals("")) {
            entityWrapper.like("name", tourName);
        }
        if (Objects.nonNull(isRecommend)) {
            entityWrapper.eq("is_recommend", isRecommend);
        }
        entityWrapper.eq("STATUS", 3);
        String organName = cMsgOrganMapper.selectById(organId).getOrganName();
        Map resMap = new HashMap();
        if (Objects.isNull(currentPage) && Objects.isNull(pageSize)) {
            List<BH5ProductTourPO> poList = bh5ProductTourMapper.selectList(entityWrapper);
            List<BH5ProductTourMobileDTO> dtoList = new ArrayList<>();
            for (BH5ProductTourPO po : poList) {
                BH5ProductTourMobileDTO dto = new BH5ProductTourMobileDTO();
                BeanUtils.copyProperties(po, dto);
                Integer orgId = msgH5ProductMapper.selectById(po.getRelatedArticle()).getOrganId();
                String orgName = cMsgOrganMapper.selectById(orgId).getOrganName();
                dto.setOrgName(orgName);
                dto.setOpenDay(getOpenDay(po.getOpenDay()));
                dto.setCreatedName(cMsgUserMapper.selectById(po.getCreatedBy()).getUserName());
                dto.setMsgH5ProductPO(msgH5ProductMapper.selectById(po.getRelatedArticle()));
                dtoList.add(dto);
            }
            dtoList = dtoList.stream().filter(data -> data.getOrgName().equals(organName)).collect(Collectors.toList());
            if (Objects.nonNull(tagId) && tagId != 0) {
                dtoList = dtoList.stream().filter(data -> Arrays.asList(data.getMsgH5ProductPO().getTagId().split(",")).contains(tagId.toString())).collect(Collectors.toList());
            }
            resMap.put("DATA", dtoList);
        } else {
            int totalSize = bh5ProductTourMapper.selectCount(entityWrapper);
            Page<BH5ProductTourPO> page = new Page<>(currentPage, pageSize);
            List<BH5ProductTourPO> poList = bh5ProductTourMapper.selectPage(page, entityWrapper);
            List<BH5ProductTourMobileDTO> dtoList = new ArrayList<>();
            for (BH5ProductTourPO po : poList) {
                BH5ProductTourMobileDTO dto = new BH5ProductTourMobileDTO();
                BeanUtils.copyProperties(po, dto);
                Integer orgId = msgH5ProductMapper.selectById(po.getRelatedArticle()).getOrganId();
                String orgName = cMsgOrganMapper.selectById(orgId).getOrganName();
                dto.setOrgName(orgName);
                dto.setOpenDay(getOpenDay(po.getOpenDay()));
                dto.setCreatedName(cMsgUserMapper.selectById(po.getCreatedBy()).getUserName());
                dto.setMsgH5ProductPO(msgH5ProductMapper.selectById(po.getRelatedArticle()));
                dtoList.add(dto);
            }
            dtoList = dtoList.stream().filter(data -> data.getOrgName().equals(organName)).collect(Collectors.toList());
            dtoList = dtoList.stream().filter(data -> Arrays.asList(data.getMsgH5ProductPO().getTagId().split(",")).contains(tagId)).collect(Collectors.toList());
            resMap.put("DATA", dtoList);
            resMap.put("TotalSize", totalSize);
        }

        return resMap;
    }

    @Override
    public BH5ProductTourPO getTourById(Integer tourId) {
        return bh5ProductTourMapper.selectById(tourId);
    }

    @Override
    public BH5ProductTourMobileDTO getTourByIdMobile(Integer tourId) {
        BH5ProductTourMobileDTO dto = new BH5ProductTourMobileDTO();
        BH5ProductTourPO po = bh5ProductTourMapper.selectById(tourId);
        BeanUtils.copyProperties(po, dto);
        dto.setOpenDay(getOpenDay(po.getOpenDay()));
        dto.setMsgH5ProductPO(msgH5ProductMapper.selectById(po.getRelatedArticle()));
        return dto;
    }

    @Override
    public String setRecommandProduct(Integer tourId, Integer isRecommend) {

        EntityWrapper<BH5ProductTourPO> wrapper = new EntityWrapper();
        wrapper.eq("is_recommend", 1);
        int count = bh5ProductTourMapper.selectCount(wrapper);
        if (isRecommend == 1 && count >= 5) {
            return "推荐景点已达上限！";
        } else if (isRecommend == 0 && count == 1) {
            return "推荐景点数量最少为1！";
        } else {
            BH5ProductTourPO currentPo = bh5ProductTourMapper.selectById(tourId);
            currentPo.setIsRecommend(isRecommend);
            bh5ProductTourMapper.updateById(currentPo);
            return "成功更新推荐景点！";
        }

    }

    @Override
    public String downProduct(Integer tourId) {
        BH5ProductTourPO po = bh5ProductTourMapper.selectById(tourId);
        if (Objects.nonNull(po.getRelatedArticle()) && po.getRelatedArticle() != 0) {
            return "该景点已绑定文章，不可下架！";
        } else {
            po.setStatus(2);
            bh5ProductTourMapper.updateById(po);
            return "成功下架该景点！";
        }

    }

    String getOpenDay(String days) {
        List<String> weekList = Arrays.asList("周一", "周二", "周三", "周四", "周五", "周六", "周天");
        List<String> paramList = Arrays.asList(days.split(","));
        List<String> offDaysList = new ArrayList<>();
        for (String day : weekList) {
            int temp = paramList.indexOf(day);
            if (temp < 0) {
                offDaysList.add(day);
            }
        }
        String offDays = "";
        if (paramList.size() != 7) {
            offDays = StringUtils.join(offDaysList.toArray(), ",") + "休息";
        }
        String workDays = "";
        for (int i = 0; i < paramList.size(); i++) {
            String day = paramList.get(i);
            int indexWeekArray = weekList.indexOf(day);
            int indexParamArray = paramList.indexOf(day);
            int count = 0;
            try {
                if (!weekList.get(indexWeekArray + 1).equals(paramList.get(indexParamArray + 1))) {
                    workDays = workDays + day + ",";
                } else {
                    workDays = workDays + day + "至";
                    int j = 2;
                    if (j >= paramList.size()) {
                        break;
                    }
                    for (j = 2; j <= paramList.size(); j++) {
                        try {
                            if (!weekList.get(indexWeekArray + j).equals(paramList.get(indexParamArray + j))) {
                                workDays = workDays + paramList.get(indexParamArray + j) + ",";
                                count = j;
                                break;
                            }
                        } catch (Exception e) {
                            workDays = workDays + paramList.get(indexParamArray + j - 1) + ",";
                            count = j - 1;
                            break;
                        }

                    }

                }
            } catch (Exception e) {
                if (!weekList.get(indexWeekArray - 1).equals(paramList.get(indexParamArray - 1))) {
                    workDays = workDays + day + ",";
                } else {
                    workDays = workDays + day;

                }
            }

            i = indexParamArray + count;
        }
        workDays = workDays + "营业";
        StringBuilder result = new StringBuilder((workDays + offDays).replaceAll(",", "、"));
        int tempIndex = result.indexOf("营");
        if (paramList.size() == 7) {
            result.replace(tempIndex - 1, tempIndex + 2, "营业");
        } else {
            result.replace(tempIndex - 1, tempIndex + 2, "营业,");
        }
        return result.toString();
    }
}
