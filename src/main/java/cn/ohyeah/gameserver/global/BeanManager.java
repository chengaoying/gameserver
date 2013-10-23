package cn.ohyeah.gameserver.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanManager {
	private static Map<String, Object> beans = new HashMap<String, Object>();
	
	static {
		initBeans();
	}

	private static void initBeans() {
		parseDocument("/beans.xml");
	}
	
	@SuppressWarnings("unchecked")
	private static void parseDocument(String cfgPath) {
		SAXReader reader = new SAXReader(); 
		InputStream is = BeanManager.class.getResourceAsStream(cfgPath);
		String name = null;
		String value = null;
		try {
			Document document = reader.read(is);
			Element root = document.getRootElement(); 
			Iterator<Element> it = (Iterator<Element>)root.elementIterator();
			while (it.hasNext()) {
				Element e = (Element) it.next(); 
				if ("bean".equalsIgnoreCase(e.getName())) {
					name = e.attributeValue("name");
					value = e.attributeValue("class");
					beans.put(name, Class.forName(value).newInstance());
				}
			}
		} catch (Throwable e) {
			throw new RuntimeException("初始化bean出错?name="+name+", value="+value, e);
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public static Object getBean(String name) {
		Object obj = beans.get(name);
		if (obj == null) {
			throw new RuntimeException("未知Bean, name="+name);
		}
		return obj;
	}
	
	public static void setBean(String name, Object value) {
		beans.put(name, value);
	}
	
}
