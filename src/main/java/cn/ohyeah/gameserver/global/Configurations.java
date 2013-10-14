package cn.ohyeah.gameserver.global;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import cn.ohyeah.gameserver.handlers.DefaultChannelInitalizer;

/**
 * @author jackey
 */
@Configuration
@ComponentScan("cn.ohyeah")
@PropertySource("classpath:configurations.properties")
public class Configurations {

	@Value("${boss.thread.count}")
	private int bossCount;

	@Value("${worker.thread.count}")
	private int workerCount;
	
	@Value("${group.thread.count}")
	private int groupCount;

	@Value("${tcp.port}")
	private int tcpPort;

	@Value("${so.keepalive}")
	private boolean keepAlive;

	@Value("${so.backlog}")
	private int backlog;
	
	@Value("${read_idle}")
	private int read_idle;
	
	@Value("${write_idle}")
	private int write_idle;

	@Value("${processor_base_package}")
	private String processorBasePackage;
	
	@Value("${remote.server}")
	private String remoteServer;
	
	@Value("${user_register_url}")
	private String registerUrl;
	
	@Value("${user_login_url_360}")
	private String loginUrl_360;
	
	@Value("${user_login_url_common}")
	private String loginUrl_common;

	@Autowired
	@Qualifier("defaultChannelInitializer")
	private DefaultChannelInitalizer defaultChannelInitalizer;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(defaultChannelInitalizer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossCount);
	}

	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerCount);
	}
	
	@Bean(name = "executorGroup", destroyMethod = "shutdownGracefully")
	public EventExecutorGroup eventExecutorGroup(){
		return new DefaultEventExecutorGroup(groupCount);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpPort() {
		return new InetSocketAddress(tcpPort);
	}

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}

	@Bean(name = "processorBasePackage")
	public String getProcessorBasePackage() {
		return processorBasePackage;
	}
	
	@Bean(name = "remoteServer")
	public String getRemoteServer() {
		return remoteServer;
	}
	
	@Bean(name = "registerUrl")
	public String getRegisterUrl(){
		return registerUrl;
	}
	
	@Bean(name = "loginUrl_360")
	public String getLoginUrl_360(){
		return loginUrl_360;
	}
	
	@Bean(name = "loginUrl_common")
	public String getLoginUrl_common(){
		return loginUrl_common;
	}
	
	@Bean(name = "httpClient")
	public DefaultHttpClient buildDefaultHttpClient(){
		return ThreadSafeConnectionManager.buildDefaultHttpsClient();
	}
	
	@Bean(name = "readIdle")
	public int getReadIdle(){
		return read_idle;
	}
	
	@Bean(name = "writeIdle")
	public int getWriteIdle(){
		return write_idle;
	}

	/**
	 * Necessary to make the Value annotations work.
	 * @return
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
