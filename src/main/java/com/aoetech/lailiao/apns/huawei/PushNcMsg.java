/*
 * FileName:  RedirectMsgService.java
 * License:  Copyright 2014-2024 Huawei Tech. Co. Ltd. All Rights Reserved.
 * Description: Sample of Push Notification Message Sender  
 * Modifier:  
 * Modification Date: 2017年7月13日
 * Modification Content: New
 * Modification Version: Push
 */
package com.aoetech.lailiao.apns.huawei;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;

import com.aoetech.lailiao.apns.config.Constant;
import com.aoetech.lailiao.apns.entity.MessageInfo;
import com.aoetech.lailiao.apns.handler.HandlerResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PushNcMsg {
    private static final Logger logger = LoggerFactory.getLogger(PushNcMsg.class);
    private String appSecret;
    private String appId;
    private static String tokenUrl = "https://login.vmall.com/oauth2/token"; //获取认证Token的URL
    private static String apiUrl = "https://api.push.hicloud.com/pushsend.do"; //应用级消息下发API
    private String accessToken;//下发通知消息的认证Token
    private long tokenExpiredTime;  //accessToken的过期时间

    public PushNcMsg(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public void dealMessage(MessageInfo info) {
        try {
            refreshToken();
            sendPushMessage(info);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    //获取下发通知消息的认证Token
    private void refreshToken() throws IOException {
        String msgBody = MessageFormat.format(
                "grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(appSecret, "UTF-8"), appId);
        String response = httpPost(tokenUrl, msgBody, 5000, 5000);
        JSONObject obj = JSONObject.parseObject(response);
        accessToken = obj.getString("access_token");
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
    }

    //发送Push消息
    private void sendPushMessage(MessageInfo info) throws IOException {
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshToken();
        }      
        /*PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败*/
        /*PushToken不支持手动编写，需使用客户端的onToken方法获取*/
        JSONArray deviceTokens = new JSONArray();//目标设备Token
        deviceTokens.add(info.getPush_token());

        JSONObject body = new JSONObject();//仅通知栏消息需要设置标题和内容，透传消息key和value为用户自定义
        body.put("title", Constant.appName);//消息标题
        body.put("content", info.getPush_msg());//消息内容体

        JSONObject param = new JSONObject();
        param.put("appPkgName", Constant.ANDROID_PACKAGE_NAME);//定义需要打开的appPkgName

        JSONObject action = new JSONObject();
        action.put("type", 3);//类型3为打开APP，其他行为请参考接口文档设置
        action.put("param", param);//消息点击动作参数

        JSONObject msg = new JSONObject();
        msg.put("type", 3);//3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容

        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        ext.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL

        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);
        hps.put("ext", ext);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken,"UTF-8"),
                URLEncoder.encode("openpush.message.api.send","UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000),"UTF-8"),
                URLEncoder.encode(deviceTokens.toString(),"UTF-8"),
                URLEncoder.encode(payload.toString(),"UTF-8"));

        String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        String result = httpPost(postUrl, postBody, 5000, 5000);
        org.json.JSONObject jsonObject = new org.json.JSONObject(result);
        info.setPush_result_string(result);
        info.setPush_result_state("80000000".equals(jsonObject.getString("code"))?MessageInfo.STATE_SUCCESS:MessageInfo.STATE_FAIL);
        HandlerResult.onHandlerResult(info);
    }

    public static String httpPost(String httpUrl, String data, int connectTimeout, int readTimeout) throws IOException {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();

            // POST data
            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();

            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

            List<String> lines = IOUtils.readLines(in, urlConnection.getContentEncoding());
            StringBuffer strBuf = new StringBuffer();
            for (String line : lines) {
                strBuf.append(line);
            }
            System.out.println(strBuf.toString());
            return strBuf.toString();
        } finally {
            IOUtils.closeQuietly(outPut);
            IOUtils.closeQuietly(in);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
