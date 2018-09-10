package com.aoetech.lailiao.apns.IOS;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.IHandler;
import com.aoetech.lailiao.apns.message.DevelopApnsServerManager;
import com.aoetech.lailiao.apns.message.ProductionApnsServerManager;
import com.google.gson.JsonSyntaxException;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/5/7.
 */
public abstract class IOSHandler implements IHandler {

    private static final Logger logger = LoggerFactory.getLogger(IOSHandler.class);


    @Override
    public void dealData(MessageInfo info) {
        try {
            final SimpleApnsPushNotification pushNotification;

            ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertBody(info.getPush_msg()).setBadgeNumber(info.getPush_msg_badge());
            if (info.getAllow_sound() == 1){
                payloadBuilder = payloadBuilder.setSoundFileName("default");
            }

            final String payload = payloadBuilder.buildWithDefaultMaximumLength();
            final String token = TokenUtil.sanitizeTokenString(info.getPush_token());

            pushNotification = new SimpleApnsPushNotification(token, Constant.IOS_TOPIC, payload);
            if (info.getPush_sdk() == 2){
                ProductionApnsServerManager.getInstance().setNotification(pushNotification,info);
            }else if (info.getPush_sdk() == 4){
                DevelopApnsServerManager.getInstance().setNotification(pushNotification,info);
            }
        } catch (JsonSyntaxException e) {
            logger.error(e.toString());
        }
    }
}
