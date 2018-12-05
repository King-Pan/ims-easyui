package club.javalearn.ims.config;

import club.javalearn.ims.shiro.DefaultAuthorizingRealm;
import club.javalearn.ims.shiro.JwtFilter;
import club.javalearn.ims.shiro.LoginLimitHashedCredentialsMatcher;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-03
 * Time: 15:05
 * Description: Shiro的配置类
 */
@Configuration
public class ShiroConfig {

    /**
     * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>(20);
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JwtFilter());

        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);

        // 设置无权限时跳转的 url;
        factoryBean.setUnauthorizedUrl("/unauthorized/无权限");

        Map<String, String> filterRuleMap = new HashMap<>(20);
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/logout", "logout");
        filterRuleMap.put("/**", "jwt");

        // 访问 /unauthorized/** 不通过JWTFilter
        filterRuleMap.put("/unauthorized/**", "anon");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getDefaultAuthorizingRealm());
        return securityManager;
    }


    /**
     * 配置自定义的权限登录器
     */
    @Bean
    public DefaultAuthorizingRealm getDefaultAuthorizingRealm() {
        DefaultAuthorizingRealm authorizingRealm = new DefaultAuthorizingRealm();
        // 配置自定义的密码比较器
        //authorizingRealm.setCredentialsMatcher(loginLimitHashedCredentialsMatcher());
        return authorizingRealm;
    }




    /**
     * 配置自定义的密码比较器
     *
     * @return LoginLimitHashedCredentialsMatcher
     */
    @Bean
    public LoginLimitHashedCredentialsMatcher loginLimitHashedCredentialsMatcher() {
        LoginLimitHashedCredentialsMatcher credentialsMatcher = new LoginLimitHashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("sha-1");
        credentialsMatcher.setHashIterations(10);
        return credentialsMatcher;
    }




    @Bean
    public CacheManager cacheManager() {
        return CacheManager.newInstance(CacheManager.class.getClassLoader().getResource("ehcache.xml"));
    }

}
