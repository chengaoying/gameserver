package cn.ohyeah.gameserver.handlers;

import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.Configurations;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Just a dummy protocol mainly to show the ServerBootstrap being initialized.
 * @author Jackey Chan
 */
public class DefaultChannelInitalizer extends ChannelInitializer<SocketChannel> {

	private static final ServerHandler serverHandler;
	private static final HeartBeatHandler heartBeatHandler;
	private static final int readIdle;
	private static final int writeIdle;
	private static final EventExecutorGroup executorGroup;
	
	static{
		serverHandler = (ServerHandler)BeanManager.getBean("serverHandler");
		heartBeatHandler = (HeartBeatHandler)BeanManager.getBean("heartBeatHandler");
		readIdle = Integer.parseInt(Configurations.configs.getProperty("read_idle"));
		writeIdle = Integer.parseInt(Configurations.configs.getProperty("write_idle"));
		
		executorGroup = new DefaultEventExecutorGroup(Integer.parseInt(Configurations.configs.getProperty("group.thread.count")));
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//pipeline.addLast("decoder", stringDecoder);
		//pipeline.addLast("encoder", stringEncoder);
		pipeline.addLast(executorGroup,"idleStateHandler", new IdleStateHandler(readIdle, writeIdle, 0));
		pipeline.addLast(executorGroup,"heartBeatHandler", heartBeatHandler);
		pipeline.addLast(executorGroup,"handler", serverHandler);
	}

}
