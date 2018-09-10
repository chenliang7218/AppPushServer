package com.aoetech.lailiao.apns.http;

import com.aoetech.lailiao.apns.Main;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * Created by HASEE on 2018/5/5.
 */
public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static int port;
    static {
        Properties prop = new Properties();
        File file = new File(System.getProperty("user.dir") + File.separator + Main.CONFIG_FILE_NAME);
//        InputStream in = Object.class.getResourceAsStream("/server.properties");
        try {
            FileInputStream stream = new FileInputStream(file);
            prop.load(stream);
            port = Integer.parseInt(prop.getProperty("port"));
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
    public HttpServer(){
    }
    public void run()  {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());

            Channel ch = b.bind(port).sync().channel();
        }catch (Exception e){
            logger.error(e.toString());
        }
    }

}
