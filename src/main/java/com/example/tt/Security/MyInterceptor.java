package com.example.tt.Security;

import com.example.tt.utils.MyLog;
import com.example.tt.utils.RedisCache;
import com.example.tt.utils.ReturnDataBuilder;
import com.example.tt.utils.Strings;
import com.google.gson.Gson;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MyLog.e("preHandle--> "+request.getRequestURI());
        String userId=request.getParameter("userId");
//        if(Strings.isEmptyOrNullAmongOf(userId))
//        {
//            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().print(ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S14));
//            return false;
//        }
//        else
//        {
//            String token=RedisCache.getInstance().get(userId);
//            if(Strings.isEmptyOrNullAmongOf(token))
//            {
//                response.setContentType("text/html;charset=utf-8");
//                response.getWriter().print(ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S14));
//                return false;
//            }
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
