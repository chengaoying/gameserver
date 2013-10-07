package cn.ohyeah.gameserver.protocol.impl;

import java.util.Map;

import io.netty.buffer.ByteBuf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ohyeah.gameserver.model.User;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.UserService;
import cn.ohyeah.gameserver.util.BytesUtil;

@Service
public class UserProcessor implements IProcessor {

	@Autowired
	private UserService userService;
	
	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.USER_SERV_REGISTER:
			userRegister(context);
			break;
		case Constant.USER_SERV_LOGIN:
			userLogin(context);
			break;
		default:
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
