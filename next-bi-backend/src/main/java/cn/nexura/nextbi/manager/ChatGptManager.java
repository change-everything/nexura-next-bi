//package cn.nexura.nextbi.manager;
//
//import cn.nexura.nextbi.common.ErrorCode;
//import cn.nexura.nextbi.exception.BusinessException;
//import com.github.rholder.retry.*;
//import com.unfbx.chatgpt.OpenAiStreamClient;
//import com.unfbx.chatgpt.entity.chat.ChatCompletion;
//import com.unfbx.chatgpt.entity.chat.Message;
//import com.unfbx.chatgpt.function.KeyRandomStrategy;
//import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
//import com.yupi.yucongming.dev.client.YuCongMingClient;
//import com.yupi.yucongming.dev.common.BaseResponse;
//import com.yupi.yucongming.dev.model.DevChatRequest;
//import com.yupi.yucongming.dev.model.DevChatResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.Objects;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author PeiYP
// * @since 2024年01月11日 14:56
// */
////@Service
////@Slf4j
//public class ChatGptManager {
//
//    public static void main(String[] args) {
//        OpenAiStreamClient client = OpenAiStreamClient.builder()
//                .apiKey(Arrays.asList("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"))
//                //自定义key的获取策略：默认KeyRandomStrategy
//                .keyStrategy(new KeyRandomStrategy())
//                .build();
//        //聊天模型：gpt-3.5
//        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
//        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
//        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
//        client.streamChatCompletion(chatCompletion, eventSourceListener);
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
