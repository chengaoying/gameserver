package cn.ohyeah.gameserver.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Just a dummy protocol mainly to show the ServerBootstrap being initialized.
 * @author Jackey Chan
 * 
 */
@Component
@Qualifier("defaultChannelInitializer")
public class DefaultChannelInitalizer extends ChannelInitializer<SocketChannel> {

	@Autowired
	ServerHandler serverHandler;
	
	@Autowired
	HeartBeatHandler heartBeatHandler;
	
	@Autowired
	int readIdle;
	
	@Autowired
	int writeIdle;
	
	@Autowired
	EventExecutorGroup executorGroup;
	
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
