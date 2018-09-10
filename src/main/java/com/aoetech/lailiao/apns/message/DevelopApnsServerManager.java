package com.aoetech.lailiao.apns.message;

import com.aoetech.lailiao.apns.config.Constant;
import com.turo.pushy.apns.ApnsClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by HASEE on 2018/5/5.
 */
public class DevelopApnsServerManager extends BaseApnsServerManager{
    private static final Logger logger = LoggerFactory.getLogger(DevelopApnsServerManager.class);

    private static DevelopApnsServerManager ourInstance = new DevelopApnsServerManager();

    public static DevelopApnsServerManager getInstance() {
        return ourInstance;
    }

    private DevelopApnsServerManager() {
        initClient();
    }

    @Override
    public String getName() {
        return "develop";
    }

    @Override
    public void initClient() {
        try {
            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setClientCredentials(new File(System.getProperty("user.dir") + File.separator + Constant.developName), Constant.developPassword)
                    .build();
        } catch (IOException e) {
            logger.error("init ios develop client error : " + e.toString());
        }
    }
}
