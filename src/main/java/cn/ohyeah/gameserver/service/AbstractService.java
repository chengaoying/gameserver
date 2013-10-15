package cn.ohyeah.gameserver.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ohyeah.gameserver.global.ErrorCode;
import cn.ohyeah.gameserver.global.ThreadSafeConnectionManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractService {
	
private static final Log log = LogFactory.getLog(AbstractService.class);
	
	@Autowired
	private DefaultHttpClient httpClient;

	public Map<String, Object>  remoteMethodInvoke(String url, String info){
		log.info("请求的地址==>"+url);
		HttpGet httpGet = new HttpGet(url);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String body = ThreadSafeConnectionManager.executeForBodyString(httpClient, httpGet);
			log.debug(info + " response body==>" + body);
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readValue(body, JsonNode.class);
			map.put("code", node.get("code"));
			map.put("message", node.get("message"));
			map.put("data", node.get("data"));
			return map;
		} catch (JsonParseException e) {
			log.error("Json Parse Exception：", e);
			map.put("code", ErrorCode.EC_PARSE_JSON_ERROR);
			map.put("message", ErrorCode.getErrorMsg(ErrorCode.EC_PARSE_JSON_ERROR));
			map.put("data", "null");
			return map;
		} catch (JsonMappingException e) {
			log.error("Json Mapping Exception：", e);
			map.put("code", ErrorCode.EC_PARSE_JSON_ERROR);
			map.put("message", ErrorCode.getErrorMsg(ErrorCode.EC_PARSE_JSON_ERROR));
			map.put("data", "null");
			return map;
		} catch (IOException e) {
			log.error("Read Data Exception：", e);
			map.put("code", ErrorCode.EC_CENTER_SERVER_ERROR);
			map.put("message", ErrorCode.getErrorMsg(ErrorCode.EC_CENTER_SERVER_ERROR));
			map.put("data", "null");
			return map;
		} catch (Exception e) {
			log.error("connection center server exception：", e);
			map.put("code", ErrorCode.EC_CENTER_SERVER_ERROR);
			map.put("message", e.getMessage());
			map.put("data", "null");
			return map;
		}
	}
}
