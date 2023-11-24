package com.example.tt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private Map<String, Map<String, Session>> sessionStore = new ConcurrentHashMap<>();

    private static volatile SessionStorage storage;

    private static final Logger logger = LoggerFactory.getLogger(SessionStorage.class);

    private static final String  FLAG_HEART_BEAT = "heartbeat";

    private SessionStorage() {
    }

    /**
     *   双端检索机制创建一个   SessionStorage 实例
     * &#64;return
     */
    public static synchronized SessionStorage getInstance() {
        if(storage == null) {
            synchronized(SessionStorage.class) {
                if(storage == null) {
                    storage = new SessionStorage();
                }
            }
        }
        return storage;
    }

    /**
     * 将ws连接会话分组保存
     * &#64;author  feiyang
     * &#64;param session
     * &#64;param wsGroupId
     * &#64;param type
     * &#64;return  void
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public  void putSession(Session session, String userId, String roomId) {
        synchronized(SessionStorage.class) {
            if(session != null) {
                Map<String, Session> sessionGroup = this.sessionStore.get(roomId);
                if (sessionGroup == null) {
                    sessionGroup = new ConcurrentHashMap<>(16);
                    this.sessionStore.put(roomId, sessionGroup);
                }
                else
                {
                    if(sessionGroup.get(userId)!=null)
                    {
                        removeSession(roomId,userId,"renewSocket");
                    }
                }
                sessionGroup.put(userId, session);
                System.err.println("putSession  roomId==>"+roomId+" userId==>"+userId+"  连接数"+sessionGroup.size());
            }
        }

    }

    /**
     * ws连接关闭时删除会话
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;param type
     * &#64;return  void
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public void removeSession(String roomId, String userId,String msg) {
        synchronized(SessionStorage.class) {
            Map<String, Session> sessionGroup = sessionStore.get(roomId);
            if(sessionGroup.get(userId)!=null)
            {
                try {
                    sessionGroup.get(userId).close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY,msg));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                return;
            }
            sessionGroup.remove(userId);
            if (sessionGroup.isEmpty()) {
                this.sessionStore.remove(userId);
            }
            System.err.println("removeSession  roomId==>"+roomId+" userId==>"+userId+"  连接数"+sessionGroup.size());
        }

    }

    /**
     * 获取指定ws连接会话
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;param type
     * &#64;return  javax.websocket.Session
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public Session getSession(String wsGroupId, String type) {
        Map<String, Session> sessionGroup = this.sessionStore.get(wsGroupId);
        return sessionGroup.get(type);
    }

    /**
     * 获取指定ws连接会话组全部session
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;return  java.util.List<javax.websocket.Session>
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public List<Session> getSessions(String wsGroupId) {
        Session[] sessions = new Session[]{};
        Map<String, Session> sessionGroup = this.sessionStore.get(wsGroupId);
        return Arrays.asList(sessionGroup.values().toArray(sessions));
    }

    /**
     * 获取全部的会话
     * &#64;author  feiyang
     * &#64;param
     * &#64;return  java.util.List<javax.websocket.Session>
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public List<Session> getAllSession() {
        List<Session> sessionList = new ArrayList<>();
        for (Map<String, Session> sessionGroup : sessionStore.values()) {
            Session[] sessions = new Session[]{};
            sessionList.addAll(Arrays.asList(sessionGroup.values().toArray(sessions)));
        }
        return sessionList;
    }

    /**
     * 针对单个会话发送文本消息
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;param type
     * &#64;param text
     * &#64;return  void
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public void sendTextSingle(String wsGroupId, String type, String text){
        try {
            Session session = getSession(wsGroupId, type);
            if (session.isOpen()) {
                session.getBasicRemote().sendText(text);
                if (text.equals(FLAG_HEART_BEAT)) {
                    logger.info("socket维持链接状态&#xff1a;" + text);
                } else {
                    logger.info("已发送》》》》》" + text);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 针对会话组发送文本信息
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;param text
     * &#64;return  void
     * &#64;date    2019/9/19
     * &#64;throws
     */
    public void  sendTextGroup(String wsGroupId, String text) {
        try {
            List<Session> sessions = getSessions(wsGroupId);
            for(Session session : sessions) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(text);
                    logger.info("已发送》》》》》" + text);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 针对全部会话发送消息
//     * &#64;author  feiyang
//     * &#64;param text
//     * &#64;return  void
//     * &#64;date    2019/9/19
//     * &#64;throws
//     */
//    public void sendTextAll(String text) {
//        try {
//            List<Session> sessions = getAllSession();
//            for(Session session : sessions) {
//                if (session.isOpen()) {
//                    session.getBasicRemote().sendText(text);
//                    logger.info("已发送》》》》》" + text);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
