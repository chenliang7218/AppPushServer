package com.aoetech.lailiao.apns.handler;

import com.aoetech.lailiao.apns.entity.MessageInfo;

/**
 * Created by HASEE on 2018/5/7.
 */
public interface IHandler {
    int getHandlerType();
    void dealData(MessageInfo info);
}
