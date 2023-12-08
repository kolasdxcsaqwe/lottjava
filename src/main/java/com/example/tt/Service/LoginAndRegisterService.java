package com.example.tt.Service;

import com.example.tt.Bean.UserBean;
import com.example.tt.dao.UserBeanMapper;
import com.example.tt.utils.MD5Gen;
import com.example.tt.utils.RedisCache;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

@Service
public class LoginAndRegisterService {

    @Autowired(required = false)
    UserBeanMapper userBeanMapper;

    private final int expireTime=1000*60*60;//1小时登陆过期


    public Object login(String userName,String password,int roomId,String agent)
    {
        UserBean userBean= userBeanMapper.selectByUserAndRoomIdAndPassword(userName,roomId,MD5Gen.getMD5Str(password),agent);
        if(userBean==null || Strings.isEmptyOrNullAmongOf(userBean.getLoginpass()))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S13);
        }

        RedisCache.getInstance().set(userBean.getUserid()+userBean.getRoomid(),MD5Gen.getMD5Str(UUID.randomUUID().toString()),expireTime);
        return ReturnDataBuilder.makeBaseJSON(userBean);
    }

    public Object register(String userName,String password,int roomId,String agent)
    {
       UserBean userBean= userBeanMapper.selectByUserAndRoomId(userName,roomId,agent);
       if(userBean!=null)
       {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S12);
       }

        //46f94c8de14fb36680850768ff1b7f2a
        userBean=new UserBean();
        userBean.setUserid(UUID.randomUUID().toString());
        userBean.setMoney(new BigDecimal("0"));
        userBean.setBzname("");
        userBean.setStatustime((int)(System.currentTimeMillis()/1000));
        userBean.setAztime(Calendar.getInstance().getTime());
        userBean.setShuashui("false");
        userBean.setLoginuser(userName);
        userBean.setUsername(userName);
        userBean.setLoginpass(MD5Gen.getMD5Str(password));
        userBean.setHeadimg("/upload/0.png");
        userBean.setMoney(new BigDecimal(0));
        userBean.setRoomid(roomId);
        userBean.setAgent(agent);
        userBean.setJia("false");
        userBean.setIsagent("false");
        userBean.setLevel(1);
        userBean.setHmd("");
        userBean.setYinhang("");
        userBean.setHuming("");
        userBean.setKahao("");

        int code=userBeanMapper.insertSelective(userBean);
        RedisCache.getInstance().set(userBean.getUserid()+userBean.getRoomid(),MD5Gen.getMD5Str(UUID.randomUUID().toString()),expireTime);
        if(code<1)
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S9);
        }
        else
        {
            return ReturnDataBuilder.makeBaseJSON(userBean);
        }
    }
}
