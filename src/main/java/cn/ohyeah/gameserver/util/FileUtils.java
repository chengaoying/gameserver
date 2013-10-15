package cn.ohyeah.gameserver.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {

	private static final Log log = LogFactory.getLog(FileUtils.class);
	
	public static byte[] FileToByteArray(File file) {
		FileInputStream stream = null;
		ByteArrayOutputStream out = null;
		try{
			stream = new FileInputStream(file);
			out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
		}catch(Exception e){
			//e.printStackTrace();
			log.error("文件"+file+"转换成字节数组失败，原因："+e);
		}finally{
			try {
				out.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}
	
	public static File bytesArrayToFile(byte[] b, String outputFile) {  
        File ret = null;  
        BufferedOutputStream stream = null;  
        try {  
            ret = new File(outputFile);  
            FileOutputStream fstream = new FileOutputStream(ret);  
            stream = new BufferedOutputStream(fstream);  
            stream.write(b);  
        } catch (Exception e) {  
            // log.error("helper:get file from byte process error!");  
        	log.error("字节数组转换成文件失败，原因："+e);
        } finally {  
            if (stream != null) {  
                try {  
                    stream.close();  
                } catch (IOException e) {  
                    // log.error("helper:get file from byte process error!");  
                    e.printStackTrace();  
                }  
            }  
        }  
        return ret;  
    }  
}
