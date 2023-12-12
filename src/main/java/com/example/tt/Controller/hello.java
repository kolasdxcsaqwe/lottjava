package com.example.tt.Controller;

import com.example.tt.Service.QxcService;
import com.example.tt.dao.UserBeanMapper;
import com.example.tt.utils.FetchLotteryResultTask;
import com.example.tt.utils.RedisCache;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class hello {

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    @ResponseBody
    @RequestMapping(value = "/hello")
    public Object hello(@RequestParam(name = "userId",required = false) String userId,
                        @RequestParam(name = "roomId",required = false) String roomId)
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
//                MyLog.e(value);
//            }
//        }.start();
        if(!Strings.isEmptyOrNullAmongOf(roomId,userId))
        {
            return userBeanMapper.selectByUserId(userId,Integer.parseInt(roomId));
        }
        else
        {
            return "sadsadc2";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public String test(HttpServletRequest request)
    {
        StringBuilder sb=new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName()).append(":");
        sb.append(request.getServerPort());
        return sb.toString();
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
