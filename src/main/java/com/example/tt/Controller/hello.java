package com.example.tt.Controller;

import com.example.tt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Controller
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

        RedisCache.getInstance().set("BBBB","BDS223",2000);
        String value=RedisCache.getInstance().get("BBBB");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String value=RedisCache.getInstance().get("BBBB");
                System.err.println(value);
            }
        }.start();
        return value;
    }

}
