package club.javalearn.ims.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 14:27
 * Description: 菜单控制器
 */
@RestController
public class MenuController {


    @GetMapping("/menus")
    public Object getMenu() {
        return "";
    }

}
