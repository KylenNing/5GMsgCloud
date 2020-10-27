package cc.htdf.msgcloud.msgcenter.service.impl;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordDictPO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.BKeywordDictVO;
import cc.htdf.msgcloud.msgcenter.mapper.BKeywordDictMapper;
import cc.htdf.msgcloud.msgcenter.mapper.CMsgOrganMapper;
import cc.htdf.msgcloud.msgcenter.service.KeywordDictService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: ningyq
 * @Date: 2020/10/7
 * @Description: TODO
 */
@Service
public class KeywordDictServiceImpl implements KeywordDictService {

    @Resource
    private BKeywordDictMapper bKeywordDictMapper;

    @Resource
    private CMsgOrganMapper cMsgOrganMapper;

    @Override
    public String createKeywordDict(CMsgUserPO user, BKeywordDictPO dict) {
        EntityWrapper<BKeywordDictPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        entityWrapper.eq("name",dict.getName());
        int count = bKeywordDictMapper.selectCount(entityWrapper);
        if(count == 0){
            int createdBy = user.getId();
            int createdOrg = user.getOrganId();
            dict.setCreatedBy(createdBy);
            dict.setCreatedOrg(createdOrg);
            dict.setCreatedTime(new Date());
            bKeywordDictMapper.insert(dict);
            return "成功添加数据至关键词码表！";
        }else {
            return "该关键词名称已经存在，请更换名称！";
        }

    }

    @Override
    public void deleteKeywordDict(Integer dictId) {
        EntityWrapper<BKeywordDictPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id",dictId);
        bKeywordDictMapper.delete(entityWrapper);
    }

    @Override
    public String updateKeywordDict(CMsgUserPO user,BKeywordDictPO dict) {
        BKeywordDictPO po = bKeywordDictMapper.selectById(dict.getId());
        EntityWrapper<BKeywordDictPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        entityWrapper.eq("name",dict.getName());
        int count = bKeywordDictMapper.selectCount(entityWrapper);
        if(count == 0 || dict.getName().equals(po.getName())){
            dict.setUpdatedTime(new Date());
            bKeywordDictMapper.updateById(dict);
            return "成功更新数据至关键词码表！";
        }else {
            return "该关键词名称已经存在，请更换名称！";
        }

    }

    @Override
    public Map getAllKeyWordDict(CMsgUserPO user, Integer currentPage, Integer pageSize, String dictName) {
        EntityWrapper<BKeywordDictPO> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("created_org", user.getOrgs());
        if(Objects.nonNull(dictName) && !dictName.equals("")){
            entityWrapper.like("name",dictName);
        }
        Map resMap = new HashMap();
        if (Objects.isNull(currentPage) && Objects.isNull(pageSize)) {
            List<BKeywordDictPO> poList =  bKeywordDictMapper.selectList(entityWrapper);
            List<BKeywordDictVO> voList = new ArrayList<>();
            for(BKeywordDictPO po : poList){
                BKeywordDictVO vo = new BKeywordDictVO();
                BeanUtils.copyProperties(po,vo);
                String orgName = cMsgOrganMapper.selectById(user.getOrganId()).getOrganName();
                vo.setDictContent(orgName);
                voList.add(vo);
            }
            resMap.put("DATA", voList);
        } else {
            int totalSize = bKeywordDictMapper.selectCount(entityWrapper);
            Page<BKeywordDictPO> page = new Page<>(currentPage, pageSize);
            List<BKeywordDictPO> poList = bKeywordDictMapper.selectPage(page, entityWrapper);
            List<BKeywordDictVO> voList = new ArrayList<>();
            for(BKeywordDictPO po : poList){
                BKeywordDictVO vo = new BKeywordDictVO();
                BeanUtils.copyProperties(po,vo);
                String orgName = cMsgOrganMapper.selectById(user.getOrganId()).getOrganName();
                vo.setDictContent(orgName);
                voList.add(vo);
            }
            resMap.put("DATA", voList);
            resMap.put("TotalSize", totalSize);
        }

        return resMap;
    }
}