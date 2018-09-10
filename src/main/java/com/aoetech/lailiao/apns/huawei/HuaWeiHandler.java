package com.aoetech.lailiao.apns.huawei;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by HASEE on 2018/5/7.
 */
public class HuaWeiHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HuaWeiHandler.class);

    @Override
    public int getHandlerType() {
        return 102;
    }

    @Override
    public void dealData(MessageInfo info) {
        PushNcMsg pushNcMsg = new PushNcMsg(Constant.HUA_WEI_APP_ID, Constant.HUA_WEI_APP_SECRET);
        pushNcMsg.dealMessage(info);
    }

}
