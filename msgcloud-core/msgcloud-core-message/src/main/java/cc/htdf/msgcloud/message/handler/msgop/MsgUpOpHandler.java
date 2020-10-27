package cc.htdf.msgcloud.message.handler.msgop;

import cc.htdf.msgcloud.common.constants.MsgOperation;
import cc.htdf.msgcloud.common.constants.QueueName;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.exceptions.BusinessException;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.message.domain.po.*;
import cc.htdf.msgcloud.message.handler.DynamicTemplateImageHelper;
import cc.htdf.msgcloud.message.handler.MediaFetch;
import cc.htdf.msgcloud.message.handler.MediaFetchStrategy;
import cc.htdf.msgcloud.message.handler.MsgOpHandler;
import cc.htdf.msgcloud.message.handler.msgtemp.strategy.DynamicTagTemplateStrategy;
import cc.htdf.msgcloud.message.service.MsgMaterialService;
import cc.htdf.msgcloud.message.service.MsgMenuService;
import cc.htdf.msgcloud.message.service.MsgTemplateService;
import com.feinno.msgctenter.sdk.dto.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 * <p>
 * 上行消息处理策略
 */
@Slf4j
@Component(MsgOperation.MSG_UP)
public class MsgUpOpHandler implements MsgOpHandler {

    @Resource
    private MsgTemplateService msgTemplateService;

    @Resource
    private DynamicTagTemplateStrategy dynamicTagTemplateStrategy;

    @Resource
    private MsgMenuService msgMenuService;

    @Resource
    private MediaFetchStrategy mediaFetchStrategy;

    @Resource
    private MsgMaterialService msgMaterialService;

