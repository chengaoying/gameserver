package cn.ohyeah.gameserver.protocol;

public class Constant {
	/**
	 * protocol version
	 */
	public static final short PROTOCOL_VERSION = 1;

	/**
	 * protocol tag
	 */
	public static final short PROTOCOL_TAG_SYS_SERV = 0; 			/* 系统服务协议 */
	public static final short PROTOCOL_TAG_USER_SERV = 1; 			/* 用户服务协议 */

	/**
	 * protocol command
	 */
	public static final short SYS_SERV_BREAK_HREAT = 0; 				/* 心跳命令 */

	public static final short USER_SERV_REGISTER = 11; 				/* 用户注册 */
	
	/**
	 * error code
	 */
	public static final short EC_PROTOCOL_PROCESSOR_ERROR = -1001;	/*协议处理异常*/	
	
	/**
	 * get error information
	 * @param errorCode
	 * @return
	 */
	public static String getErrorInfo(int errorCode){
		switch(errorCode){
		case EC_PROTOCOL_PROCESSOR_ERROR:
			return "协议处理异常";
		default:
			return "未知错误";
		}
	}

}
