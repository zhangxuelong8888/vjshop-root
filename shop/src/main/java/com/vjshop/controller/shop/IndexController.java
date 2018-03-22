package com.vjshop.controller.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 首页
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Controller("indexController")
public class IndexController {

    /**
     * 列表
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "/shop/${theme}/index";
    }

}
