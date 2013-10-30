package cn.ohyeah.gameserver.protocol.impl;

import javax.annotation.PostConstruct;
import io.netty.buffer.ByteBuf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.HeadWrapper;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.protocol.ProcessFrame;
import cn.ohyeah.gameserver.util.BytesUtil;

public class DefaultProcessor {

	private static Log logger = LogFactory.getLog(DefaultProcessor.class);
	
	private static final SystemProcessor systemProcessor;
	private static final UserProcessor userProcessor;
	private static final PrizeProcessor prizeProcessor;
	private static final GameRecordProcessor gameRecordProcessor;
	
	static{
		systemProcessor = (SystemProcessor)BeanManager.getBean("systemProcessor");
		userProcessor = (UserProcessor)BeanManager.getBean("userProcessor");
		prizeProcessor = (PrizeProcessor)BeanManager.getBean("prizeProcessor");
		gameRecordProcessor = (GameRecordProcessor)BeanManager.getBean("gameRecordProcessor");
	}
	
	@PostConstruct
	public void initDefaultProcessor() {}

	public void process(ProcessFrame frame) {
		ByteBuf req = frame.getRequest();
		int head = req.readInt();
		HeadWrapper headWrapper = new HeadWrapper(head);
		ProcessContext context = new ProcessContext();
		context.setHead(headWrapper);
		context.setRequest(frame.getRequest());
		context.setChannel(frame.getChannel());
		switch (headWrapper.getTag()) {
		case Constant.PROTOCOL_TAG_USER_SERV:
			userProcessor.process(context);
			frame.getChannel().writeAndFlush(context.getResponse());
			break;
		case Constant.PROTOCOL_TAG_SYS_SERV:
			systemProcessor.process(context);
			break;
		case Constant.PROTOCOL_TAG_PRIZE_SERV:
			prizeProcessor.process(context);
			//frame.getChannel().writeAndFlush(context.getResponse());
			break;
		case Constant.PROTOCOL_TAG_RECORD_SERV:
			gameRecordProcessor.process(context);
			frame.getChannel().writeAndFlush(context.getResponse());
			break;
		default:
			String errorInfo = ErrorCode.getErrorMsg(ErrorCode.EC_PROTOCOL_PROCESSOR_ERROR);
			logger.info(errorInfo);
			ByteBuf rsp = context.createResponse(256);
			rsp.writeInt(context.getHead().getHead());
			rsp.writeInt(ErrorCode.EC_PROTOCOL_PROCESSOR_ERROR);
			BytesUtil.writeString(rsp, errorInfo);
			BytesUtil.writeString(rsp, "null");
			frame.getChannel().writeAndFlush(context.getResponse());
			break;
		}
	}

}
