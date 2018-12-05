package club.javalearn.ims.web.controller;

import club.javalearn.ims.common.ServerResponse;
import club.javalearn.ims.entity.User;
import club.javalearn.ims.service.UserService;
import club.javalearn.ims.utils.JwtUtil;
import club.javalearn.ims.utils.PasswordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-03
 * Time: 17:10
 * Description: No Description
 */
@Slf4j
@RestController
public class LoginController {


    @Autowired
    private UserService userService;


    @Autowired
    private PasswordHelper passwordHelper;

    //@PostMapping("/login")
    public ServerResponse login(@RequestParam("userName") String userName,
                                @RequestParam("password") String password) {

        User user = userService.findByUserName(userName);

        log.info("当前登陆用户为:{}",user);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(401, "用户名密码错误");
        }

        //密码匹配成功
        if (passwordHelper.matchPassword(password, user)) {
            return ServerResponse.createBySuccess("登录成功", JwtUtil.sign(userName, user.getPassword()));
        } else {
            return ServerResponse.createByErrorCodeMessage(401, "用户名密码错误");
        }
    }

    @PostMapping("/login")
    public ServerResponse login(@RequestBody User userInfo) {

        User user = userService.findByUserName(userInfo.getUserName());

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(401, "用户名密码错误");
        }

        //密码匹配成功
        if (passwordHelper.matchPassword(userInfo.getPassword(), user)) {
            ServerResponse serverResponse = ServerResponse.createBySuccess("登录成功");
            serverResponse.setToken(JwtUtil.sign(userInfo.getUserName(), user.getPassword()));
            user.setPassword("");
            user.setSalt("");
            serverResponse.setData(user);
            log.info("当前登陆用户为:{}",user);
            return serverResponse;
        } else {
            return ServerResponse.createByErrorCodeMessage(401, "用户名密码错误");
        }
    }
}
