package cc.htdf.msgcloud.msgcenter;

import cc.htdf.msgcloud.common.utils.JWTUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * author: JT
 * date: 2020/8/18
 * title:
 */
@Slf4j
public class ApiUploadTest {

    @Test
    public void uploadTest() {
        try {
//            InputStream inputStream= new FileInputStream(new File("D:\\下载\\5g.jpg"));
//            InputStream thumbImageInputStream= new FileInputStream(new File("D:\\下载\\a.png"));
//            ResponseData responseData= Api.uploadFileAndThumbImage(inputStream,thumbImageInputStream,"image",1);
//            System.out.println(JSON.toJSONString(responseData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imageTest() throws IOException {
        try {
//            InputStream inputStream= new FileInputStream(new File("D:\\data\\dataset\\a.png"));
//            InputStream thumbImageInputStream= new FileInputStream(new File("D:\\data\\dataset\\a_out.png"));
//            ResponseData responseData= Api.uploadFileAndThumbImage(inputStream, thumbImageInputStream,"image",1);
//            System.out.println(JSON.toJSONString(responseData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            log.error("提示:",e);
        }
        return null;
    }


    @Test
    public void jwtTest() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTc5OTIzOTMsInVzZXJuYW1lIjoiMTIzNDU2In0.Rb1PA8zhZlKatnZ0_2RhgBRQgP4TmPu5z8CNLBOcX7s";
        String username = JWTUtil.getUsername(token);
    }
}
