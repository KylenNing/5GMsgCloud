package cc.htdf.msgcloud.common.utils;

import java.security.MessageDigest;

/**
 * @program: 消息中台开发者接口签名生成工具类
 * @description
 * @author: LuoHongYu
 * @create: 2019-12-27 13:44
 **/
public class SignUtil {
     private static long lastRequstTime=0;

    // 签名重新生成时长，默认时间为10分钟，ms
    private static final int SIGN_EXPIRED_TIME =10 * 60 * 1000;

    private static final String MD5_ALGORITHM_NAME = "MD5";

    /**
     *  生成签名token
     * @param developerId 开发者账号
     * @param secret  开发者账号密钥
     * @return
     */
    public static String createSign(String developerId,String secret){
        if(System.currentTimeMillis() - lastRequstTime > SIGN_EXPIRED_TIME){
            lastRequstTime=System.currentTimeMillis();
        }
        StringBuilder temp = new StringBuilder();
        temp.append(developerId).append("&").append(lastRequstTime).append("&").append(secret);
        String sign = getMD5(temp.toString()).toUpperCase();
        return developerId+"_"+lastRequstTime+"_"+sign;
    }


    public static String getMD5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
            byte[] md5Bytes = md5.digest(str.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            str = hexValue.toString();
            return str;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
