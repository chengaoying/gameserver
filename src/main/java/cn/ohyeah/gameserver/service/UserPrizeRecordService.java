package cn.ohyeah.gameserver.service;

import java.util.Map;

import cn.ohyeah.gameserver.global.Configurations;

public class UserPrizeRecordService extends AbstractService{

	private static final String remoteServer;
	private static final String saveUrl;
	private static final String loadUrl;
	
	static{
		remoteServer = Configurations.configs.getProperty("remote.server");
		saveUrl = Configurations.configs.getProperty("save_user_prize_record");
		loadUrl = Configurations.configs.getProperty("load_user_prize_record");
	}
	
	public Map<String, Object> save(int userid, int prizeid){
		String url = String.format(remoteServer + saveUrl, userid, prizeid);
		Map<String, Object> map = remoteMethodInvoke(url, "save user prize record");
		return map;
	}
	
	public Map<String, Object> load(int userid){
		String url = String.format(remoteServer + loadUrl, userid);
		Map<String, Object> map = remoteMethodInvoke(url, "load user prize record");
		return map;
	}
}
