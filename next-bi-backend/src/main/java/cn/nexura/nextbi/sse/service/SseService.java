package cn.nexura.nextbi.sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author peiYP
 * @create 2024-01-21 21:32
 **/
public interface SseService {
    SseEmitter createConnect(String clientId);

    void sendMessageToAllClient(String msg);

    void sendMessageToOneClient(String clientId, String msg);

    void closeConnect(String clientId);
}
