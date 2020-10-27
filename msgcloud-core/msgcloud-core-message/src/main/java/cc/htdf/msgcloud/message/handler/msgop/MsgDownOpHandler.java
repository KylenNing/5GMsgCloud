package cc.htdf.msgcloud.message.handler.msgop;

import cc.htdf.msgcloud.common.constants.MsgOperation;
import cc.htdf.msgcloud.common.constants.MsgTemplateType;
import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.common.domain.MsgChatbot;
import cc.htdf.msgcloud.common.domain.MsgDownInfo;
import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplateButtonPO;
import cc.htdf.msgcloud.message.domain.po.MsgTemplatePO;
import cc.htdf.msgcloud.message.domain.po.SeveiceNumPO;
import cc.htdf.msgcloud.message.handler.MediaFetch;
import cc.htdf.msgcloud.message.handler.MediaFetchStrategy;
import cc.htdf.msgcloud.message.handler.MsgOpHandler;
import cc.htdf.msgcloud.message.handler.msgtemp.strategy.DynamicTagTemplateStrategy;
import cc.htdf.msgcloud.message.service.MsgMaterialService;
import cc.htdf.msgcloud.message.service.MsgMenuService;
import cc.htdf.msgcloud.message.service.MsgTemplateService;
import cc.htdf.msgcloud.message.service.SeveiceNumService;
import com.feinno.msgctenter.sdk.dto.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
@Component(MsgOperation.MSG_DOWN)
public class MsgDownOpHandler implements MsgOpHandler {

    @Resource
    private SeveiceNumService seveiceNumService;

    @Resource
    private MsgTemplateService msgTemplateService;

    @Resource
    private DynamicTagTemplateStrategy dynamicTagTemplateStrategy;

    @Resource
    private MsgMenuService msgMenuService;

    @Resource
    private MsgMaterialService msgMaterialService;

    @Resource
    private MediaFetchStrategy mediaFetchStrategy;

    @Override
    public Msg generateMsg(Msg msg) throws ParseException {
        MsgDownInfo msgDownInfo = msg.getMsgDownInfo();
        String serviceId = msgDownInfo.getServiceId();
        SeveiceNumPO seveiceNum = seveiceNumService.findByServiceId(serviceId);
        msg.setChannelBusiness(seveiceNum.getCspCode());
        MsgChatbot msgChatbot = new MsgChatbot();
        msgChatbot.setChatbotId(seveiceNum.getChatbotId());
        msg.setMsgChatbot(msgChatbot);
        Long channelId = Long.valueOf(seveiceNum.getChannelId());
        LinkedList<String> templateIds = msgDownInfo.getTemplateList();
        if (templateIds.size() == 0) {
            return null;
        }

        Map<String, MsgTemplatePO> msgTemplateMap = msgTemplateService.findByTemplateIds(templateIds);
        List<String> templateTypes = msgTemplateMap.values().stream()
                .map(template -> template.getTemplateType()).collect(Collectors.toList());

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setChannelId(channelId);
        msgInfo.setChannelType(2);
        msgInfo.setSendToAll(msgDownInfo.getSendToAll());
        msgInfo.setReceiver(msgDownInfo.getReceiver());

        Integer msgType;
        MsgBody msgBody = new MsgBody();
        if (templateTypes.contains(MsgTemplateType.TEXT)) {
            MsgTemplatePO msgTemplate = msgTemplateMap.get(templateIds.get(0));
            msgType = 3;
            String text = msgTemplate.getTemplateContent();
            msgBody.setText(text);
        } else if (templateTypes.contains(MsgTemplateType.Material)) {
            Media media = new Media();
            MsgMaterialPO msgMaterialPO = msgMaterialService.findById(msgTemplateMap.get(templateIds.get(0)).getTemplateImageId());
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
            for (String templateId : templateIds) {
                MsgTemplatePO msgTemplate = msgTemplateMap.get(templateId);
                if (Objects.isNull(msgTemplate)) {
                    continue;
                }

                Card card = new Card();
                String title = dynamicTagTemplateStrategy.handlerText(msgTemplate.getTemplateTitle(), msg.getParam());
                String newAbstract = dynamicTagTemplateStrategy.handlerText(msgTemplate.getTemplateContent(), msg.getParam());
                card.setTitle(title);
                card.setNewAbstract(newAbstract);
                card.setContent(newAbstract);

                MsgMaterialPO msgMaterialPO = msgMaterialService.findById(msgTemplate.getTemplateImageId());
                MediaFetch mediaFetch = mediaFetchStrategy.getStrategy(msg.getChannelBusiness());
                Media media = mediaFetch.fetchMedia(msgMaterialPO);
                card.setMedia(media);

                // 卡片建议返回
                List<MsgTemplateButtonPO> templateButtonList = msgMenuService.findTemplateButtonsByTemplateId(msgTemplate.getId());
                if (!Objects.isNull(templateButtonList) && !templateButtonList.isEmpty()) {
                    List<SuggestItem> suggestItems = new ArrayList<>();
                    for (MsgTemplateButtonPO templateButton : templateButtonList) {
                        if (Objects.isNull(templateButton) ||
                                Objects.isNull(templateButton.getButtonName()) ||
                                Objects.isNull(templateButton.getButtonType())) {
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
                cards.add(card);

            }
            msgBody.setCard(cards);
        }

        // 悬浮菜单返回
        LinkedList<Map<String, Object>> tempList = msgDownInfo.getMenuList();
        LinkedList<SuggestItem> suggestItems = new LinkedList<>();
        if (!Objects.isNull(tempList) && !tempList.isEmpty()) {
            for (Map<String, Object> suspensionMenu : tempList) {
                SuggestItem suggestItem = new SuggestItem();
                suggestItem.setButtonName(
                        Optional.ofNullable(suspensionMenu.get("menuName"))
                                .map(String::valueOf).orElse("")
                );
                int type = Integer.valueOf((String) suspensionMenu.get("menuType"));
                if (type == 0) {
                    suggestItem.setButtonType(type + 1);
                } else {
                    suggestItem.setButtonType(type);
                }
//                suggestItem.setButtonType(
//                        Optional.ofNullable(suspensionMenu.get("menuType"))
//                                .map(String::valueOf).map(Integer::valueOf).map(i -> i + 1)
//                                .orElse(2)
//                );
                suggestItem.setValue(
                        Optional.ofNullable(suspensionMenu.get("menuContent"))
                                .map(String::valueOf).orElse("")
                );
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
