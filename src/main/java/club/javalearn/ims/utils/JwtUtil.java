package club.javalearn.ims.utils;

import club.javalearn.ims.properties.ShiroProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author king-pan
 * Date: 2018/9/10
 * Time: 下午3:10
 * Description: Json Web Token 工具类
 */
@Slf4j
public class JwtUtil {

    /**
     * 过期时间 30分钟
     */
    private static final long EXPIRE_TIME = 1 * 60 * 1000;


    private static final String USER_NAME = "userName";

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确：验证通过没有返回值，没有异常，验证失败抛出异常
     */
    public static boolean verify(String token, String userName, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(USER_NAME, userName)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_NAME).asString();
        } catch (JWTDecodeException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 生成签名,30分钟后过期
     *
     * @param userName 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String userName, String secret) {
        try {
            ShiroProperties shiroProperties = SpringUtil.getBean(ShiroProperties.class);
            if (log.isDebugEnabled()) {
                log.debug("shiro.expire设置的jwt过期时间为：" + shiroProperties.getExpire());
            }
            Date date = new Date(System.currentTimeMillis() + (shiroProperties.getExpire() * EXPIRE_TIME));

            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带userName信息
            return JWT.create()
                    .withClaim(USER_NAME, userName)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
