package cn.ohyeah.gameserver.message;

/**
 * 公告信息(服务器主动发送)
 * @author Jackey Chan
 *
 */
public class Notice {

	/**
	 * 公告内容
	 */
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
