package com.aoetech.lailiao.apns.handler;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.message.JedisClientPool;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ${DESCRIPTION}
 *
 * @author
 * @create 2018-08-21 19:09
 **/
public class HandlerResult {
    private static final Logger logger = LoggerFactory.getLogger(HandlerResult.class);
    public static void onHandlerResult(MessageInfo info){
        Gson gson = new Gson();
        JedisClientPool.repush(Constant.MESSAGE_RESULT_KEY,gson.toJson(info));
        logger.debug("HandlerResult key : " + Constant.MESSAGE_RESULT_KEY + " ;value : " + gson.toJson(info));
    }
}
