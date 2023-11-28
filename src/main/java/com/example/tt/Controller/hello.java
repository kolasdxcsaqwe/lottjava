package com.example.tt.Controller;

import com.example.tt.utils.FetchLotteryResultTask;
import com.example.tt.utils.RedisCache;
import com.example.tt.utils.ReturnDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class hello {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @RequestMapping("/lll")
    public String hello()
    {
//        redisTemplate.opsForValue().set("ccc","123",2000, TimeUnit.SECONDS);
//        redisTemplate.opsForValue().set("ccc","5454",2000, TimeUnit.SECONDS);
//        String value=redisTemplate.opsForValue().get("ccc");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return value+"<---  -->"+redisTemplate.opsForValue().get("ccc");

//        RedisCache.getInstance().set("BBBB","BDS223",2000);
//        String value=RedisCache.getInstance().get("BBBB");
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                String value=RedisCache.getInstance().get("BBBB");
//                System.err.println(value);
//            }
//        }.start();

        String path="";

        path=new ClassPathResource("").getPath();
        return new ClassPathResource("").getPath()+" "+getClass().getResourceAsStream("/");
    }

    @ResponseBody
    @RequestMapping(value = "/checkTaskStatus", method = RequestMethod.POST)
    public Object checkTaskStatus()
    {
        Map map=new HashMap();
        map.put("status", FetchLotteryResultTask.isOpen && FetchLotteryResultTask.isInit ? 1:0);
        return ReturnDataBuilder.makeBaseJSON(map);
    }

    @ResponseBody
    @RequestMapping(value = "/changeTaskStatus", method = RequestMethod.POST)
    public Object changeTaskStatus(@RequestParam(name = "changeStatus") int status,@RequestParam(name = "configPath") String path)
    {
        return FetchLotteryResultTask.startProcess(path,status>0);
    }


}
