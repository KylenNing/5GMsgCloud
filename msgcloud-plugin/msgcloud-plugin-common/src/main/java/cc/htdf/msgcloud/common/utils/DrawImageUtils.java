package cc.htdf.msgcloud.common.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

/**
 * @Author: ningyq
 * @Date: 2020/10/15
 * @Description: TODO
 */
public class DrawImageUtils {

    
    //天气实况
    public static InputStream drawWeatherPicture(Map<String, Object> map) throws IOException {
//    	map.put("weather", "多云");
//    	map.put("daytemp", "20.0°");
//    	map.put("nighttemp", "/15.0°");
//    	map.put("area", "天津市");
//    	map.put("sunrise", "06:35");
//    	map.put("sunset", "18:35");
//    	map.put("wind", "东北风");
//    	map.put("wins", "7-8级");
//        map.put("flag", "2");
    	String weather =(String) map.get("weather");
    	String daytemp =(String) map.get("daytemp");
    	String nighttemp =(String) map.get("nighttemp");
    	String area =(String) map.get("area");
    	String sunup =(String) map.get("sunrise");
    	String sundown =(String) map.get("sunset");
    	String winddir =(String) map.get("wind");
    	String windspeed =(String) map.get("wins");
    	String flag = (String) map.get("flag");

        String backPicName = "weather"+flag+".png";
        String iconPicName = "icon"+flag+".png";
        Resource resource1 = new ClassPathResource("classpath:imgs/"+ backPicName);
        InputStream is1 = resource1.getStream();
        BufferedImage image = ImageIO.read(is1);

        Resource resource2 = new ClassPathResource("classpath:imgs/"+ iconPicName);
        InputStream is2 = resource2.getStream();
        BufferedImage imageIcon = ImageIO.read(is2);
//        String bgpPath = "/Users/kylen/Desktop/gra/png/weather"+flag+".png";
//        File file = new File(bgpPath);
//        FileInputStream fileInputStream = new FileInputStream(file);
//        BufferedImage image = ImageIO.read(fileInputStream);
//
//        String iconPath = "/Users/kylen/Desktop/gra/png/icon"+flag+".png";
//        File fileIcon = new File(iconPath);
//        FileInputStream fileInputStreamIcon = new FileInputStream(fileIcon);
//        BufferedImage imageIcon = ImageIO.read(fileInputStreamIcon);
        int iconWidth = imageIcon.getWidth();
        int iconHeight = imageIcon.getHeight();
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //设置文字效果平滑
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
        g2.fillRect(0, 0, width, height);
        //设置颜色
        g2.setColor(Color.WHITE);
        Color subTitleColor = new Color(252,253,167);
        //设置背景图片
        g2.drawImage(image, 0, 0, width, height,null);
        //设置天气icon
        g2.drawImage(imageIcon, 210, 279, 105, 105,null);
        g2.drawRect(0, 0, width - 1, height - 1);
        //向图片上写字符串
        g2.setFont(new Font("微软雅黑", Font.BOLD, 80));
        g2.drawString(weather, 50, 360);

        g2.setFont(new Font("微软雅黑", Font.BOLD, 190));
        g2.drawString(daytemp, 50, 560);

//        g2.setFont(new Font("微软雅黑", Font.BOLD, 50));
//        g2.drawString("O", 275, 450);

        g2.setFont(new Font("微软雅黑", Font.BOLD, 65));
        g2.drawString(nighttemp, 540, 555);

        g2.setFont(new Font("微软雅黑", Font.BOLD, 70));
        g2.drawString(area, 1015, 560);

//        g2.setFont(new Font("微软雅黑", Font.BOLD, 20));
//        g2.drawString("O", 430, 530);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
        g2.drawString(sunup, 110, 677);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
        g2.drawString(sundown, 413, 677);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
        g2.drawString(winddir, 735, 677);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
        g2.drawString(windspeed, 1075, 677);

//        String savePath = "/Users/kylen/Desktop/gra/tw.png";
//        ImageIO.write(bi, "PNG", new FileOutputStream(savePath));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream input = null;
        try {
            ImageIO.write(bi, "png", os);
            input = new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public static InputStream drawIndexPicture(Map<String, Object> map) throws IOException {
    	//紫外线
    	String title1=(String)map.get("title1");
    	//强度
    	String content1=(String)map.get("content1");
    	//感冒指数
    	String title2=(String)map.get("title2");
    	//易发
    	String content2=(String)map.get("content2");
    	//chenlian
    	String title3=(String)map.get("title3");
    	//适宜
    	String content3=(String)map.get("content3");
    	//穿衣指数
    	String title4=(String)map.get("title4");
    	//舒适
    	String content4=(String)map.get("content4");
    	
        String picName = "index.png";
//        String filePath = ResourceUtils.getFile("classpath:imgs/"+ picName).getPath();
//        File file = new File(filePath);
        Resource resource = new ClassPathResource("classpath:imgs/"+ picName);
        InputStream is = resource.getStream();
        //FileInputStream fileInputStream = (FileInputStream) (is);
        BufferedImage image = ImageIO.read(is);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
        g2.fillRect(0, 0, width, height);
        //设置颜色
        g2.setColor(Color.WHITE);
        Color subTitleColor = new Color(252,253,167);
        g2.drawImage(image, 0, 0, width, height,null);
        //g2.drawImage(image1.getScaledInstance(640, 360, Image.SCALE_SMOOTH), 0, 0,null);
        g2.drawRect(0, 0, width - 1, height - 1);
        //向图片上写字符串
        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 25));
        g2.drawString(title1, 117, 75);
        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 15));
        g2.setColor(subTitleColor);
        g2.drawString(content1, 117, 100);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(title2, 372, 75);
        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 15));
        g2.setColor(subTitleColor);
        g2.drawString(content2, 372, 100);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(title3, 117, 199);
        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 15));
        g2.setColor(subTitleColor);
        g2.drawString(content3, 117, 224);

        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(title4, 372, 199);
        g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 15));
        g2.setColor(subTitleColor);
        g2.drawString(content4, 372, 224);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream input = null;
        try {
            ImageIO.write(bi, "png", os);
            input = new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;

    }
    
	
  //降水
      public static void drawPrecWeatherPicture() throws IOException {

          String bgpPath = "/D:/img/prec.png";
          File file = new File(bgpPath);
          FileInputStream fileInputStream = new FileInputStream(file);
          BufferedImage image = ImageIO.read(fileInputStream);
          int width = image.getWidth();
          int height = image.getHeight();
          BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
          Graphics2D g2 = (Graphics2D) bi.getGraphics();
          //设置文字效果平滑
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
          g2.fillRect(0, 0, width, height);
          //设置颜色
          g2.setColor(Color.WHITE);
          Color subTitleColor = new Color(252,253,167);
          g2.drawImage(image, 0, 0, width, height,null);
          //g2.drawImage(image1.getScaledInstance(640, 360, Image.SCALE_SMOOTH), 0, 0,null);
          g2.drawRect(0, 0, width - 1, height - 1);
          //向图片上写字符串
          g2.setFont(new Font("微软雅黑", Font.BOLD, 70));
          g2.drawString("10", 40, 565);
          
          
          g2.setFont(new Font("微软雅黑", Font.BOLD, 70));
          g2.drawString("18", 160, 565);
          
          g2.setFont(new Font("微软雅黑", Font.BOLD, 95));
          g2.drawString("大雨", 305, 555);
          
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
          g2.drawString("1.3", 215, 662);
         
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
          g2.drawString("3.3", 380, 662);
          
          String savePath = "D:/img/precweather.png";
          ImageIO.write(bi, "PNG", new FileOutputStream(savePath));
          System.out.println(savePath);

      }
      
      //天津实况天气图片生成
      public static InputStream drawObserveWeatherPicture(Map<String,Object> param) throws IOException {

          String temp =(String) param.get("temp");
          String weather =(String) param.get("weather");
          String sunrise =(String) param.get("sunrise");
          String sunset =(String) param.get("sunset");
          String maxTemp =(String) param.get("maxtemp");
          String minTemp =(String) param.get("mintemp");
          String humdity =(String) param.get("humdity");
          String wind =(String) param.get("wind");
          String wins =(String) param.get("wins");
          String flag = (String) param.get("flag");
//          String bgpPath = "/Users/kylen/Desktop/gra/png/天津实况天气图片/observe"+flag+".png";
//          File file = new File(bgpPath);
//          FileInputStream fileInputStream = new FileInputStream(file);
//          BufferedImage image = ImageIO.read(fileInputStream);
          String backPicName = "observe"+flag+".png";
          Resource resource1 = new ClassPathResource("classpath:imgs/"+ backPicName);
          InputStream is1 = resource1.getStream();
          BufferedImage image = ImageIO.read(is1);
          int width = image.getWidth();
          int height = image.getHeight();
          BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
          Graphics2D g2 = (Graphics2D) bi.getGraphics();
          //设置文字效果平滑
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);
          g2.fillRect(0, 0, width, height);
          //设置颜色
          g2.setColor(Color.WHITE);
          Color subTitleColor = new Color(252,253,167);
          g2.drawImage(image, 0, 0, width, height,null);
          //g2.drawImage(image1.getScaledInstance(640, 360, Image.SCALE_SMOOTH), 0, 0,null);
          g2.drawRect(0, 0, width - 1, height - 1);
          //向图片上写字符串
          g2.setFont(new Font("微软雅黑", Font.BOLD, 185));
          g2.drawString(temp, 40, 450);
          g2.setFont(new Font("微软雅黑", Font.BOLD, 75));
          g2.setFont(new Font("微软雅黑", Font.BOLD, 50));
          g2.drawString(weather, 530, 450);
          
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(sunrise, 110, 570);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(sunset, 330, 570);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(maxTemp, 95, 670);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(minTemp, 205, 670);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 50));
          g2.drawString(humdity, 465, 670);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(wind, 730, 670);
          g2.setFont(new Font("微软雅黑", Font.TRUETYPE_FONT, 45));
          g2.drawString(wins, 1022, 670);
          
//          String savePath = "/Users/kylen/Desktop/gra/png/天津实况天气图片/observeTest.png";
//          ImageIO.write(bi, "PNG", new FileOutputStream(savePath));
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          InputStream input = null;
          try {
              ImageIO.write(bi, "png", os);
              input = new ByteArrayInputStream(os.toByteArray());
          } catch (IOException e) {
              e.printStackTrace();
          }
          return input;

      }
}