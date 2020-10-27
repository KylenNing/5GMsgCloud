package cc.htdf.msgcloud.msgcenter.mapper;

import java.util.List;
import java.util.Set;

import cc.htdf.msgcloud.msgcenter.domain.po.BKeywordDictPO;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: ningyq
 * @Date: 2020/8/12
 * @Description: TODO
 */
@Mapper
public interface BKeywordDictMapper extends BaseMapper<BKeywordDictPO> {
	
	@Select("select * from b_keyword_dict where name != #{keyWord}")
	List<BKeywordDictPO> getNameByKeyWord(String keyWord);

	@Select("select * from b_keyword_dict")
	Set<BKeywordDictPO> getNameAllKeyWord();



}
