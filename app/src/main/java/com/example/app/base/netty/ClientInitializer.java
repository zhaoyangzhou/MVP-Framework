package com.example.app.base.netty;

import com.example.app.base.Constants;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Package: com.example.app.base.netty
 * Class: ClientInitializer
 * Description: Netty初始化
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
	 
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
 
        pipeline.addLast("encoder", new TcpEncoder());  
        pipeline.addLast("decoder", new TcpDecoder()); 
 
        pipeline.addLast("idleStateHandler", new IdleStateHandler(Constants.READ_IDLE_TIME, 0, Constants.ALL_IDLE_TIME, TimeUnit.SECONDS));
        // 客户端的逻辑
        pipeline.addLast("handler", new ClientHandler());
        // 处理大数据流
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
    }
}
