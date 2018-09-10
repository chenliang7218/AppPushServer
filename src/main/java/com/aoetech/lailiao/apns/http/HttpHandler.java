package com.aoetech.lailiao.apns.http;

import com.aoetech.lailiao.apns.thread.ThreadPoolManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * The type Http handler.
 */
public class HttpHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);


    public HttpHandler() {
        super(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {//如果是HTTP请求，进行HTTP操作
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    //处理HTTP的代码
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        logger.warn("uri:" + req.uri());
        ByteBuf buf = req.content();
        String dataContent = buf.toString(Charset.defaultCharset());
//        buf.release();
        logger.info("receive data from http post :" + dataContent);
        ThreadPoolManager.getInstance().dealWork(new DealHttpThread(dataContent));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("client close error : " + cause.toString());
        ctx.close();
        String channelUUid = ctx.channel().id().asLongText();
    }


}
