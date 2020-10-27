package cc.htdf.msgcloud.msgcenter.mapper;


import cc.htdf.msgcloud.msgcenter.domain.po.SeveiceNumPO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: renxh
 * @Date: 2020/8/12
 * @Description:
 */
@Mapper
public interface SeveiceNumMapper extends BaseMapper<SeveiceNumPO> {

    @Update("update b_seveice_num " +
            "set CHATBOT_ID = #{po.chatbotId}, CHANNEL_ID = #{po.channelId}," +
            "CHATBOT_NAME = #{po.chatbotName}, APP_ID = #{po.appId}," +
            "APP_SECRET = #{po.appSecret}, CSP_CODE = #{po.cspCode}," +
            "LOGO_URL = #{po.logoUrl}, CREATED_ORG = #{po.createdOrg}, CREATED_BY = #{po.createdBy}," +
            "CREATED_TIME = #{po.createdTime}, UPDATED_BY = #{po.updatedBy}," +
            "UPDATED_TIME = #{po.updatedTime}" +
            " where ID = #{po.id}")
    void updateseveiceNumById(@Param("po") SeveiceNumPO po);

    @Select("SELECT " +
            "ID,CHATBOT_ID,CHANNEL_ID,CHATBOT_NAME,APP_ID,APP_SECRET,CSP_CODE,CREATED_ORG," +
            "CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME " +
            "FROM b_seveice_num WHERE CHANNEL_ID = #{channelId}")
    SeveiceNumPO findByChannleId(@Param("channelId") Long channelId);

    @Select("SELECT " +
            "ID,CHATBOT_ID,CHANNEL_ID,CHATBOT_NAME,APP_ID,APP_SECRET,CSP_CODE,CREATED_ORG," +
            "CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME " +
            "FROM b_seveice_num WHERE CHATBOT_ID = #{chatbotId}")
    SeveiceNumPO findByChatbotId(@Param("chatbotId") String chatbotId);


    @Insert("insert into b_seveice_num(CHATBOT_ID,CHANNEL_ID,CHATBOT_NAME,APP_ID,APP_SECRET,CSP_CODE,LOGO_URL,CREATED_ORG,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME) " +
            "values (#{po.chatbotId},#{po.channelId},#{po.chatbotName},#{po.appId},#{po.appSecret},#{po.cspCode},#{po.logoUrl},#{po.createdOrg},#{po.createdBy},#{po.createdTime},#{po.updatedBy},#{po.updatedTime})")
    void insertSeveiceNum(@Param("po") SeveiceNumPO po);

}
