package com.aoetech.lailiao.apns.entity;


/**
 * Created by HASEE on 2018/5/7.
 */
public class MessageInfo {
    public static final int STATE_SUCCESS = 0;
    public static final int STATE_FAIL = 1;



    /**
     * uid : 1
     * push_sdk : 1
     * allow_sound : 0
     * push_msg :
     * push_msg_badge : 0
     * use_sandbox : 0
     * push_token :
     */
    private int uid;
    private int push_sdk;
    private int allow_sound;
    private String push_msg;
    private int push_msg_badge;
    private int use_sandbox;
    private String push_token;
    private String msg_uuid;
    private String third_uuid;

    private String push_result_string;
    private int push_result_state;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setPush_sdk(int push_sdk) {
        this.push_sdk = push_sdk;
    }

    public void setAllow_sound(int allow_sound) {
        this.allow_sound = allow_sound;
    }

    public void setPush_msg(String push_msg) {
        this.push_msg = push_msg;
    }

    public void setPush_msg_badge(int push_msg_badge) {
        this.push_msg_badge = push_msg_badge;
    }

    public void setUse_sandbox(int use_sandbox) {
        this.use_sandbox = use_sandbox;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public int getUid() {
        return uid;
    }

    public int getPush_sdk() {
        return push_sdk;
    }

    public int getAllow_sound() {
        return allow_sound;
    }

    public String getPush_msg() {
        return push_msg;
    }

    public int getPush_msg_badge() {
        return push_msg_badge;
    }

    public int getUse_sandbox() {
        return use_sandbox;
    }

    public String getPush_token() {
        return push_token;
    }

    public String getMsg_uuid() {
        return msg_uuid;
    }

    public String getThird_uuid() {

        return third_uuid;
    }

    public String getPush_result_string() {
        return push_result_string;
    }

    public void setMsg_uuid(String msg_uuid) {
        this.msg_uuid = msg_uuid;
    }

    public void setThird_uuid(String third_uuid) {
        this.third_uuid = third_uuid;
    }

    public void setPush_result_string(String push_result_string) {
        this.push_result_string = push_result_string;
    }

    public void setPush_result_state(int push_result_state) {
        this.push_result_state = push_result_state;
    }

    public int getPush_result_state() {

        return push_result_state;
    }
}
