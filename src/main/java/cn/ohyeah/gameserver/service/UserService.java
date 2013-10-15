package cn.ohyeah.gameserver.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ohyeah.gameserver.model.User;

@Service
public class UserService extends AbstractService{

	@Autowired
	private String remoteServer;
	
	@Autowired
	private String registerUrl;
	
	@Autowired
	private String loginUrl_common;
	
	public Map<String, Object> register(User user) {
		String url = String.format(remoteServer + registerUrl, user.getName(),user.getPassword());
		Map<String,Object> map = remoteMethodInvoke(url,"register");
		return map;
	}
	
	public Map<String, Object> login(User user) {
		String url = String.format(remoteServer + loginUrl_common, user.getName(),user.getPassword());
		Map<String,Object> map = remoteMethodInvoke(url,"login");
		return map;
	}
} 
