package cn.ohyeah.gameserver.protocol.impl;

import org.springframework.stereotype.Service;

import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;

@Service
public class SystemProcessor implements IProcessor {

	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.SYS_SERV_BREAK_HREAT:
			//心跳包无须处理
			System.out.println("接受心跳包");
			break;
		default:
			break;
		}
	}

}
