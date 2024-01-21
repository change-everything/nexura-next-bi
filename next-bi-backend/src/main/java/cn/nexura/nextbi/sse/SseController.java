package cn.nexura.nextbi.sse;

import cn.nexura.nextbi.sse.service.SseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {
    @Autowired
    private SseServiceImpl sseService;

    @GetMapping("/createConnect")
    public SseEmitter createConnect(String clientId){
        return sseService.createConnect(clientId);
    }

    @PostMapping("/broadcast")
    public void sendMessageToAllClient(@RequestBody(required = false) String msg){
        sseService.sendMessageToAllClient(msg);
    }

    @PostMapping("/sendMessage")
    public void sendMessageToOneClient(String clientId){
        sseService.sendMessageToOneClient(clientId, "我先给你发个消息你看看咋样？！");
    }

    @GetMapping("/closeConnect")
    public void closeConnect(@RequestParam(required = true) String clientId){
        sseService.closeConnect(clientId);
    }

}
