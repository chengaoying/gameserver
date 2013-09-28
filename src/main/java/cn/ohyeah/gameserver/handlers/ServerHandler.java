package cn.ohyeah.gameserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.ohyeah.gameserver.protocol.ProcessFrame;
import cn.ohyeah.gameserver.protocol.impl.DefaultProcessor;
import cn.ohyeah.gameserver.server.ConnectionsManager;

@Component
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	private static final Log logger = LogFactory.getLog(ServerHandler.class);

	@Autowired
	private DefaultProcessor defaultProcessor;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		ProcessFrame frame = new ProcessFrame();
		frame.setRequest((ByteBuf)msg);
		frame.setChannel(ctx.channel());
		defaultProcessor.process(frame);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("receive a connection:"+ctx.channel());
		ConnectionsManager.getInstance().getChannelGroup().add(ctx.channel());
		/*for(Channel ch:ConnectionsManager.getInstance().getChannelGroup()){
			ch.writeAndFlush("hello, "+ch);
		}*/
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
		ConnectionsManager.getInstance().getChannelGroup().remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		logger.debug("error ", cause);
		ctx.close();
		ConnectionsManager.getInstance().getChannelGroup().remove(ctx.channel());
	}

}
