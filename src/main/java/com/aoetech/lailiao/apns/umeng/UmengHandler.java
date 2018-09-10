package com.aoetech.lailiao.apns.umeng;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/5/7.
 */
public class UmengHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(UmengHandler.class);
    @Override
    public int getHandlerType() {
        return 103;
    }

    @Override
    public void dealData(MessageInfo info) {
        try {
            PushClient client = new PushClient();
            AndroidUnicast unicast = new AndroidUnicast(Constant.UMENG_APP_KEY,Constant.UMENG_APP_MASTER_SECRET);
            unicast.setDeviceToken(info.getPush_token());
            unicast.setTicker( info.getPush_msg());
            unicast.setTitle(  Constant.appName);
            unicast.setText(   info.getPush_msg());
            unicast.goAppAfterOpen();
            unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
            // For how to register a test device, please see the developer doc.
            unicast.setProductionMode();
            // Set customized fields
            client.send(unicast,info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
