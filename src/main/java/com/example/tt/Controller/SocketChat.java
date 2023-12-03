package com.example.tt.Controller;

import com.example.tt.Bean.ChatBean;
import com.example.tt.Bean.MySession;
import com.example.tt.OpenResult.LotteryConfigGetter;
import com.example.tt.TtApplication;
import com.example.tt.dao.ChatBeanMapper;
import com.example.tt.dao.LotteryOpenBeanMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Component
@RestController
@ServerEndpoint(value = "/chat/{roomid}/{game}/{userid}")
public class SocketChat {

    ChatBeanMapper chatBeanMapper;

    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    static SessionStorage sessionStorage=SessionStorage.getInstance();

    static Gson gson=new Gson();

    @ResponseBody
    @RequestMapping(value = "/sendChat", method = RequestMethod.POST)
    public Object sendChat(@RequestParam(name = "content") String content,
                           @RequestParam(name = "chat_term",defaultValue = "") String chat_term,
                           @RequestParam(name = "chat_status",defaultValue = "") String chat_status,
                           @RequestParam(name = "userid") String userid,
                           @RequestParam(name = "chatType") String chatType,
                           @RequestParam(name = "roomid") String roomid,
                           @RequestParam(name = "game") String game,
                           @RequestParam(name = "betTerm",defaultValue = "") String betTerm,
                           @RequestParam(name = "chatid",defaultValue = "") String chatid,
                           @RequestParam(name = "headimg",defaultValue = "") String headimg,
                           @RequestParam(name = "username") String username,
                           @RequestParam(name = "imgType",defaultValue = "") String imgType
                           ) {
        if(Strings.isEmptyOrNullAmongOf(content,userid,chatType,roomid,game,username) || !Strings.isDigitOnly(roomid))
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        ChatBean chatBean=new ChatBean();
        chatBean.setType(chatType);
        chatBean.setContent(content);
        chatBean.setUserid(userid);
        chatBean.setRoomid(Integer.parseInt(roomid));
        chatBean.setGame(game);
        chatBean.setChatStatus(chat_status);
        chatBean.setChatTerm(chat_term);
        chatBean.setBetterm(betTerm);
        chatBean.setChatid(chatid);
        chatBean.setUsername(username);
        chatBean.setTerm("");
        chatBean.setMingci("");
        chatBean.setNeirong("");
        chatBean.setStatus("");
        chatBean.setJine("");
        String time=TimeUtils.convertToDetailTime(Calendar.getInstance().getTime());
        chatBean.setAddtime(time);
        chatBean.setTime(time);

        if(Strings.isEmptyOrNullAmongOf(imgType))
        {
            if(Strings.isEmptyOrNullAmongOf(headimg))
            {
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S4);
            }
            else
            {
                chatBean.setHeadimg(headimg);
            }
        }
        else
        {
            switch (imgType)
            {
                case "robot":
                    chatBean.setHeadimg(LotteryConfigGetter.getInstance().getLotteryRoomSetting().getSettingRobotsimg());
                    break;
                case "admin":
                    chatBean.setHeadimg(LotteryConfigGetter.getInstance().getLotteryRoomSetting().getSettingSysimg());
                    break;
            }
        }

        boolean isSendSuccess=false;
        chatBeanMapper= TtApplication.getContext().getBean(ChatBeanMapper.class);
        int insertStatus=chatBeanMapper.insertSelective(chatBean);
        isSendSuccess=insertStatus>0;

        List<ChatBean> list=new ArrayList<>();
        list.add(chatBean);

        Map<String,Object> map=new HashMap<>();
        map.put("betTerm",betTerm);
        map.put("list",list);

