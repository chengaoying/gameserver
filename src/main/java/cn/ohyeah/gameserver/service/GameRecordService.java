package cn.ohyeah.gameserver.service;

import java.util.Map;

import cn.ohyeah.gameserver.global.Configurations;

public class GameRecordService extends AbstractService {

	private static final String remoteServer;
	private static final String save_game_record;
	private static final String load_game_record;
	
	static{
		remoteServer = Configurations.configs.getProperty("remote.server");
		save_game_record = Configurations.configs.getProperty("save_game_record");
		load_game_record = Configurations.configs.getProperty("load_game_record");
	}
	
	public Map<String, Object> saveGameRecord(int recordindex, String data){
		String url = String.format(remoteServer + save_game_record, recordindex,data);
		Map<String, Object> map = remoteMethodInvoke(url, "save game record");
		return map;
	}
	
	public Map<String, Object> loadGameRecord(int recordindex){
		String url = String.format(remoteServer + load_game_record, recordindex);
		Map<String, Object> map = remoteMethodInvoke(url, "load game record");
		return map;
	}
}
