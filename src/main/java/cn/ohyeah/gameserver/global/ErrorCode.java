package cn.ohyeah.gameserver.global;

public class ErrorCode {
	
	public static final int EC_UNKNOW_ERROE = -10000;			//服务器未知错误
	public static final int EC_CENTER_SERVER_ERROR = -10001;	//连接中央服务器错误
	public static final int EC_PARSE_JSON_ERROR = -10002;		//解析json数据错误

	public static String getErrorMsg(int errorCode){
		switch (errorCode) {
		case EC_UNKNOW_ERROE:
			return "服务器未知错误";
		case EC_CENTER_SERVER_ERROR:
			return "连接中央服务器错误";
		case EC_PARSE_JSON_ERROR:
			return "解析json数据错误";
		default:
			return "未知错误";
		}
	}
}
