package cn.ohyeah.gameserver.protocol.impl;

import javax.annotation.PostConstruct;
import io.netty.buffer.ByteBuf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.HeadWrapper;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.protocol.ProcessFrame;

@Service
public class DefaultProcessor {

	private static Log logger = LogFactory.getLog(DefaultProcessor.class);
	
	@Autowired
	private SystemProcessor systemProcessor;
	
	@Autowired
	private UserProcessor userProcessor;
	
	
	@PostConstruct
	public void initDefaultProcessor() {}

	public void process(ProcessFrame frame) {
		ByteBuf req = frame.getRequest();
		int head = req.readInt();
		HeadWrapper headWrapper = new HeadWrapper(head);
		ProcessContext context = new ProcessContext();
		context.setHead(headWrapper);
		context.setRequest(frame.getRequest());
		switch (headWrapper.getTag()) {
		case Constant.PROTOCOL_TAG_USER_SERV:
			userProcessor.process(context);
			frame.getChannel().writeAndFlush(context.getResponse());
			break;
		case Constant.PROTOCOL_TAG_SYS_SERV:
			systemProcessor.process(context);
			break;
		default:
			String errorInfo = Constant.getErrorInfo(Constant.EC_PROTOCOL_PROCESSOR_ERROR);
			logger.info(errorInfo);
			throw new ProtocolProcessException(errorInfo);
		}
	}

}
