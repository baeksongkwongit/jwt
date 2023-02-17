package com.cos.jwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    public @ResponseBody String Index(){
        return "Index";

    }
}
