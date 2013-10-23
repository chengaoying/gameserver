package cn.ohyeah.gameserver.service;

import java.util.Map;

import cn.ohyeah.gameserver.global.Configurations;
import cn.ohyeah.gameserver.model.User;

public class UserService extends AbstractService{

	private static final String remoteServer;
	private static final String registerUrl;
	private static final String loginUrl_common;
	
	static{
		remoteServer = Configurations.configs.getProperty("remote.server");
		registerUrl = Configurations.configs.getProperty("user_register_url");
		loginUrl_common = Configurations.configs.getProperty("user_login_url_common");
	}
	
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
