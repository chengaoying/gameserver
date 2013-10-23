package cn.ohyeah.gameserver.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jackey
 */
public class Configurations {
	
	public static Properties configs;

	static{
		init();
	}
	
	private static void init() {
		configs = loadProperties("/configurations.properties");
	}

	private static Properties loadProperties(String path) {
		Properties props = new Properties();
		InputStream is = Configurations.class.getResourceAsStream(path);
		try {
			props.load(is);
			return props;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
