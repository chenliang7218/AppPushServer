package com.aoetech.lailiao.apns.xiaomi;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.HandlerResult;
import com.aoetech.lailiao.apns.handler.IHandler;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by HASEE on 2018/5/7.
 */
public class XiaoMiHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(XiaoMiHandler.class);

    @Override
    public int getHandlerType() {
        return 101;
    }

    @Override
    public void dealData(MessageInfo info) {
        Sender sender = new Sender(Constant.XIAOMI_APP_SECRET_KEY);
        String messagePayload = info.getPush_msg();
        String title = Constant.appName;
        String description = info.getPush_msg();
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(Constant.ANDROID_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        Result result = null;
        try {
            result = sender.send(message, info.getPush_token(), 3);
            info.setPush_result_string(result.toString());
            info.setThird_uuid(result.getMessageId());
            info.setPush_result_state(result.getErrorCode() == ErrorCode.Success ? MessageInfo.STATE_SUCCESS:MessageInfo.STATE_FAIL);
            logger.info("Server response: "+ result.toString());
        } catch (Exception e) {
            logger.error("XiaoMiHandler dealData error : " + e.toString());
            info.setPush_result_string(e.toString());
        }
        HandlerResult.onHandlerResult(info);

    }
}
