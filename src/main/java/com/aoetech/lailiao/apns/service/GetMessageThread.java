package com.aoetech.lailiao.apns.service;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.handler.DealMessageThread;
import com.aoetech.lailiao.apns.message.JedisClientPool;
import com.aoetech.lailiao.apns.thread.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by HASEE on 2018/5/7.
 */
public class GetMessageThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GetMessageThread.class);

    @Override
    public void run() {
        while (true){
            try {
                List<String> datas = JedisClientPool.blpop(Constant.REDIS_MESSAGE_KEY);
                if (datas != null && datas.size() != 0){
                    for (String data :datas) {
                        if (!Constant.REDIS_MESSAGE_KEY.equals(data)) {
                            logger.info("message information ：" + data);
                            ThreadPoolManager.getInstance().dealWork(new DealMessageThread(data));
                        }
                    }
                }
            }catch (Exception e){
                logger.error(e.toString());
            }
        }
    }
}
