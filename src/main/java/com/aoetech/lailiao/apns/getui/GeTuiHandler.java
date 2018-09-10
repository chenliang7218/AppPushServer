package com.aoetech.lailiao.apns.getui;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.HandlerResult;
import com.aoetech.lailiao.apns.handler.IHandler;
import com.aoetech.lailiao.apns.message.JedisClientPool;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/6/28.
 */
public class GeTuiHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(GeTuiHandler.class);
    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";
    @Override
    public int getHandlerType() {
        return 105;
    }

    @Override
    public void dealData(MessageInfo info) {
        IGtPush push = new IGtPush(url, Constant.GE_TUI_APP_KEY, Constant.GE_TUI_MASTER_SECRET);

        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(Constant.GE_TUI_APP_ID);
        template.setAppkey(Constant.GE_TUI_APP_KEY);
        template.setTitle(Constant.appName);
        template.setText(info.getPush_msg());

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(Constant.appName);
        style.setText(info.getPush_msg());
        // 配置通知栏图标
//        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);



        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        SingleMessage message = new SingleMessage();
        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        Target target = new Target();
        target.setAppId(Constant.GE_TUI_APP_ID);
        target.setClientId(info.getPush_token());
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            logger.info(ret.getResponse().toString());
            info.setThird_uuid(ret.getMessageId());
            info.setPush_result_string(ret.getResponse().toString());
            if ("ok".equals(ret.getResponse().get("result"))){
                info.setPush_result_state(MessageInfo.STATE_SUCCESS);
            }else {
                info.setPush_result_state(MessageInfo.STATE_FAIL);
            }
        } else {
            logger.error("个推服务器响应异常");
            info.setPush_result_string("个推服务器响应异常");
            info.setPush_result_state(MessageInfo.STATE_FAIL);
        }
        HandlerResult.onHandlerResult(info);
    }
}
