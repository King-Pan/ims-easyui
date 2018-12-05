package club.javalearn.ims.shiro;

import club.javalearn.ims.entity.Permission;
import club.javalearn.ims.entity.Role;
import club.javalearn.ims.entity.User;
import club.javalearn.ims.service.UserService;
import club.javalearn.ims.utils.Constants;
import club.javalearn.ims.utils.JwtUtil;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/6/22
 * Time: 下午5:40
 * Description: No Description
 */
@Slf4j
public class DefaultAuthorizingRealm extends AuthorizingRealm implements Realm {


    @Autowired
    private UserService userService;


    /**
     * 认证.登录
     *
     * @param authenticationToken 用户信息
     * @return AuthenticationInfo
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        String userName = JwtUtil.getUsername(token);
        log.info("=======>当前登录的用户为: {}<=======", userName);
        if (StringUtils.isBlank(userName)) {
            throw new UnauthorizedException("token已失效");
        }
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UnauthorizedException("用户不存在");
        }

        if (!user.getStatus().endsWith(Constants.DEFAULT_STATUS)) {
            throw new AuthenticationException("账号状态未启动");
        }

        try {
            if (JwtUtil.verify(token, userName, user.getPassword())) {
                log.info("=======>当前登录的用户为: [{}] 登录成功<=======", userName);
            } else {
                log.error("=======>当前登录的用户为: [{}] 登录失败<=======", userName);
            }
        } catch (TokenExpiredException exception) {
            log.error(exception.getMessage(), exception);
            throw new AuthenticationException("密码已过期");
        } catch (SignatureVerificationException exception) {
            log.error(exception.getMessage(), exception);
            throw new AuthenticationException("账号密码错误");
        } catch (AuthenticationException exception) {
            log.error(exception.getMessage(), exception);
            throw new AuthenticationException(exception.getMessage());
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new AuthenticationException(exception.getMessage());
        }


        //放入shiro.调用CredentialsMatcher检验密码
        return new SimpleAuthenticationInfo(token, token, this.getClass().getName());
    }

    /**
     * 授权
     *
     * @param principal 用户登录信息
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取session中的用户
        //User user = (User) principal.fromRealm(this.getClass().getName()).iterator().next();
        String userName = JwtUtil.getUsername(principal.toString());
        User user = userService.findByUserName(userName);
        List<String> permissions = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        Set<String> roleSet = new HashSet<>(10);
        if (roles.size() > 0) {
            for (Role role : roles) {
                roleSet.add(role.getRoleCode());
                Set<Permission> permissionList = role.getPermissions();
                if (CollectionUtils.isNotEmpty(permissionList)) {
                    for (Permission permission : permissionList) {
                        if (StringUtils.isNoneBlank(permission.getExpression())) {
                            permissions.add(permission.getExpression());
                        } else {
                            log.error("{} 未配置 expression 属性", permission.getPermissionName());
                        }

                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //将权限放入shiro中.
        info.addStringPermissions(permissions);
        info.addRoles(roleSet);
        return info;
    }


    @Override
    public String getName() {
        return "DefaultAuthorizingRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
}
