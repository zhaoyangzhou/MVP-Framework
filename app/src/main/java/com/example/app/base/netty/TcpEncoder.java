package com.example.app.base.netty;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Package: com.example.app.base.netty
 * Class: TcpEncoder
 * Description: 发送数据解析器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class TcpEncoder extends MessageToByteEncoder<ArrayList> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ArrayList pack, ByteBuf out)
			throws Exception {
		if(pack == null || pack.size() == 0) {
			return;
		}
		
		for(int i = 0, len = pack.size(); i < len; i++) {
			if(pack.get(i) instanceof Integer) {
				out.writeInt((Integer)pack.get(i));
			} else if(pack.get(i) instanceof Short) {
				out.writeShort((Short)pack.get(i));
			} else if(pack.get(i) instanceof Character) {
				out.writeChar((Character)pack.get(i));
			} else if(pack.get(i) instanceof Byte) {
				out.writeByte((Byte)pack.get(i));
			} else if(pack.get(i) instanceof byte[]) {
				out.writeBytes((byte[])pack.get(i));
			} 
		}
	}
}
