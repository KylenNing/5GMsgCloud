package cc.htdf.msgcloud.common.utils;

import cc.htdf.msgcloud.common.constants.JWTConstant;
import cc.htdf.msgcloud.common.exceptions.ExceptionCode;
import cc.htdf.msgcloud.common.exceptions.SystemException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;


/**
 * Created by JT on 2018/5/30
 */
public class JWTUtil {

    // 过期时间24小时
    private static final long EXPIRE_TIME = 24*60*60*1000;

    /**
     * 生成签名
     * @param username
     * @param securt
     * @return
     */
    public static String sign(String username, String securt) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            Algorithm algorithm = Algorithm.HMAC256(securt);
            return JWT.create()
                    .withClaim(JWTConstant.JWT_KEY_USERNAME, username)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(ExceptionCode.JWT, String.format("用户[%s]签名失败，请重新签名！[异常:%s]", username, e.getMessage()));
        }
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(JWTConstant.JWT_KEY_USERNAME, username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            throw new SystemException(ExceptionCode.JWT, String.format("用户[%s]签名验证失败! [异常: %s]", username, exception.getMessage()));
        }
    }


    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(JWTConstant.JWT_KEY_USERNAME).asString();
    }


    /**
     * 生成签名
     * @param params
     * @param securt
     * @return
     */
    public static String sign(Map<String, String> params, String securt) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        try {
            Algorithm algorithm = Algorithm.HMAC256(securt);
            JWTCreator.Builder jwtBuilder = JWT.create();
            for (String key : params.keySet()) {
                jwtBuilder.withClaim(key, params.get(key));
            }
            return jwtBuilder
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new SystemException(ExceptionCode.JWT, String.format("签名失败，请重新签名！[异常: %s]", e.getMessage()));
        }
    }

}
