package cn.ohyeah.gameserver.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ohyeah.gameserver.global.ThreadSafeOfConnectionManager;
import cn.ohyeah.gameserver.model.UserInfo;

@Service
public class UserService {

	private static final Log log = LogFactory.getLog(UserService.class);
	
	@Autowired
	private DefaultHttpClient httpClient;
	
	@Autowired
	private String remoteServer;
	
	@Autowired
	private String registerUrl;
	
	public Map<String, Object> register(UserInfo user) {
		String url = String.format(remoteServer + registerUrl, user.getName(),user.getPassword());
		HttpGet httpGet = new HttpGet(url);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String body = ThreadSafeOfConnectionManager.executeForBodyString(httpClient, httpGet);
			log.debug("register response body==>" + body);
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readValue(body, JsonNode.class);
			map.put("code", node.get("code"));
			map.put("message", node.get("message"));
			map.put("data", node.get("data"));
			return map;
		} catch (JsonParseException e) {
			log.error("Json Parse Exception：", e);
			throw new RuntimeException("Json Parse Exception：", e);
		} catch (JsonMappingException e) {
			log.error("Json Mapping Exception：", e);
			throw new RuntimeException("Json Mapping Exception：", e);
		} catch (IOException e) {
			log.error("Read Data Exception：", e);
			throw new RuntimeException("Read Data Exception：", e);
		} catch (Exception e) {
			log.error("connection center server exception：", e);
			throw new RuntimeException("connection center server exception：", e);
		}

	}
} 
