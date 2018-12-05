package club.javalearn.ims.shiro;

import club.javalearn.ims.properties.ShiroProperties;
import club.javalearn.ims.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-03
 * Time: 15:06
 * Description: 密码错误5次后，将会锁定账号
 */
@Getter
@Setter
@Slf4j
public class LoginLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {


    @Autowired
    private CacheManager cacheManager;

    private Ehcache passwordRetryCache;

    @Autowired
    private ShiroProperties shiroProperties;


    public LoginLimitHashedCredentialsMatcher() {
        //设置加密次数
        this.setHashIterations(10);
        //设置加密算法
        this.setHashAlgorithmName("sha-1");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {


        System.out.println(token);
        System.out.println(token.getCredentials());
        System.out.println(token.getPrincipal());
        System.out.println(token.getClass().getSimpleName());

        System.out.println(info);
        System.out.println(info.getCredentials());
        System.out.println(info.getPrincipals());
        System.out.println(info.getClass().getSimpleName());
        //获取缓存对象
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");

        String username = (String) token.getPrincipal();
        //retry count + 1
        Element element = passwordRetryCache.get(username);
        if (element == null) {
            element = new Element(username, new AtomicInteger(0));
            passwordRetryCache.put(element);
        }
        AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
        if (retryCount.incrementAndGet() > Constants.TRY_LOGIN_TIME) {
            //if retry count > 5 throw
            log.error("账号密码错误次数过多: {}次，请稍后重试", retryCount);
            throw new ExcessiveAttemptsException("账号密码错误次数过多，请稍后重试");
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
