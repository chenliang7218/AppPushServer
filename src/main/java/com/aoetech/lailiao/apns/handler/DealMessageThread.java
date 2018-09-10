package com.aoetech.lailiao.apns.handler;

import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by HASEE on 2018/5/7.
 */
public class DealMessageThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DealMessageThread.class);
    private String data;
    public DealMessageThread(String data){
        this.data = data;
    }
    @Override
    public void run() {
        Gson gson = new Gson();
        MessageInfo info = gson.fromJson(data, MessageInfo.class);
        IHandler handler = HandlerManager.getInstance().getHandler(info.getPush_sdk());
        if (handler != null) {
            handler.dealData(info);
        }else {
            logger.error("no handler deal message : " + data);
        }
    }
}
