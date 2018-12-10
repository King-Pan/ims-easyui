package club.javalearn.ims.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getMenu() {
        return new ModelAndView("system/menu");
    }

}
