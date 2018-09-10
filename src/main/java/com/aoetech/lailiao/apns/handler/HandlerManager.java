package com.aoetech.lailiao.apns.handler;


import com.aoetech.lailiao.apns.IOS.IOSDevelopHandler;
import com.aoetech.lailiao.apns.IOS.IOSProductionHandler;
import com.aoetech.lailiao.apns.getui.GeTuiHandler;
import com.aoetech.lailiao.apns.huawei.HuaWeiHandler;
import com.aoetech.lailiao.apns.meizu.MeiZuHandler;
import com.aoetech.lailiao.apns.umeng.UmengHandler;
import com.aoetech.lailiao.apns.xiaomi.XiaoMiHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HASEE on 2018/5/7.
 */
public class HandlerManager {
    private Map<Integer,IHandler> mHandler = new HashMap<>();
    private static HandlerManager ourInstance = new HandlerManager();

    public static HandlerManager getInstance() {
        return ourInstance;
    }

    private HandlerManager() {
    }

    public void initHandler(){
        mHandler.clear();
        mHandler.put(2,new IOSProductionHandler());
        mHandler.put(4,new IOSDevelopHandler());
        mHandler.put(103,new UmengHandler());
        mHandler.put(102,new HuaWeiHandler());
        mHandler.put(101,new XiaoMiHandler());
        mHandler.put(104,new MeiZuHandler());
        mHandler.put(105,new GeTuiHandler());
    }

    public IHandler getHandler(int type){
        return mHandler.get(type);
    }
}
