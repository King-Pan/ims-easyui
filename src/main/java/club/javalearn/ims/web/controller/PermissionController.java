package club.javalearn.ims.web.controller;

import club.javalearn.ims.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 15:27
 * Description: No Description
 */
@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permission/")
    public ModelAndView permissionPage() {
        return new ModelAndView("permission");
    }


    @PostMapping("/permissions")
    public Object list() {
        return permissionService.getList();
    }


    @PostMapping("/menu/treeMenu")
    public Object treeMenu(){
        return permissionService.getUserTreeMenu();
    }
}
