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
public class ProductionApnsServerManager extends BaseApnsServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ProductionApnsServerManager.class);


    private static ProductionApnsServerManager ourInstance = new ProductionApnsServerManager();

    public static ProductionApnsServerManager getInstance() {
        return ourInstance;
    }

    private ProductionApnsServerManager() {
        initClient();
    }

    @Override
    public String getName() {
        return "production";
    }

    @Override
    public void initClient() {
        try {
            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setConcurrentConnections(4)
                    .setClientCredentials(new File(System.getProperty("user.dir") + File.separator + Constant.productionName), Constant.productionPassword)
                    .build();
        } catch (IOException e) {
            logger.error("init ios product client error :" + e.toString());
        }
    }

}
