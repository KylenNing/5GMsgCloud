import cc.htdf.msgcloud.common.utils.DrawImageUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/10/16
 * @Description: TODO
 */


public class DrawImageTest {


    @Test
    public void drawIndexImageTest() throws IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("title1", "紫外线");
    	map.put("content1", "紫外线");
    	map.put("title2", "紫外线");
    	map.put("content2", "紫外线");
    	map.put("title3", "紫外线");
    	map.put("content3", "紫外线");
    	map.put("title4", "紫外线");
    	map.put("content4", "紫外线");
        InputStream indexPicture = DrawImageUtils.drawIndexPicture(map);

    }

    @Test
    public void drawWeatherImageTest() throws IOException {
    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("weather", "晴");
//    	map.put("daytemp", "20");
//    	map.put("nighttemp", "15");
//    	map.put("area", "天津");
//    	map.put("sunup", "06:35");
//    	map.put("sundown", "18:35");
//    	map.put("winddir", "东北风转西北风");
//    	map.put("windspeed", "8级");
        InputStream picture = DrawImageUtils.drawWeatherPicture(map);
        System.out.println(picture);


    }

    //降水
    @Test
    public void drawPrecWeatherImageTest() throws IOException {
        DrawImageUtils.drawPrecWeatherPicture();

    }

    //降雪
    @Test
    public void drawObserveWeatherPicture() throws IOException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("temp","14.5"+"°");
        paramMap.put("weather", "晴");
        paramMap.put("sunrise", "06:23");
        paramMap.put("sunset", "18:08");
        paramMap.put("maxtemp", "18.1"+"°");
        paramMap.put("mintemp", "/"+"10.6"+"°");
        paramMap.put("humdity", "60%");
        paramMap.put("wind", "西北风");
        paramMap.put("wins", "4-5级");
        //paramMap.put("wins", transformedParams[8]);
//        if(transformedParams[8].equals("微风")){
//            paramMap.put("wins", transformedParams[8]);
//        }else {
//            paramMap.put("wins", transformedParams[8]+"级");
//        }
        paramMap.put("flag","2");
        DrawImageUtils.drawObserveWeatherPicture(paramMap);

    }
}