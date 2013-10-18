package test;

import java.io.File;
import java.io.IOException;

import cn.ohyeah.gameserver.util.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		/*String str = "{\"message\":null," +
				"\"data\":[{\"activityid\":1,\"price\":321,\"productid\":11,\"name\":\"ggaf\",\"location\":\"F:/test/prize/正则表达式系统教程.CHM\"},{\"activityid\":1,\"price\":321,\"productid\":11,\"name\":\"ggaf\",\"location\":\"F:/test/prize/architect-201310.pdf\"}]" +
				",\"code\":0}";*/
		
		String s = "[{\"activityid\":1,\"price\":321,\"productid\":11,\"name\":\"ggaf\",\"location\":\"F:/test/prize/正则表达式系统教程.CHM\"},{\"activityid\":1,\"price\":321,\"productid\":11,\"name\":\"ggaf\",\"location\":\"F:/test/prize/architect-201310.pdf\"}]";
		/*ObjectMapper om = new ObjectMapper();
		JsonNode node = om.readValue(s, JsonNode.class);
		for(int i=0;i<node.size();i++){
			System.out.println(node.get(i).get("activityid"));
			System.out.println(node.get(i).get("price"));
			System.out.println(node.get(i).get("productid"));
			System.out.println(format(String.valueOf(node.get(i).get("name"))));
			System.out.println(format(String.valueOf(node.get(i).get("location"))));
		}*/
		File file = new File("F:/test/prize/正则表达式系统教程.CHM");
		byte[] bytes = FileUtils.FileToByteArray(file);
		System.out.println("字节数组大小==》"+bytes.length);
		FileUtils.bytesArrayToFile(bytes, "F:/test/test.CHM");
		File file2 = new File("F:/test/正则表达式系统教程.CHM");
		System.out.println(picName("F:/test/prize/正则表达式系统教程.CHM"));
		
	}

	private static String format(String str){
		if(str.startsWith("\"")){
			str = str.substring(1, str.length()-1);
		}
		return str;
	}
	
	private static String picName(String location){
		String[] ss = location.split("/");
		if(ss.length > 1){
			return ss[ss.length-1];
		}
		return "";
	}
	
}
