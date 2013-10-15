package cn.ohyeah.gameserver.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrizeService extends AbstractService{
	
	@Autowired
	private String remoteServer;
	
	@Autowired
	private String load_prizes_url;

	public Map<String, Object> loadPrizes(int activityid){
		String url = String.format(remoteServer + load_prizes_url, activityid);
		Map<String, Object> map = remoteMethodInvoke(url, "loadPrizes");
		return map;
	}
}
