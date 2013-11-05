package cn.ohyeah.gameserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ohyeah.gameserver.global.BeanManager;
import cn.ohyeah.gameserver.global.Configurations;
import cn.ohyeah.gameserver.handlers.DefaultChannelInitalizer;
import cn.ohyeah.gameserver.message.TaskExecutor;

public class BootStrap {
	
	private static final Log log = LogFactory.getLog(BootStrap.class);
	
	private static final DefaultChannelInitalizer defaultChannelInitalizer;
	
	static{
		defaultChannelInitalizer = (DefaultChannelInitalizer)BeanManager.getBean("defaultChannelInitalizer");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void start(){
		log.info("game server start");
		EventLoopGroup bossGroup = bossGroup();   		//这个是用于serversocketchannel的eventloop  
        EventLoopGroup workerGroup = workerGroup();     //这个是用于处理accept到的channel  
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 .childHandler(defaultChannelInitalizer);
			Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
			Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
			for (ChannelOption option : keySet) {
				b.option(option, tcpChannelOptions.get(option));
			}
		
			new Thread(new TaskExecutor(5)).start();
			
			ChannelFuture future = b.bind(tcpPort()).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("gameserver start error:", e);
			throw new RuntimeException("gameserver start error:", e);
		}finally{
			bossGroup.shutdownGracefully();  
            workerGroup.shutdownGracefully(); 
		}
	}
	
	private NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(Integer.parseInt(Configurations.configs.getProperty("boss.thread.count")));
	}

	private NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(Integer.parseInt(Configurations.configs.getProperty("worker.thread.count")));
	}
	
	private InetSocketAddress tcpPort() {
		return new InetSocketAddress(Integer.parseInt(Configurations.configs.getProperty("tcp.port")));
	}

	private Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, Boolean.parseBoolean(Configurations.configs.getProperty("so.keepalive")));
		options.put(ChannelOption.SO_BACKLOG, Integer.parseInt(Configurations.configs.getProperty("so.backlog")));
		return options;
	}
	
}
