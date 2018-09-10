package com.aoetech.lailiao.apns.meizu;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.HandlerResult;
import com.aoetech.lailiao.apns.handler.IHandler;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ErrorCode;
import com.meizu.push.sdk.server.constant.PushResponseCode;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HASEE on 2018/6/27.
 */
public class MeiZuHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(MeiZuHandler.class);
    @Override
    public int getHandlerType() {
        return 104;
    }

    @Override
    public void dealData(MessageInfo info) {

        try {
            //推送对象
            IFlymePush push = new IFlymePush(Constant.MEI_ZU_APP_SECRET_KEY);

            //组装消息
            VarnishedMessage message = new VarnishedMessage.Builder().appId(Constant.MEI_ZU_APP_ID)
                    .title(Constant.appName).content(info.getPush_msg())
                    .offLine(true).validTime(12)
                    .suspend(true).clearNoticeBar(true).vibrate(true).lights(true).sound(true)
                    .build();

            //目标用户
            List<String> pushIds = new ArrayList<String>();
            pushIds.add(info.getPush_token());
            // 1 调用推送服务
            ResultPack<PushResult> result = push.pushMessage(message, pushIds);
            if (result.isSucceed()) {
                // 2 调用推送服务成功 （其中map为设备的具体推送结果，一般业务针对超速的code类型做处理）
                PushResult pushResult = result.value();
                String msgId = pushResult.getMsgId();//推送消息ID，用于推送流程明细排查
                Map<String, List<String>> targetResultMap = pushResult.getRespTarget();//推送结果，全部推送成功，则map为empty
                logger.debug("push msgId:" + msgId);
                logger.debug("push targetResultMap:" + targetResultMap);
                if (targetResultMap != null && !targetResultMap.isEmpty()) {
                    // 3 判断是否有获取超速的target
                    if (targetResultMap.containsKey(PushResponseCode.RSP_SPEED_LIMIT.getValue())) {
                        // 4 获取超速的target
                        List<String> rateLimitTarget = targetResultMap.get(PushResponseCode.RSP_SPEED_LIMIT.getValue());
                        logger.debug("rateLimitTarget is :" + rateLimitTarget);
                    }
                }
                info.setThird_uuid(msgId);
                info.setPush_result_string(pushResult.toString());
                info.setPush_result_state(MessageInfo.STATE_SUCCESS);
            } else {
                // 调用推送接口服务异常 eg: HUA_WEI_APP_ID、appKey非法、推送消息非法.....
                // result.code(); //服务异常码
                // result.comment();//服务异常描述

                //全部超速
                if (String.valueOf(ErrorCode.APP_REQUEST_EXCEED_LIMIT.getValue()).equals(result.code())) {
                    //TODO 5 业务处理，重推......
                }
                logger.error(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
                info.setPush_result_string(String.format("pushMessage error code:%s comment:%s", result.code(), result.comment()));
                info.setPush_result_state(MessageInfo.STATE_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            info.setPush_result_state(MessageInfo.STATE_FAIL);
            info.setPush_result_string(e.toString());
        }
        HandlerResult.onHandlerResult(info);
    }
}
