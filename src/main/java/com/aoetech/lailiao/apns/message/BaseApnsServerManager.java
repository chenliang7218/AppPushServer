package com.aoetech.lailiao.apns.message;

import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.HandlerResult;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import com.turo.pushy.apns.util.concurrent.PushNotificationFuture;
import com.turo.pushy.apns.util.concurrent.PushNotificationResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/5/5.
 */
public abstract class BaseApnsServerManager {
    private static final Logger logger = LoggerFactory.getLogger(BaseApnsServerManager.class);

    public abstract String getName();
    public abstract void initClient();
    protected ApnsClient apnsClient;
    public void setNotification(SimpleApnsPushNotification notification,final MessageInfo info){
        if (apnsClient == null){
            initClient();
        }
        try {
            final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient.sendNotification(notification);
            sendNotificationFuture.addListener(new PushNotificationResponseListener<SimpleApnsPushNotification>() {

                @Override
                public void operationComplete(final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future) throws Exception {
                    // When using a listener, callers should check for a failure to send a
                    // notification by checking whether the future itself was successful
                    // since an exception will not be thrown.
                    if (future.isSuccess()) {
                        final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse =
                                sendNotificationFuture.getNow();
                        logger.info( getName() + " Production notification accepted by APNs gateway. apns id :" + pushNotificationResponse.getApnsId() + " ;reason " + pushNotificationResponse.getRejectionReason());
                        info.setPush_result_string(pushNotificationResponse.getRejectionReason());
                        info.setThird_uuid(pushNotificationResponse.getApnsId().toString());
                        info.setPush_result_state(MessageInfo.STATE_SUCCESS);
                        // Handle the push notification response as before from here.
                    } else {
                        // Something went wrong when trying to send the notification to the
                        // APNs gateway. We can find the exception that caused the failure
                        // by getting future.cause().
                        logger.info(getName() + " Notification rejected by the APNs gateway: " + future.cause().toString());
                        info.setPush_result_string(future.cause().toString());
                        info.setPush_result_state(MessageInfo.STATE_FAIL);
                    }
                    HandlerResult.onHandlerResult(info);
                }
            });
        } catch (final Exception e) {
            logger.error( getName() + " Failed to send push notification.");
            info.setPush_result_string(" Failed to send push notification.");
            info.setPush_result_state(MessageInfo.STATE_FAIL);
            HandlerResult.onHandlerResult(info);
            e.printStackTrace();
        }
    }
}
