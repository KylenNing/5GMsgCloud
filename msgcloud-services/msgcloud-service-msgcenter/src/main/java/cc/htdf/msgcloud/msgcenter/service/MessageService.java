package cc.htdf.msgcloud.msgcenter.service;

import cc.htdf.msgcloud.msgcenter.annotation.LoginUser;
import cc.htdf.msgcloud.msgcenter.domain.dto.MessagePageDTO;
import cc.htdf.msgcloud.msgcenter.domain.po.CMsgUserPO;
import cc.htdf.msgcloud.msgcenter.domain.vo.MsgMessageVO;

import java.text.ParseException;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/8/14
 * @Description: TODO
 */
public interface MessageService {

   void insertNewMessageToDatabase(CMsgUserPO user,MsgMessageVO messageVO) throws InterruptedException;

   void sendNewMessageToQueue(CMsgUserPO user,MsgMessageVO messageVO, Integer messageId) throws InterruptedException;

   Map getAllMessage(@LoginUser CMsgUserPO user, Integer msgStatus, String serviceId, Integer currentPage, Integer pageSize, String startTime, String endTime) throws ParseException;

   MessagePageDTO getMessageRelatedInfoById(Integer msgId);
}
