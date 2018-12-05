package club.javalearn.ims.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-03
 * Time: 22:25
 * Description: No Description
 */
@RestController
public class IndexController {


    @RequestMapping("/hello")
    public String hello(){
        return "Hello Spring Boot Shiro Jwt";
    }


    @RequestMapping("/vip")
    @RequiresRoles("vip")
    public String vip(){
        return "vip才能访问的页面";
    }


    @RequestMapping("/user")
    @RequiresRoles("user")
    public String user(){
        return "user才能访问的页面";
    }


    @PutMapping("/add")
    @RequiresPermissions("user:add")
    public String userAdd(){
        return "用户添加操作";
    }

    @DeleteMapping("/delete")
    @RequiresPermissions("user:delete")
    public String userDelete(){
        return "用户添加操作";
    }
}
