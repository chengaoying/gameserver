package cn.ohyeah.gameserver.message;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 消息队列
 * @author Administrator
 *
 */
public class MessageQueue {
	
	public static final Queue<Notice> noticeQueue = new LinkedList<Notice>();
	
	/**
	 * 是否用公告
	 * @return
	 */
	public static boolean hasNotice(){
		return !noticeQueue.isEmpty();
	}
}
