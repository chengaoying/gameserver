package cn.ohyeah.gameserver.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameServer {
	private static final Log log = LogFactory.getLog(GameServer.class);
	public static void main(String[] args) {
		log.debug("Starting");
		BootStrap bootstrap = new BootStrap();
		bootstrap.start();
	}
}
