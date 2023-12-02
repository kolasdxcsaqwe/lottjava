package com.example.tt.utils;

import com.example.tt.Bean.MySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private Map<String, Map<String, MySession>> sessionStore = new ConcurrentHashMap<>();

    private static volatile SessionStorage storage;

    private static final Logger logger = LoggerFactory.getLogger(SessionStorage.class);

    private static final String FLAG_HEART_BEAT = "heartbeat";

    private SessionStorage() {
    }

    /**
     * 双端检索机制创建一个   SessionStorage 实例
     * &#64;return
     */
    public static synchronized SessionStorage getInstance() {
        if (storage == null) {
            synchronized (SessionStorage.class) {
                if (storage == null) {
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
    public void putSession(Session session,String userId,String roomId, String game) {
        synchronized (SessionStorage.class) {
            if (session != null) {
                Map<String, MySession> sessionGroup = this.sessionStore.get(roomId + game);
                if (sessionGroup == null) {
                    sessionGroup = new ConcurrentHashMap<>(16);
                    this.sessionStore.put(roomId + game, sessionGroup);
                }
                MySession mySession=new MySession();
                mySession.setSession(session);
                mySession.setRoomId(roomId);
                mySession.setUserId(userId);
                mySession.setGame(game);
                sessionGroup.put(session.getId(), mySession);
                MyLog.e("putSession  roomId==>" + roomId + " game==>" + game + " userId==>" + userId + "  连接数" + sessionGroup.size());
            }
        }

    }

    /**
     * ws连接关闭时删除会话
     * &#64;author  feiyang
     * &#64;param wsGroupId
     * &#64;param type
     * &#64;return  void
     * &#64;date    2019/9/
     * &#64;throws
     */
    public void removeSession(Session session,String roomId, String game, String userId, String msg) {
        synchronized (SessionStorage.class) {
            Map<String, MySession> sessionGroup = sessionStore.get(roomId + game);

            if (sessionGroup != null) {
                MySession mSession=sessionGroup.get(session.getId());
                if(mSession!=null)
                {
                    try {
                        session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return;
            }

            sessionGroup.remove(userId);
            MyLog.e("removeSession  roomId==>" + roomId + " game==>" + game + " userId==>" + userId + "  连接数" + sessionGroup.size());
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
//    public Session getSession(String wsGroupId, String type) {
//        Map<String, MySession> sessionGroup = this.sessionStore.get(wsGroupId);
//        return sessionGroup.get(type);
//    }
//
//    /**
//     * 获取指定ws连接会话组全部session
//     * &#64;author  feiyang
//     * &#64;param wsGroupId
//     * &#64;return  java.util.List<javax.websocket.Session>
//     * &#64;date    2019/9/19
//     * &#64;throws
//     */
//    public List<Session> getSessions(String wsGroupId) {
//        Session[] sessions = new Session[]{};
//        Map<String, Session> sessionGroup = this.sessionStore.get(wsGroupId);
//        return Arrays.asList(sessionGroup.values().toArray(sessions));
//    }

    /**
     * 获取全部的会话
     * &#64;author  feiyang
     * &#64;param
     * &#64;return  java.util.List<javax.websocket.Session>
     * &#64;date    2019/9/19
     * &#64;throws
     */
//    public List<Session> getAllSession() {
//        synchronized (SessionStorage.class) {
//            List<Session> sessionList = new ArrayList<>();
//            for (Map<String, Session> sessionGroup : sessionStore.values()) {
//                Session[] sessions = new Session[]{};
//                sessionList.addAll(Arrays.asList(sessionGroup.values().toArray(sessions)));
//            }
//            return sessionList;
//        }
//    }
//
//    public List<Session> getSessionByGameAndRoomId(String roomId, String game) {
//
//        synchronized (SessionStorage.class) {
//            List<Session> sessionList;
//            Map<String, Session> sessionGroup = sessionStore.get(roomId + game);
//            if(sessionGroup!=null)
//            {
//                sessionList = new ArrayList<>(sessionGroup.values());
//            }
//            else
//            {
//                sessionList=new ArrayList<>();
//            }
//
//            return sessionList;
//        }
//    }

    public Map<String, MySession> getSessionMapsByGameAndRoomId(String roomId, String game) {

        synchronized (SessionStorage.class) {
            return sessionStore.get(roomId + game);
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
