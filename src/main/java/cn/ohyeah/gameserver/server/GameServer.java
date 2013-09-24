package cn.ohyeah.gameserver.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import cn.ohyeah.gameserver.cfg.SpringConfig;

public class GameServer {
	private static final Logger LOG = LoggerFactory.getLogger(GameServer.class);
	public static void main(String[] args) {
		LOG.debug("Starting application context");
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		ctx.registerShutdownHook();
	}

}
