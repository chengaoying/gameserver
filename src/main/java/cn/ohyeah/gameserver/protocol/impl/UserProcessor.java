package cn.ohyeah.gameserver.protocol.impl;

import java.util.Map;

import io.netty.buffer.ByteBuf;

import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.model.User;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.UserService;
import cn.ohyeah.gameserver.util.BytesUtil;

public class UserProcessor implements IProcessor {

	private static final UserService userService;
	
	static{
		userService = (UserService)BeanManager.getBean("userService");
	}
	
	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.USER_SERV_REGISTER:
			userRegister(context);
			break;
		case Constant.USER_SERV_LOGIN:
			userLogin(context);
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

	private void userLogin(ProcessContext context) {
		ByteBuf req = context.getRequest();
		ByteBuf rsp = context.createResponse(256);
		User user = new User();
		user.read(req);
		Map<String, Object> map = userService.login(user);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}

	private void userRegister(ProcessContext context) {
		ByteBuf req = context.getRequest();
		ByteBuf rsp = context.createResponse(256);
		User user = new User();
		user.read(req);
		Map<String, Object> map = userService.register(user);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}
}
