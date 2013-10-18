package cn.ohyeah.gameserver.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import cn.ohyeah.gameserver.global.Configurations;

public class GameServer {
	private static final Log log = LogFactory.getLog(GameServer.class);
	public static void main(String[] args) {
		log.debug("Starting application context");
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Configurations.class);
		ctx.registerShutdownHook();
	}
}
