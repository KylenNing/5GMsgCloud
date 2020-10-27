package cc.htdf.msgcloud.message.handler;

import cc.htdf.msgcloud.common.constants.NlpParamName;
import cc.htdf.msgcloud.common.utils.DrawImageUtils;
import cc.htdf.msgcloud.common.utils.StorageUtil;
import cc.htdf.msgcloud.message.domain.po.MsgMaterialPO;
import cc.htdf.msgcloud.message.domain.po.MsgMaterialPictureLogPO;
import cc.htdf.msgcloud.message.handler.msgtemp.strategy.DynamicTagTemplateStrategy;
import cc.htdf.msgcloud.message.mapper.MsgMaterialPictureLogMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.dto.Media;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import com.google.common.collect.ImmutableMap;
import io.minio.errors.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: ningyq
 * @Date: 2020/10/19
 * @Description: TODO
 */
@Component
public class DynamicTemplateImageHelper {

    private final Map<String, String> indexParamMap = ImmutableMap.of(

            "6553b2f5313a4f618eea359904fea138", "{index_ultraviolet_rays},{index_cold},{index_morning_exercises},{index_wearing}",
            "4e8f441963e04b84b1230196c8938052", "{forecast_day_pehno},{forecast_night_pehno},{forecast_day_temp},{forecast_night_temp}," +
                    "{forecast_sunrise},{forecast_sunset},{forecast_day_wind},{forecast_night_wind},{forecast_day_wins},{forecast_night_wins}",
            "6190a4edb449413a80c0250bcf959a4e","{observe-tj-temp},{observe-tj-pehno},{observe-tj-sunrise},{observe-tj-sunset},{observe-tj-maxtemp},{observe-tj-mintemp}," +
                    "{observe-tj-humdity},{observe-tj-wind},{observe-tj-wins}"
    );

    @Resource
    private DynamicTagTemplateStrategy dynamicTagTemplateStrategy;

    @Resource
    private MsgMaterialPictureLogMapper msgMaterialPictureLogMapper;

    public MsgMaterialPO getDynamicMedia(String dynamicTemplateId, Map<String, Object> param) throws ParseException, IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {

        //Object areaObj = param.get(NlpParamName.LOCATION_CITY);
//        Object startTimeObj = param.get(NlpParamName.TIMES_START);
//        Object endTimeObj = param.get(NlpParamName.TIMES_END);
//        param = new HashMap<>();
//        param.put("{location_city}","天津市");
//        param.put("{times_start}","2020-10-19 16:00:00");
//        param.put("{times_end}","2020-10-19 17:00:00");
        param.put("dynamicTemplateId", dynamicTemplateId);
        String[] transformedParams = dynamicTagTemplateStrategy.handlerText(indexParamMap.get(dynamicTemplateId), param).split(",");
        InputStream inputStream = null;
        if(dynamicTemplateId.equals("6553b2f5313a4f618eea359904fea138")){
            inputStream = getLifeIndexImage(transformedParams);
        }else if(dynamicTemplateId.equals("4e8f441963e04b84b1230196c8938052")){
            inputStream = getWeatherDayForecastImage(transformedParams,param);
        }else {
            inputStream = getWeatherObserveImage(transformedParams,param);
        }
        MsgMaterialPO msgMaterial = uploadImageToUltra(inputStream, param);
        return msgMaterial;
    }

