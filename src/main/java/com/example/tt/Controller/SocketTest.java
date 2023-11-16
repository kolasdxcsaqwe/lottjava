package com.example.tt.Controller;

import com.example.tt.Bean.ChatBean;
import com.example.tt.TtApplication;
import com.example.tt.dao.ChatBeanMapper;
import com.example.tt.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ServerEndpoint(value = "/chat/{roomid}/{game}/{userid}")
public class SocketTest {



    ChatBeanMapper chatBeanMapper;

    static SessionStorage sessionStorage=SessionStorage.getInstance();


    @OnOpen
    public void onOpen(Session session, @PathParam("roomid") String roomid,
                       @PathParam("game") String game,
                       @PathParam("userid") String userid) throws IOException {

        chatBeanMapper= TtApplication.context.getBean(ChatBeanMapper.class);
        session.getBasicRemote().sendText(new Gson().toJson(NewChats(userid, game, roomid)));
        session.setMaxIdleTimeout(30*1000);
        sessionStorage.putSession(session,userid,roomid);
    }

    @OnMessage
    public void OnMessage(String message, Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }


    @OnClose
    public void onClose(@PathParam("roomid") String roomid,
                        @PathParam("game") String game,
                        @PathParam("userid") String userid,Session session)
    {
        System.err.println(roomid+" "+session.getId());
    }

    @OnError
    public void OnError(Session session,Throwable throwable)
    {

    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    public Object NewChats(String userid,
                               String game,
                               String roomid) {
        if (Strings.isNullAmongOf(userid, game, roomid)) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S2);
        }

        List<ChatBean> list = chatBeanMapper.last50Row(roomid, game);
        int nowTerm = GameIndex.getLotteryIndex(game);
        if (nowTerm < 1) {
            return ReturnDataBuilder.error(ReturnDataBuilder.GameListNameEnum.S1);
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserid().equals(userid)) {
                list.get(i).setType("U2");
            }
        }

        Map<String,Object> map=new HashMap<>();
        map.put("betTerm",game);
        map.put("list",list);

        return ReturnDataBuilder.makeBaseJSON(map);
    }
}
