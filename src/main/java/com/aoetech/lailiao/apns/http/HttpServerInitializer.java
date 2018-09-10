package com.aoetech.lailiao.apns.http;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import io.netty.channel.socket.SocketChannel;

/**
 * Created by HASEE on 2018/5/5.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public HttpServerInitializer(){

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("respDecoder-reqEncoder", new HttpServerCodec())
                .addLast("http-aggregator", new HttpObjectAggregator(655360))
                .addLast(new ChunkedWriteHandler())
                .addLast("action-handler", new HttpHandler());
    }
}
