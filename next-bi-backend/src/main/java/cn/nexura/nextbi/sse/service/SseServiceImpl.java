package cn.nexura.nextbi.sse.service;

import cn.nexura.nextbi.common.BaseResponse;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author peiYP
 * @create 2024-01-21 21:29
 **/
@Service
public class SseServiceImpl implements SseService {

    // 创建一个容器来存储所有的 SseEmitter 使用ConcurrentHashMap 是因为它是线程安全的。
    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    private static Integer num=0;

    /**
     * 模拟数据
     */
    private static List<String> msgList = new ArrayList<String>(){{
        add("早上好呀!");
        add("新的一天要加油呀!");
        add("要天天开心呀!");
        add("你可以的呀!");
    }};

    @Override
    public SseEmitter createConnect(String clientId) {
        // 设置过期时间 0 表示 不过期 默认值位30秒
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 如果clientId 为空 后端自动创建一个clientId 并返回给前端
        if (ObjectUtils.isEmpty(clientId)){
            clientId = UUID.randomUUID().toString().replaceAll("-","");
        }
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));     // 长链接完成后回调接口(即关闭连接时调用)
        sseEmitter.onTimeout(timeoutCallBack(clientId));        // 连接超时回调
        sseEmitter.onError(errorCallBack(clientId));          // 推送消息异常时，回调方法

        // 存入容器中
        sseCache.put(clientId,sseEmitter);
        System.out.println("创建新的sse连接,当前用户:"+clientId+"   累计用户数:"+sseCache.size());
        try {
            List<BaseResponse> list = new ArrayList<>();
            list.add(new BaseResponse(0, clientId));
            sseEmitter.send(SseEmitter.event().data(list, MediaType.APPLICATION_JSON));
        }catch (Exception e){
            System.out.println("创建ss连接异常,客户端id:"+clientId);
            e.printStackTrace();
        }
        return sseEmitter;
    }

    /**
     * 发送消息给所有客户端
     * @param msg
     */
    @Override
    public void sendMessageToAllClient(String msg) {
        if (ObjectUtils.isEmpty(sseCache)){
            return;
        }
        BaseResponse<String> responseResult = new BaseResponse<>(200, msg);

        for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
            sendMsgToClientByClientId(entry.getKey(),responseResult,entry.getValue());
        }
    }

    /**
     * 根据clientId发送消息给某一客户端
     * @param clientId
     * @param msg
     */
    @Override
    public void sendMessageToOneClient(String clientId, String msg) {
        BaseResponse<String> responseResult = new BaseResponse<>(200, msg);

        sendMsgToClientByClientId(clientId,responseResult,sseCache.get(clientId));
    }

    /**
     * 关闭连接
     * @param clientId
     */
    @Override
    public void closeConnect(String clientId) {
        // 获取对应的sseEmitter
        SseEmitter sseEmitter = sseCache.get(clientId);

        if (sseEmitter!=null){
            sseEmitter.complete();
            removeUser(clientId);
        }
    }


    /**
     * 获取写死的消息
     * @return
     */
    private String getMessage(){
        String result = msgList.get(num);
        num = num+1;
        num = num%4;
        return result;
    }


    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     * @param clientId
     * @return
     */
    private Runnable completionCallBack(String clientId) {
        return () -> {
            System.out.println("结束连接:"+clientId);
            removeUser(clientId);
        };
    }

    /**
     * 连接超时回调
     * @param clientId
     * @return
     */
    private Runnable timeoutCallBack(String clientId){
        return ()->{
            System.out.println("连接超时:"+clientId);
            removeUser(clientId);
        };
    }

    /**
     * 根据客户端id 发送给某一客户端
     * @param clientId
     * @param responseResultList
     * @param sseEmitter
     */
    private void sendMsgToClientByClientId(String clientId, BaseResponse responseResultList, SseEmitter sseEmitter){
        if (sseEmitter == null){
            System.out.println("推送消息失败:客户端:"+clientId+" 未创建长连接,失败消息:"+responseResultList.toString());
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id("201").data(responseResultList, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (IOException e) {
            // 推送消息失败，记录错误日志，进行重推
            System.out.println(" 推送消息失败："+responseResultList.toString());
            boolean isSuccess = true;
            for (int i = 0;i<5;i++){
                try {
                    Thread.sleep(1000);
                    sseEmitter = sseCache.get(clientId);
                    if(sseEmitter == null){
                        System.out.println(responseResultList.toString()+"消息的"+"第"+i+1+"次"+"重推失败,未创建长链接");
                        continue;
                    }
                    sseEmitter.send(sendData);
                }catch (Exception ex){
                    System.out.println(responseResultList.toString()+"消息的"+"第"+i+1+"次"+"重推失败");
                    ex.printStackTrace();
                    continue;
                }
                System.out.println(responseResultList.toString()+"消息的"+"第"+i+1+"次"+"重推成功");
                return;
            }
        }
    }

    /**
     * 推送消息异常时，回调方法
     * @param clientId
     * @return
     */
    private Consumer<Throwable> errorCallBack(String clientId){
        return throwable -> {
            System.out.println("连接异常:客户端ID:"+clientId);

            // 推送消息失败后 每隔1s 推送一次 推送5次
            for (int i = 0;i<5;i++){
                try {
                    Thread.sleep(1000);
                    SseEmitter sseEmitter = sseCache.get(clientId);
                    if (sseEmitter == null){
                        System.out.println("第"+i+"次消息重推失败,未获取到"+clientId+"对应的长链接");
                        continue;
                    }
                    sseEmitter.send("失败后重新推送");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * 删除用户
     * @param clientId
     */
    private void  removeUser(String clientId){
        sseCache.remove(clientId);
        System.out.println("移除用户:"+clientId);
    }
}
