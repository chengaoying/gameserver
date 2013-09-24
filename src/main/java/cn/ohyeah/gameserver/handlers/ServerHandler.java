package cn.ohyeah.gameserver.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.ohyeah.gameserver.server.ConnectionsManager;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Log logger = LogFactory.getLog(ServerHandler.class);
	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg)throws Exception {
		System.out.print(msg);
		//ctx.channel().writeAndFlush(msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("receive a connection");
		ConnectionsManager.getInstance().getChannelGroup().add(ctx.channel());
		ctx.channel().writeAndFlush("hello, "+ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel is disconnected");
		ctx.close();
		ConnectionsManager.getInstance().getChannelGroup().remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		System.out.println("server exception==>"+cause);
		ctx.close();
		ConnectionsManager.getInstance().getChannelGroup().remove(ctx.channel());
	}

}
