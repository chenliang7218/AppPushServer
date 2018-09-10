package com.aoetech.lailiao.apns.http;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.message.DevelopApnsServerManager;
import com.aoetech.lailiao.apns.message.MessageEntity;
import com.aoetech.lailiao.apns.message.ProductionApnsServerManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/5/5.
 */
public class DealHttpThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DealHttpThread.class);

    private String data;

    public DealHttpThread(String data) {
        this.data = data;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        try {
            MessageEntity entity = gson.fromJson(data, MessageEntity.class);
            final SimpleApnsPushNotification pushNotification;

            final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
            payloadBuilder.setAlertBody(entity.getMessage()).setBadgeNumber(entity.getBadge()).setSoundFileName(entity.getSound());

            final String payload = payloadBuilder.buildWithDefaultMaximumLength();
            final String token = TokenUtil.sanitizeTokenString(entity.getToken());

            pushNotification = new SimpleApnsPushNotification(token, Constant.IOS_TOPIC, payload);
//            if (entity.getSandbox() == 0){
//                ProductionApnsServerManager.getInstance().setNotification(pushNotification);
//            }else if (entity.getSandbox() == 1){
//                DevelopApnsServerManager.getInstance().setNotification(pushNotification);
//            }
        } catch (JsonSyntaxException e) {
            logger.error(e.toString());
        }
    }
}
