package cn.ohyeah.gameserver.service;

import java.util.Map;

import cn.ohyeah.gameserver.global.Configurations;


public class PrizeService extends AbstractService{
	
	private static final String remoteServer;
	private static final String load_prizes_url;
	private static final String load_prize_url;
	
	static{
		remoteServer = Configurations.configs.getProperty("remote.server");
		load_prizes_url = Configurations.configs.getProperty("load_prizes_url");
		load_prize_url = Configurations.configs.getProperty("load_prize_url");
	}

	public Map<String, Object> loadPrizes(int activityid){
		String url = String.format(remoteServer + load_prizes_url, activityid);
		Map<String, Object> map = remoteMethodInvoke(url, "loadPrizes");
		return map;
	}
	
	public Map<String, Object> load(int prizeid){
		String url = String.format(remoteServer + load_prize_url, prizeid);
		Map<String, Object> map = remoteMethodInvoke(url, "load");
		return map;
	}
}