        String string=gson.toJson(ReturnDataBuilder.makeBaseJSON(map));
        Map<String, MySession> sessions=sessionStorage.getSessionMapsByGameAndRoomId(roomid,game);
        int sessionSize=0;
        if(sessions!=null && !sessions.isEmpty())
        {
            sessionSize=sessions.size();
            for (Map.Entry<String, MySession> entry : sessions.entrySet()) {
                try {
                    if(entry.getValue().getSession().isOpen())
                    {
                        synchronized (SocketChat.class)
                        {
                            entry.getValue().getSession().getBasicRemote().sendText(string);
                        }
                    }
                    else
                    {
                        sessionStorage.removeSession(entry.getValue().getSession(),roomid,game,entry.getKey(),"acc close");
                    }
                } catch (IOException e) {
                    MyLog.e(e.getMessage());
                    sessionStorage.removeSession(entry.getValue().getSession(),roomid,game,entry.getKey(),"acc close");
                    isSendSuccess = false;
                }
            }
        }


        MyLog.e(" roomid-->"+roomid+"  game--->"+game+" sessions--->"+sessionSize);
        MyLog.e("sendChat-->"+content);

        if(isSendSuccess)
        {
            return ReturnDataBuilder.makeBaseJSON(null);
        }
        else
        {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S5);
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomid") String roomid,
                       @PathParam("game") String game,
                       @PathParam("userid") String userid) throws IOException {

        chatBeanMapper= TtApplication.getContext().getBean(ChatBeanMapper.class);
        lotteryOpenBeanMapper= TtApplication.getContext().getBean(LotteryOpenBeanMapper.class);
        session.getBasicRemote().sendText(new Gson().toJson(NewChats(userid, game, roomid)));
        session.setMaxIdleTimeout(30*1000);
        sessionStorage.putSession(session,userid,roomid,game);

    }

    @OnMessage
    public void OnMessage(@PathParam("roomid") String roomid,
                          @PathParam("game") String game,
                          @PathParam("userid") String userid,String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);

    }


    @OnClose
    public void onClose(@PathParam("roomid") String roomId,
                        @PathParam("game") String game,
                        @PathParam("userid") String userId,Session session)
    {
        sessionStorage.removeSession(session,roomId,game,userId,"onClose");
        StringBuilder sb=new StringBuilder();
        sb.append("onClose  roomId==>").append(roomId);
        sb.append(" game==>").append(game);
        sb.append(" userId==>").append(userId);
        sb.append(" sessionID=>").append(session.getId());
        MyLog.e(sb.toString());
    }

    @OnError
    public void OnError(@PathParam("roomid") String roomId,
                        @PathParam("game") String game,
                        @PathParam("userid") String userId,Session session,Throwable throwable)
    {
        sessionStorage.removeSession(session,roomId,game,userId,"OnError");
        StringBuilder sb=new StringBuilder();
        sb.append("onClose  roomId==>").append(roomId);
        sb.append(" game==>").append(game);
        sb.append(" userId==>").append(userId);
        sb.append(" sessionID=>").append(session.getId());
        MyLog.e(sb.toString());
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public Object NewChats(String userid,
                               String game,
                               String roomid) {
        if (Strings.isNullAmongOf(userid, game, roomid) || !Strings.isDigitOnly(roomid)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        List<ChatBean> list=new ArrayList<>();
        int nowTerm =0;
        if(!Strings.isEmptyOrNullAmongOf(game) && game.equals("all"))
        {
            list.addAll(chatBeanMapper.last50RowByRoom(Integer.parseInt(roomid)));
        }
        else
        {
            list.addAll(chatBeanMapper.last50RowByGame(Integer.parseInt(roomid),game));
            nowTerm = GameIndex.getLotteryIndex(game);
            if (nowTerm < 1) {
                MyLog.e("gameName错误--->"+game);
                return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1);
            }
        }


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserid().equals(userid)) {
                list.get(i).setType("U2");
            }
        }

        String betTerm=lotteryOpenBeanMapper.getOpenByTerm(nowTerm);
        if(betTerm==null)
        {
            betTerm="";
        }
        Map<String,Object> map=new HashMap<>();
        map.put("betTerm",betTerm);
        map.put("list",list);

        return ReturnDataBuilder.makeBaseJSON(map);
    }
}
