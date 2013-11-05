package cn.ohyeah.gameserver.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ohyeah.gameserver.protocol.Constant;
import cn.ohyeah.gameserver.protocol.HeadWrapper;
import cn.ohyeah.gameserver.server.ConnectionsManager;
import cn.ohyeah.gameserver.util.BytesUtil;

/**
 * 服务器主动推送通知给客户端，另起线程池来负责该任务
 * @author Administrator
 *
 */
public class TaskExecutor implements Runnable{

	private final ExecutorService executorService ;
	
	public TaskExecutor(int poolSize){
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void run() {
		
		while(true){
			if(MessageQueue.hasNotice()){
				for(Channel ch:ConnectionsManager.getInstance().getChannelGroup()){
					HeadWrapper head = new HeadWrapper.Builder().version(Constant.PROTOCOL_VERSION).type(1)
	                .tag(Constant.PROTOCOL_TAG_SYS_SERV).command(Constant.SYS_SERV_NOTICE).build();
					ByteBuf buf = Unpooled.buffer(256);
					buf.writeInt(head.getHead());
					Notice notice = MessageQueue.noticeQueue.poll();
					System.out.println(notice);
					BytesUtil.writeString(buf, notice.getContent());
					ch.writeAndFlush(buf);
					System.out.println("服务器发送了通知");
				}
			}
		}
		
		/*try {
			for (;;) {
				executorService.execute(new Runnable() {
					
					@Override
					public void run() {
						if(MessageQueue.hasNotice()){
							for(Channel ch:ConnectionsManager.getInstance().getChannelGroup()){
								HeadWrapper head = new HeadWrapper.Builder().version(Constant.PROTOCOL_VERSION).type(1)
				                .tag(Constant.PROTOCOL_TAG_SYS_SERV).command(Constant.SYS_SERV_NOTICE).build();
								ByteBuf buf = Unpooled.buffer(256);
								buf.writeInt(head.getHead());
								Notice notice = MessageQueue.noticeQueue.poll();
								System.out.println(notice);
								BytesUtil.writeString(buf, notice.getContent());
								ch.writeAndFlush(buf);
								System.out.println("服务器发送了通知");
							}
						}
					}
				});
			}
		} catch (Exception ex) {
			executorService.shutdown();
		}*/
	}
}
