package com.aoetech.lailiao.apns.config;

import com.aoetech.lailiao.apns.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by HASEE on 2018/5/7.
 */
public class Constant {
    private static final Logger logger = LoggerFactory.getLogger(Constant.class);
    public static String IOS_TOPIC;
    public static String ANDROID_PACKAGE_NAME;
    public static String HUA_WEI_APP_SECRET;
    public static String HUA_WEI_APP_ID;
    public static String developName;
    public static String developPassword;
    public static String productionName;
    public static String productionPassword;
    public static String REDIS_MESSAGE_KEY;
    public static String appName;
    public static String UMENG_APP_KEY;
    public static String UMENG_APP_MASTER_SECRET;
    public static String XIAOMI_APP_SECRET_KEY;
    public static String MEI_ZU_APP_SECRET_KEY;
    public static Long MEI_ZU_APP_ID;
    public static String GE_TUI_APP_ID;
    public static String GE_TUI_APP_KEY;
    public static String GE_TUI_MASTER_SECRET;
    public static String MESSAGE_RESULT_KEY;

    static {
        Properties prop = new Properties();
        File file = new File(System.getProperty("user.dir") + File.separator + Main.CONFIG_FILE_NAME);
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            //prop.load(in);//直接这么写，如果properties文件中有汉子，则汉字会乱码。因为未设置编码格式。
            prop.load(new InputStreamReader(in, "utf-8"));
            logger.info(prop.toString());
            HUA_WEI_APP_ID = prop.getProperty("huawei.app.id");
            HUA_WEI_APP_SECRET = prop.getProperty("huawei.app.secret");
            ANDROID_PACKAGE_NAME = prop.getProperty("packageName");
            IOS_TOPIC = prop.getProperty("topic");
            developName = prop.getProperty("develop.name");
            developPassword = prop.getProperty("develop.password");
            productionName = prop.getProperty("production.name");
            productionPassword = prop.getProperty("production.password");
            appName = prop.getProperty("app.name");
            REDIS_MESSAGE_KEY = prop.getProperty("redis.message.key");
            UMENG_APP_KEY = prop.getProperty("umeng.appkey");
            UMENG_APP_MASTER_SECRET = prop.getProperty("umeng.app.secret");
            XIAOMI_APP_SECRET_KEY = prop.getProperty("xiaomi.app.secret.key");
            MEI_ZU_APP_SECRET_KEY = prop.getProperty("meizu.app.secret.key");
            MEI_ZU_APP_ID = Long.parseLong(prop.getProperty("meizu.app.id"));
            GE_TUI_APP_ID = prop.getProperty("ge.tui.app.id");
            GE_TUI_APP_KEY = prop.getProperty("ge.tui.app.key");
            GE_TUI_MASTER_SECRET = prop.getProperty("ge.tui.master.secret");
            MESSAGE_RESULT_KEY = prop.getProperty("message.result.key");
            in.close();
            logger.info("init Constant ok ! MEI_ZU_APP_ID = " + MEI_ZU_APP_ID + " ;developName =" + developName);
        } catch (Exception e) {
            logger.error("init Constant error :" + e.toString());
        }
    }

}
