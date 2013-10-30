package cn.ohyeah.gameserver.protocol.impl;

import io.netty.buffer.ByteBuf;
import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.util.BytesUtil;

public class SystemProcessor implements IProcessor {

	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.SYS_SERV_BREAK_HREAT:
			//心跳包无须处理
			System.out.println("接收心跳包");
			break;
		default:
			ByteBuf rsp = context.createResponse(256);
			rsp.writeInt(context.getHead().getHead());
			rsp.writeInt(ErrorCode.EC_PROTOCOL_PROCESSOR_ERROR);
			BytesUtil.writeString(rsp, ErrorCode.getErrorMsg(ErrorCode.EC_PROTOCOL_PROCESSOR_ERROR)+":"+context.getHead().getCommand());
			BytesUtil.writeString(rsp, "null");
			context.getChannel().writeAndFlush(context.getResponse());
			break;
		}
	}

}
