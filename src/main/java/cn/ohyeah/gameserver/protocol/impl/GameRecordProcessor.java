package cn.ohyeah.gameserver.protocol.impl;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.GameRecordService;
import cn.ohyeah.gameserver.util.BytesUtil;

public class GameRecordProcessor implements IProcessor {

	private static final GameRecordService gameRecordService;
	
	static{
		gameRecordService = (GameRecordService)BeanManager.getBean("gameRecordService");
	}
	
	@Override
	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.RECORD_SERV_SAVE:
			saveGameRecord(context);
			break;
		case Constant.RECORD_SERV_LOAD:
			loadGameRecord(context);
			break;
		default:
			throw new ProtocolProcessException(ErrorCode.getErrorMsg(ErrorCode.EC_PROTOCOL_PROCESSOR_ERROR));
		}
	}

	private void loadGameRecord(ProcessContext context) {
		ByteBuf req = context.getRequest();
		int recordindex = req.readInt();
		Map<String, Object> map = gameRecordService.loadGameRecord(recordindex);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		ByteBuf rsp = context.createResponse(256 + data.length());
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}

	private void saveGameRecord(ProcessContext context) {
		ByteBuf req = context.getRequest();
		ByteBuf rsp = context.createResponse(256);
		int recordindex = req.readInt();
		String record = BytesUtil.readString(req);
		Map<String, Object> map = gameRecordService.saveGameRecord(recordindex, record);
		int code = Integer.parseInt(String.valueOf(map.get("code")));
		String message = String.valueOf(map.get("message"));
		String data = String.valueOf(map.get("data"));
		rsp.writeInt(context.getHead().getHead());
		rsp.writeInt(code);
		BytesUtil.writeString(rsp, message);
		BytesUtil.writeString(rsp, data);
	}

}
