package com.example.tt.Controller;

import com.example.tt.Service.LoginAndRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginAndRegisterController {

    @Autowired(required = false)
    LoginAndRegisterService service;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam(name = "loginName") String loginName,
                        @RequestParam(name = "password") String password,
                        @RequestParam(name = "roomId") int roomId,
                        @RequestParam(name = "agent") String agent) {

        return service.login(loginName, password, roomId, agent);
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(@RequestParam(name = "loginName") String loginName,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "roomId") int roomId,
                           @RequestParam(name = "agent") String agent) {

        return service.register(loginName, password, roomId, agent);
    }

}
