package club.javalearn.ims.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-04
 * Time: 23:51
 * Description: No Description
 */
@RestController
public class IndexController {

    @GetMapping(value = {"/", "/index", "/home"})
    public ModelAndView indexPage() {
        return new ModelAndView("index");
    }
}
