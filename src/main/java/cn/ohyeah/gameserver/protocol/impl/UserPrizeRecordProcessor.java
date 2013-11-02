package cn.ohyeah.gameserver.protocol.impl;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.UserPrizeRecordService;
import cn.ohyeah.gameserver.util.BytesUtil;

public class UserPrizeRecordProcessor implements IProcessor {
	
	private static final UserPrizeRecordService userPrizeRecordService;
	
	static{
		userPrizeRecordService = (UserPrizeRecordService)BeanManager.getBean("userPrizeRecordService");
	}

	@Override
	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.USER_PRIZE_RECORD_SAVE:
			saveUserPrize(context);
			break;
		case Constant.USER_PRIZE_RECORD_LOAD:
			loadUserPrize(context);
			break;
		default:
			break;
		}
	}

	private void loadUserPrize(ProcessContext context) {
		ByteBuf req = context.getRequest();
		ByteBuf rsp = context.createResponse(256);
		int userid = req.readInt();
		Map<String, Object> map = userPrizeRecordService.load(userid);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}

	private void saveUserPrize(ProcessContext context) {
		ByteBuf req = context.getRequest();
		ByteBuf rsp = context.createResponse(256);
		int userid = req.readInt();
		int prizeid = req.readInt();
		Map<String, Object> map = userPrizeRecordService.save(userid, prizeid);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}

}