    private InputStream getLifeIndexImage(String[] transformedParams) throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title1", "紫外线");
        paramMap.put("title2", "感冒指数");
        paramMap.put("title3", "晨练指数");
        paramMap.put("title4", "穿衣指数");
        paramMap.put("content1", transformedParams[0]);
        paramMap.put("content2", transformedParams[1]);
        paramMap.put("content3", transformedParams[2]);
        paramMap.put("content4", transformedParams[3]);
        InputStream inputStream = DrawImageUtils.drawIndexPicture(paramMap);
        return inputStream;
    }

    private InputStream getWeatherDayForecastImage(String[] transformedParams, Map<String, Object> param) throws IOException {
        Date d = new Date();
        int currentHour = d.getHours();
        Object areaObj = param.get(NlpParamName.LOCATION_CITY);
        if (Objects.isNull(areaObj)) {
            areaObj = param.get(NlpParamName.LOCATION_COUNTY);
        }
        Map<String, Object> paramMap = new HashMap<>();
        String[] tempArray = new String[8];
        //白天温度
        tempArray[1] = transformedParams[2];
        //夜间温度
        tempArray[2] = "/"+transformedParams[3];
        //日出时间
        tempArray[3] = transformedParams[4];
        //日落时间
        tempArray[4] = transformedParams[5];

        //地区
        tempArray[7] = (String) areaObj;
        if(currentHour < 20){
            //天气
            tempArray[0] = transformedParams[0];
            //风向
            tempArray[5] = transformedParams[6];
            //风力
            tempArray[6] = transformedParams[8];
        }else {
            //天气
            tempArray[0] = transformedParams[1];
            //风向
            tempArray[5] = transformedParams[7];
            //风力
            tempArray[6] = transformedParams[9];
        }
        paramMap.put("weather", tempArray[0]);
        paramMap.put("daytemp", tempArray[1]+"°");
        paramMap.put("nighttemp", tempArray[2]+"°");
        paramMap.put("sunrise", tempArray[3]);
        paramMap.put("sunset", tempArray[4]);
        paramMap.put("wind", tempArray[5]);
        paramMap.put("wins", tempArray[6]);
        paramMap.put("area",tempArray[7]);
        paramMap.put("flag",getWeatherFlagByPhenomena(tempArray[0]));
        return DrawImageUtils.drawWeatherPicture(paramMap);
    }


    private String getWeatherFlagByPhenomena(String phenomena){
        List<String> fineList = Arrays.asList("晴");
        List<String> overcastList = Arrays.asList("阴","多云","雾","扬沙","雾霾");
        List<String> rainList = Arrays.asList("雨","阵雨","中雨","大雨","小雨","暴雨","大暴雨","特大暴雨","小到中雨",
                "中到大雨","大到暴雨","阵雨","雷阵雨");
        List<String> snowList = Arrays.asList("雪","小雪","中雪","大雪","暴雪","雨加雪","阵雪");
        String flag = "";
        Map<String,List<String>> flagMap = ImmutableMap.of("1",fineList,"2",overcastList,"3",rainList,"4",snowList);
        for(Map.Entry<String,List<String>> entry : flagMap.entrySet()){
            if(entry.getValue().contains(phenomena)){
                flag =  entry.getKey();
                break;
            }
        }
        if(Objects.isNull(flag) || flag.equals("")){
            flag = "2";
        }
        return flag;
    }

    private InputStream getWeatherObserveImage(String[] transformedParams, Map<String, Object> param) throws IOException {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("temp",transformedParams[0]+"°");
        paramMap.put("weather", transformedParams[1]);
        paramMap.put("sunrise", transformedParams[2]);
        paramMap.put("sunset", transformedParams[3]);
        paramMap.put("maxtemp", transformedParams[4]+"°");
        paramMap.put("mintemp", "/"+transformedParams[5]+"°");
        paramMap.put("humdity", transformedParams[6].substring(0,transformedParams[5].length()-2)+"%");
        paramMap.put("wind", transformedParams[7]);
        paramMap.put("wins", transformedParams[8]);
        paramMap.put("flag",getWeatherFlagByPhenomena(transformedParams[1]));
        return DrawImageUtils.drawObserveWeatherPicture(paramMap);
    }

    private MsgMaterialPO uploadImageToUltra(InputStream inputStream, Map<String, Object> param) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, InvalidBucketNameException, InsufficientDataException, RegionConflictException {
        MsgMaterialPO msgMaterialPO = new MsgMaterialPO();
        // 查询数据库看是否有一样的
        EntityWrapper<MsgMaterialPictureLogPO> wrapper = new EntityWrapper();
        wrapper.eq("CITY", param.get("{location_city}"));
        wrapper.eq("START_TIME", param.get("{times_start}"));
        wrapper.eq("END_TIME", param.get("{times_end}"));
        wrapper.eq("DYNAMIC_TEMPLATE_ID", param.get("dynamicTemplateId"));
        List<MsgMaterialPictureLogPO> lists = msgMaterialPictureLogMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(lists)) {// 无则发新的
            String MaterialName = param.get("{location_city}") + "_" + param.get("{times_start}") + "_" + param.get("{times_end}") + "_" + param.get("dynamicTemplateId") + ".png";
            // 将流复制
            ByteArrayOutputStream baos = cloneInputStream(inputStream);
            // 主图上传Minio
            String fileNameMain = StorageUtil.uploadFile("imgs", new ByteArrayInputStream(baos.toByteArray()), "png", "image/png");
            String materialLocalUrlMain = "imgs" + "/" + fileNameMain;
//            long materialLocalSizeMain = inputStream.available();
            //上传文件至远端服务器
            ResponseData responseData = Api.uploadFile(
                    new ByteArrayInputStream(baos.toByteArray()),
                    MaterialName,
                    "image/png",
                    1);
            Media media = JSONObject.parseObject(responseData.getData().toString(), Media.class);
            msgMaterialPO.setMaterialType("1");
            msgMaterialPO.setMaterialName(MaterialName);
            msgMaterialPO.setMaterialWebId(media.getId());
            msgMaterialPO.setMaterialWebUrl(media.getUrl());
            msgMaterialPO.setMaterialWebSize(media.getSize());
            msgMaterialPO.setMaterialWebSlUrl(media.getUrl());
            msgMaterialPO.setMaterialLocalUrl(materialLocalUrlMain);
            msgMaterialPO.setMaterialLocalSize(media.getSize());
            // 新数据插入数据库
            MsgMaterialPictureLogPO msgMaterialPictureLogPO = new MsgMaterialPictureLogPO();
            msgMaterialPictureLogPO.setCity((String) param.get("{location_city}"));
            msgMaterialPictureLogPO.setStartTime((String) param.get("{times_start}"));
            msgMaterialPictureLogPO.setEndTime((String) param.get("{times_end}"));
            msgMaterialPictureLogPO.setDynamicTemplateId((String) param.get("dynamicTemplateId"));
            msgMaterialPictureLogPO.setWebId(media.getId());
            msgMaterialPictureLogPO.setWebUrl(media.getUrl());
            msgMaterialPictureLogPO.setWebSize(media.getSize());
            msgMaterialPictureLogPO.setWebSlUrl(media.getUrl());
            msgMaterialPictureLogPO.setLocalUrl(materialLocalUrlMain);
            msgMaterialPictureLogPO.setLocalSize(media.getSize());
            msgMaterialPictureLogMapper.insert(msgMaterialPictureLogPO);
        } else {// 有则发老的
            MsgMaterialPictureLogPO po = lists.get(0);
            msgMaterialPO.setMaterialType("1");
            String MaterialName = po.getCity() + "_" + po.getStartTime() + "_" + po.getEndTime() + "_" + po.getDynamicTemplateId() + ".png";
            msgMaterialPO.setMaterialName(MaterialName);
            msgMaterialPO.setMaterialWebId(po.getWebId());
            msgMaterialPO.setMaterialWebUrl(po.getWebUrl());
            msgMaterialPO.setMaterialWebSize(po.getWebSize());
            msgMaterialPO.setMaterialWebSlUrl(po.getWebSlUrl());
            msgMaterialPO.setMaterialLocalUrl(po.getLocalUrl());
            msgMaterialPO.setMaterialLocalSize(po.getLocalSize());
        }
        return msgMaterialPO;
    }

    private ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
