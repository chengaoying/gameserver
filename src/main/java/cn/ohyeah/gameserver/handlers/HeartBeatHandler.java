package cn.ohyeah.gameserver.handlers;

import org.springframework.stereotype.Component;
import io.netty.channel.ChannelHandler.Sharable;
import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.HeadWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
@Sharable
public class HeartBeatHandler extends ChannelDuplexHandler {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
		if (evt instanceof IdleStateEvent){
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				HeadWrapper head = new HeadWrapper.Builder().version(Constant.PROTOCOL_VERSION).type(1)
                .tag(Constant.PROTOCOL_TAG_SYS_SERV).command(Constant.SYS_SERV_BREAK_HREAT).build();
				ByteBuf buf = Unpooled.buffer(128);
				buf.writeInt(head.getHead());
				ctx.channel().writeAndFlush(buf);
			}
		}
	}
}


