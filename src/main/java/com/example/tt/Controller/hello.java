package com.example.tt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class hello {

    @ResponseBody
    @RequestMapping("/lll")
    public String hello()
    {
        return "sad";
    }
}
