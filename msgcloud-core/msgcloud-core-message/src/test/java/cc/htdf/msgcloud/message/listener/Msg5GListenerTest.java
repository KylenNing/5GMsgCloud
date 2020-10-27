package cc.htdf.msgcloud.message.listener;

import cc.htdf.msgcloud.common.domain.Msg;
import cc.htdf.msgcloud.message.BaseTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feinno.msgctenter.sdk.Api;
import com.feinno.msgctenter.sdk.respon.ResponseData;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * author: JT
 * date: 2020/8/15
 * title:
 */
public class Msg5GListenerTest extends BaseTest {

    @Resource
    private Msg5GListener msg5GListener;

    @Test
    public void executeTest() {

        String json = "{\"channelBusiness\":\"ultra\",\"operation\":\"msgdown\",\"type\":\"immediateDown\",\"status\":1,\"action\":null,\"svip\":null,\"param\":null,\"msgUpInfo\":null,\"msgDownInfo\":{\"serviceId\":\"182\",\"messageId\":120,\"templateList\":[\"29a17c21453347ddb0c501957e8e40a0\"],\"menuList\":[],\"sendToAll\":0,\"receiver\":{\"userIdType\":1,\"toPartyList\":null,\"toTagList\":null,\"toUserList\":[\"15715793007\",\"18018939638\",\"1146890877\",\"17857992797\"],\"fileUrl\":null}},\"msgInfo\":null}";
        Msg msg = JSONObject.parseObject(json, Msg.class);
        msg5GListener.onMessage(msg);

    }

    @Test
    public void imgUpTest() {
//        try {
////            InputStream inputStream= new FileInputStream(ResourceUtils.getFile("classpath:5g.jpg"));
//            InputStream inputStream= new FileInputStream(new File("D:\\下载\\a.png"));
//            // InputStream inputStream=HttpKit.getInputStream("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1593105220406&di=dff0795bfa1b30d95e1737a099f5d2f7&imgtype=0&src=http%3A%2F%2Fimg.17ok.com%2Fadmin%2Fmedia%2Fimages%2F20180615-145950.jpg%3F9");
//            ResponseData responseData= Api.uploadFile(inputStream,"image",1);
//            System.out.println(JSON.toJSONString(responseData));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
