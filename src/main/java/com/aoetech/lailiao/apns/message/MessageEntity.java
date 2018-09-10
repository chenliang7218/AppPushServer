package com.aoetech.lailiao.apns.message;

/**
 * Created by HASEE on 2018/5/5.
 */
public class MessageEntity {
    /**
     * token:字符串，设备token
     message:字符串，推送内容
     badge:1，数字，角标
     sound:“default”,字符串
     sandbox:1,数字，0--正式服务器，1--测试服务器
     * token : 111
     * message : 111
     * badge : 1
     * sound : 1111
     * sandbox : 1
     */

    private String token;
    private String message;
    private int badge;
    private String sound;
    private int sandbox;

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setSandbox(int sandbox) {
        this.sandbox = sandbox;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getBadge() {
        return badge;
    }

    public String getSound() {
        return sound;
    }

    public int getSandbox() {
        return sandbox;
    }
}
