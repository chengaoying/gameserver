package cn.ohyeah.gameserver.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ConnectionsManager {
	
	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);;

	private static ConnectionsManager connectionsManager;
	
	public static ConnectionsManager getInstance(){
		if(connectionsManager == null){
			return new ConnectionsManager();
		}
		return connectionsManager;
	}
	
	private ConnectionsManager(){};
	
	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}
	
	public void close(){
		channelGroup.close();
	}
	
	public void closeExpire(){
	}
	
}
