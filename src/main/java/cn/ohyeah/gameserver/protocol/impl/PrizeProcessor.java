package cn.ohyeah.gameserver.protocol.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.handler.stream.ChunkedFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.IProcessor;
import cn.ohyeah.gameserver.protocol.ProcessContext;
import cn.ohyeah.gameserver.service.PrizeService;
import cn.ohyeah.gameserver.util.BytesUtil;

public class PrizeProcessor implements IProcessor {

	private static final PrizeService prizeService;
	
	static{
		prizeService = (PrizeService)BeanManager.getBean("prizeService");
	}
	
	@Override
	public void process(ProcessContext context) {
		switch (context.getHead().getCommand()) {
		case Constant.PRIZE_SERV_LOAD_INFO:
			loadPrizes(context);
			context.getChannel().writeAndFlush(context.getResponse());
			break;
		case Constant.PRIZE_SERV_LOAD_RES:
			loadPrizesRes(context);
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

	private void loadPrizesRes(ProcessContext context){
		if(Constant.res.size() > 1){
			for(int i=0;i<Constant.res.size();i++){
				try {
					context.getChannel().write(new ChunkedFile(new File(Constant.res.get(i))));
					System.out.println("文件==>"+Constant.res.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadPrizes(ProcessContext context) {
		try {
			ByteBuf req = context.getRequest();
			int activityid = req.readInt();
			Map<String, Object> map = prizeService.loadPrizes(activityid);
			int code = Integer.parseInt(String.valueOf(map.get("code")));
			String message = String.valueOf(map.get("message"));
			String data = String.valueOf(map.get("data"));
			ByteBuf rsp = context.createResponse(1024*2);
			rsp.writeInt(context.getHead().getHead());
			rsp.writeInt(code);
			BytesUtil.writeString(rsp, message);
			//BytesUtil.writeString(rsp, data);
			if(data == null || data.equals("null")){
				rsp.writeInt(-1);
				return;
			}
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readValue(data, JsonNode.class);
			rsp.writeInt(node.size());
			for(int i=0;i<node.size();i++){
				BytesUtil.writeString(rsp, String.valueOf(node.get(i).get("activityid")));
				BytesUtil.writeString(rsp, String.valueOf(node.get(i).get("price")));
				BytesUtil.writeString(rsp, String.valueOf(node.get(i).get("productid")));
				BytesUtil.writeString(rsp, format(String.valueOf(node.get(i).get("name"))));
				String location = format(String.valueOf(node.get(i).get("location")));
				BytesUtil.writeString(rsp, picName(location));
				Constant.res.add(location);
				//byte[] bytes = FileUtils.FileToByteArray(new File(location));
				//System.out.println("文件大小==>"+bytes.length);
				//rsp.writeBytes(bytes);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String format(String str){
		if(str.startsWith("\"")){
			str = str.substring(1, str.length()-1);
		}
		return str;
	}
	
	private static String picName(String location){
		String[] ss = location.split("/");
		if(ss.length > 1){
			return ss[ss.length-1];
		}
		return "";
	}
	
}
