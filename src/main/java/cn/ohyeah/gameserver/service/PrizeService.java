package cn.ohyeah.gameserver.service;

import java.util.Map;

import cn.ohyeah.gameserver.global.Configurations;


public class PrizeService extends AbstractService{
	
	private static final String remoteServer;
	private static final String load_prizes_url;
	
	static{
		remoteServer = Configurations.configs.getProperty("remote.server");
		load_prizes_url = Configurations.configs.getProperty("load_prizes_url");
	}

	public Map<String, Object> loadPrizes(int activityid){
		String url = String.format(remoteServer + load_prizes_url, activityid);
		Map<String, Object> map = remoteMethodInvoke(url, "loadPrizes");
		return map;
	}
}
