package cn.nexura.nextbi.manager;

import cn.nexura.nextbi.common.ErrorCode;
import cn.nexura.nextbi.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author PeiYP
 * @since 2024年01月11日 14:56
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;


    public String doChat(long modelId, String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI相应错误");
        }
        return response.getData().getContent();
    }

}
