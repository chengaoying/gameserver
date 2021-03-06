package cn.ohyeah.gameserver.protocol;

public class Constant {
	/**
	 * protocol version
	 */
	public static final short PROTOCOL_VERSION = 1;

	/**
	 * protocol tag
	 */
	public static final short PROTOCOL_TAG_SYS_SERV = 0; 					/* 系统服务协议 */
	public static final short PROTOCOL_TAG_USER_SERV = 1; 					/* 用户服务协议 */
	public static final short PROTOCOL_TAG_PRIZE_SERV = 2; 					/* 奖品服务协议 */
	public static final short PROTOCOL_TAG_RECORD_SERV = 3;					/* 游戏记录服务 */
	public static final short PROTOCOL_TAG_USER_PRIZE_RECORD = 4;			/* 用户中奖记录 */

	/**
	 * protocol command
	 */
	public static final short SYS_SERV_BREAK_HREAT = 1; 					/* 心跳命令 */
	public static final short SYS_SERV_NOTICE = 2;							/* 通知 */

	public static final short USER_SERV_REGISTER = 11; 						/* 用户注册 */
	public static final short USER_SERV_LOGIN = 12; 						/* 用户登入 */
	
	public static final short PRIZE_SERV_LOAD_INFO = 21; 					/* 获取奖品信息*/
	public static final short PRIZE_SERV_LOAD_RES = 22; 					/* 获取奖品图片资源*/
	
	public static final short RECORD_SERV_SAVE = 31;						/* 保存游戏 记录*/
	public static final short RECORD_SERV_LOAD = 32;						/* 读取游戏记录 */
	
	public static final short USER_PRIZE_RECORD_SAVE = 41;					/* 保存用户中奖记录 */
	public static final short USER_PRIZE_RECORD_LOAD = 42;					/* 读取用户中奖记录 */
	
}
