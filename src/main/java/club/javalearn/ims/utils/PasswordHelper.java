package club.javalearn.ims.utils;

import club.javalearn.ims.entity.User;
import club.javalearn.ims.properties.ShiroProperties;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/11
 * Time: 下午1:42
 * Description: No Description
 */
@Component
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Autowired
    private ShiroProperties shiroProperties;


    public String encryptPassword(User user, String password) {
        String newPassword = new SimpleHash(
                //加密算法
                shiroProperties.getPassword().getAlgorithmName(),
                //密码
                password,
                //salt盐   username + salt
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                //迭代次数
                shiroProperties.getPassword().getHashIterations()
        ).toHex();
        return newPassword;
    }

    public void encryptPassword(User user, Boolean isNew) {
        String password;
        if (isNew) {
            user.setSalt(user.getUserName() + randomNumberGenerator.nextBytes().toHex());
            password = shiroProperties.getPassword().getDefaultPassword();
        } else {
            password = user.getPassword();
        }

        String newPassword = new SimpleHash(
                //加密算法
                shiroProperties.getPassword().getAlgorithmName(),
                //密码
                password,
                //salt盐   username + salt
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                //迭代次数
                shiroProperties.getPassword().getHashIterations()
        ).toHex();

        user.setPassword(newPassword);
    }


    public boolean matchPassword(String password,User user){

        //根据salt和密码生成加密后的密码
        String newPassword = new SimpleHash(
                //加密算法
                shiroProperties.getPassword().getAlgorithmName(),
                //密码
                password,
                //salt盐   username + salt
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                //迭代次数
                shiroProperties.getPassword().getHashIterations()
        ).toHex();

        return newPassword.equals(user.getPassword());
    }
}
