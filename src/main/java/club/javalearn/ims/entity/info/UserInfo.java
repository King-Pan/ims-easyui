package club.javalearn.ims.entity.info;

import club.javalearn.ims.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/6/25
 * Time: 上午9:59
 * Description: No Description
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "UserInfo", description = "用户信息")
public class UserInfo implements Serializable {

    public static Long serialVersionUID = 1l;

    /**
     * 用戶编码-自增长策略
     */
    private Long userId;

    /**
     * 登录用户名
     */

    private String userName;


    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户电话号码
     */
    private String phoneNum;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * salt
     */
    private String salt;

    /**
     * 用户头像地址
     */
    private String imgUrl;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 修改日期
     */
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户状态
     */
    private String status;


    public UserInfo(User user){
        this.createTime = user.getCreateTime();
        this.lastLoginTime = user.getLastLoginTime();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.imgUrl = user.getImgUrl();
        this.password = user.getPassword();
        this.phoneNum = user.getPhoneNum();
        this.status = user.getStatus();
        this.updateTime = user.getUpdateTime();
        this.salt = user.getSalt();
    }

    public User convertUser() {
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setImgUrl(imgUrl);
        user.setUpdateTime(updateTime);
        user.setLastLoginTime(lastLoginTime);
        user.setStatus(status);
        user.setSalt(salt);
        user.setCreateTime(createTime);

        return user;
    }

}