    @Resource
    private DynamicTemplateImageHelper dynamicTemplateImageHelper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Msg generateMsg(Msg msg) throws ParseException, IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {

        String action = msg.getAction();
        MsgUpInfo msgUpInfo = msg.getMsgUpInfo();
        Long channelId = msgUpInfo.getChannelId();

        BMsgKeywordPO bMsgKeywordPO = msgTemplateService.findGroupByChannelIdAndAction(channelId, action);
        LinkedList<MsgTemplatePO> msgTemplateList;

        if (bMsgKeywordPO != null && bMsgKeywordPO.getGroupId() != null) {//走卡片组的逻辑
            msgTemplateList = msgTemplateService.findByGroupId(bMsgKeywordPO.getGroupId());
        } else {//如果返回的组ID为空,走正常卡片的逻辑
            msgTemplateList = msgTemplateService.findByChannelIdAndAction(channelId, action);
        }
        if (msgTemplateList.isEmpty() || msgTemplateList.size() == 0) {
            return null;
        }

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setChannelId(msgUpInfo.getChannelId());
        msgInfo.setMessageId(msgUpInfo.getMsgId());
        msgInfo.setChannelType(2);
        msgInfo.setSendToAll(0);

        Receiver receiver = new Receiver();
        receiver.setUserIdType(1);
        receiver.setToUserList(Arrays.asList(msgUpInfo.getDestination()));
        msgInfo.setReceiver(receiver);

        Integer msgType;
        MsgBody msgBody = new MsgBody();

        String templateType = msgTemplateList.get(0).getTemplateType();
        if ("2".equals(templateType)) {// 文本
            msgType = 3;
            String text = msgTemplateList.get(0).getTemplateContent();
            msgBody.setText(text);
        } else if ("3".equals(templateType)) {// 图片，视频，音频
            Media media = new Media();
            MsgMaterialPO msgMaterialPO = msgMaterialService.findById(msgTemplateList.get(0).getTemplateImageId());
            if ("1".equals(msgMaterialPO.getMaterialType())) {// 图片
                msgType = 4;
                // 素材id，调用文件上传接口时返回的素材id
                media.setId(msgMaterialPO.getMaterialWebId());
                // 文件下载路径
                media.setUrl(msgMaterialPO.getMaterialWebUrl());
                // 缩略图url
                media.setThumbUrl(msgMaterialPO.getMaterialWebSlUrl());
                // 文件名称
                media.setName(msgMaterialPO.getMaterialName());
                // 文件大小
                media.setSize(msgMaterialPO.getMaterialLocalSize());
                // 文件类型
                media.setType(msgType);
                msgBody.setImage(media);
            } else if ("2".equals(msgMaterialPO.getMaterialType())) {// 视频
                msgType = 6;
                // 素材id，调用文件上传接口时返回的素材id
                media.setId(msgMaterialPO.getMaterialWebId());
                // 文件下载路径
                media.setUrl(msgMaterialPO.getMaterialWebUrl());
                // 文件名称
                media.setName(msgMaterialPO.getMaterialName());
                // 文件大小
                media.setSize(msgMaterialPO.getMaterialLocalSize());
                // 文件类型
                media.setType(msgType);
                msgBody.setVideo(media);
            } else {// 音频
                msgType = 5;
                // 素材id，调用文件上传接口时返回的素材id
                media.setId(msgMaterialPO.getMaterialWebId());
                // 文件下载路径
                media.setUrl(msgMaterialPO.getMaterialWebUrl());
                // 文件名称
                media.setName(msgMaterialPO.getMaterialName());
                // 文件大小
                media.setSize(msgMaterialPO.getMaterialLocalSize());
                // 文件类型
                media.setType(msgType);
                msgBody.setAudio(media);
            }
        } else {
            msgType = 7;
            LinkedList<Card> cards = new LinkedList<>();
            for (MsgTemplatePO msgTemplatePO : msgTemplateList) {
                Card card = new Card();
                String title = dynamicTagTemplateStrategy.handlerText(msgTemplatePO.getTemplateTitle(), msg.getParam());
                String newAbstract = dynamicTagTemplateStrategy.handlerText(msgTemplatePO.getTemplateContent(), msg.getParam());
                card.setTitle(title);
                card.setNewAbstract(newAbstract);
                card.setContent(newAbstract);

                // 卡片建议返回
                List<MsgTemplateButtonPO> templateButtonList = msgMenuService.findTemplateButtonsByTemplateId(msgTemplatePO.getId());
                if (!Objects.isNull(templateButtonList) && !templateButtonList.isEmpty()) {
                    List<SuggestItem> suggestItems = new ArrayList<>();
                    for (MsgTemplateButtonPO templateButton : templateButtonList) {

                        if (Objects.isNull(templateButton) ||
                                Objects.isNull(templateButton.getButtonName()) ||
                                Objects.isNull(templateButton.getButtonType())
                        ) {
                            continue;
                        }

                        SuggestItem suggestItem = new SuggestItem();
                        suggestItem.setButtonName(templateButton.getButtonName());
                        suggestItem.setValue(templateButton.getButtonContent());
                        Integer type = Integer.valueOf(templateButton.getButtonType());
                        if (type == 0) {
                            suggestItem.setButtonType(type + 1);
                        } else {
                            suggestItem.setButtonType(type);
                        }
                        suggestItems.add(suggestItem);
                    }
                    card.setSuggestItemList(suggestItems);
                }

                if (Objects.isNull(msgTemplatePO.getTemplateImageId())) {
                    throw new BusinessException(ExceptionCode.ERROR, "模版[{}]未配置媒体！", msgTemplatePO.getId());
                }
                Media media = null;
                String dynamicTemplateId = msgTemplatePO.getDynamicTemplateId();
                MediaFetch mediaFetch = mediaFetchStrategy.getStrategy(msg.getChannelBusiness());
                if(Objects.isNull(msgTemplatePO.getDynamicTemplateId()) || msgTemplatePO.getDynamicTemplateId().equals("")){
                    MsgMaterialPO msgMaterialPO = msgMaterialService.findById(msgTemplatePO.getTemplateImageId());
                    media = mediaFetch.fetchMedia(msgMaterialPO);
                }else {
                    try{
                        media = mediaFetch.fetchMedia(dynamicTemplateImageHelper.getDynamicMedia(dynamicTemplateId,msg.getParam()));
                    }catch (Exception e){
                        log.info("未获取到相关天气数据");
                        msg.setAction("未匹配到真实意图");
                        rocketMQTemplate.convertAndSend(QueueName.MSGCLOUD_MESSAGE,msg);
                        return null;
                    }
                }
                card.setMedia(media);
                cards.add(card);
            }
            msgBody.setCard(cards);
        }
        // 悬浮菜单返回
        List<BMsgMenuPO> suspensionMenusList;
        if (bMsgKeywordPO != null && bMsgKeywordPO.getGroupId() != null) {//走卡片组的逻辑
            suspensionMenusList = msgMenuService.findSuspensionMenusByGroupId(bMsgKeywordPO.getGroupId());
        } else {//反之,如果返回的组ID为空,走正常卡片的逻辑
            suspensionMenusList = msgMenuService.findSuspensionMenusByKeyword(action, msg.getMsgChatbot().getServiceId());
        }

        if (!Objects.isNull(suspensionMenusList) && !suspensionMenusList.isEmpty()) {
            List<SuggestItem> suggestItems = new ArrayList<>();
            for (BMsgMenuPO msgMenuPO : suspensionMenusList) {
                if (Objects.isNull(msgMenuPO)) {
                    continue;
                }
                SuggestItem suggestItem = new SuggestItem();
                suggestItem.setButtonName(msgMenuPO.getMenuName());
                suggestItem.setValue(msgMenuPO.getMenuContent());
                Integer type = Integer.valueOf(msgMenuPO.getMenuType());
                if (type == 0) {
                    suggestItem.setButtonType(type + 1);
                } else {
                    suggestItem.setButtonType(type);
                }
                suggestItems.add(suggestItem);
            }
            msgBody.setBottomButtons(suggestItems);
        }
        msgInfo.setMsgType(msgType);
        msgInfo.setMsg(msgBody);
        msg.setMsgInfo(msgInfo);
        return msg;
    }

}
