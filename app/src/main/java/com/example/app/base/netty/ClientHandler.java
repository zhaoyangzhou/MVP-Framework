package com.example.app.base.netty;

import android.util.Log;

import com.example.app.service.PushService;

import de.greenrobot.event.EventBus;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Package: com.example.app.base.netty
 * Class: ClientHandler
 * Description: Netty Channel Handler
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
	
	private final static String TAG = "IMMessage-ClientHandler";
	 
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object data)
            throws Exception {
    	if(data.equals("ok")) {//如果是服务端的心跳反馈包
    		Log.i(TAG, "===收到反馈包===");
		} else {
    		Log.i(TAG, data.toString());
    	}
        EventBus.getDefault().post(data);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	Log.i(TAG, "===与服务器成功建立连接===");
        super.channelActive(ctx);
        PushService.isConnSucc = true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	Log.i(TAG, "===与服务器断开连接===");
        super.channelInactive(ctx);
        PushService.isConnSucc = false;
    }
    
    @Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {//读超时
                ctx.channel().disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {//总超时
                ctx.writeAndFlush("ping");
            }/* else if (event.state() == IdleState.WRITER_IDLE) {//写超时
            	
            } */
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            ctx.writeAndFlush("ERR: "
                    + cause.getClass().getSimpleName() + ": "
                    + cause.getMessage() + '\n').addListener(ChannelFutureListener.CLOSE);
        }
    }
}