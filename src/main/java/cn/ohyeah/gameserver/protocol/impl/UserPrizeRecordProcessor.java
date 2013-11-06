package cn.ohyeah.gameserver.protocol.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.message.MessageQueue;
import cn.ohyeah.gameserver.message.Notice;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.PrizeService;
import cn.ohyeah.gameserver.service.UserPrizeRecordService;
import cn.ohyeah.gameserver.service.UserService;
import cn.ohyeah.gameserver.util.BytesUtil;

public class UserPrizeRecordProcessor implements IProcessor {
	private static final Log log = LogFactory.getLog(UserPrizeRecordProcessor.class);
	
	private static final UserPrizeRecordService userPrizeRecordService;
	private static final UserService userService;
	private static final PrizeService prizeService;
	
	static{
		userPrizeRecordService = (UserPrizeRecordService)BeanManager.getBean("userPrizeRecordService");
		userService = (UserService)BeanManager.getBean("userService");
		prizeService = (PrizeService)BeanManager.getBean("prizeService");
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
		
		/**
		 * 如果code=0,往消息队列中添加信息，服务器检测到有信息就发送给客户端
		 */
		if(code == 0){
			Map<String,Object> userMap = userService.load(userid);
			Map<String,Object> prizeMap = prizeService.load(prizeid);
			log.info("中奖用户信息==>"+userMap);
			log.info("用户所中奖品信息==>"+prizeMap);
			String username = "";
			String prizename = "";
			String userdata = String.valueOf(userMap.get("data"));
			String prizedata = String.valueOf(prizeMap.get("data"));
			ObjectMapper om = new ObjectMapper();
			try {
				JsonNode node = om.readValue(format(userdata), JsonNode.class);
				username = String.valueOf(node.get("name"));
				JsonNode node2 = om.readValue(format(prizedata), JsonNode.class);
				prizename = String.valueOf(node2.get("name"));
			} catch (JsonParseException e) {
				//e.printStackTrace();
				log.error("json解析出错!");
			} catch (JsonMappingException e) {
				//e.printStackTrace();
				log.error("json映射出错!");
			} catch (IOException e) {
				//e.printStackTrace();
				log.error("io异常!");
			}
			String str = "恭喜用户" + format(username) + "获得奖品" + format(prizename) + "！";
			Notice notice = new Notice();
			notice.setContent(str);
			MessageQueue.noticeQueue.add(notice);
		}
	}
	
	private String format(String str){
		if(str.startsWith("[") || str.startsWith("\"")){
			str = str.substring(1, str.length()-1);
		}
		return str;
	}

}
