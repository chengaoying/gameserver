package cn.ohyeah.gameserver.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {

	private static final Log log = LogFactory.getLog(FileUtils.class);

	/**
	 * 文件转换成字节数组
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] FileToByteArray(File file) {
		FileInputStream stream = null;
		ByteArrayOutputStream out = null;
		try {
			stream = new FileInputStream(file);
			out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("文件" + file + "转换成字节数组失败，原因：" + e);
		} finally {
			try {
				out.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	/**
	 * 字节数组转换成文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
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
			log.error("字节数组转换成文件失败，原因：" + e);
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

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * 
	 * @param sourceFilePath  待压缩的文件路径
	 * @param zipFilePath 压缩后存放路径
	 * @param fileName 压缩后文件的名称
	 * @return flag
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath+ " 不存在. <<<<<<");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					System.out.println(">>>>>> " + zipFilePath + " 目录下存在名字为：" + fileName + ".zip" + " 打包文件. <<<<<<");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩. <<<<<<");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {

							// 创建ZIP实体,并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);

							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	/**
	 * 解压zip文件
	 * @param srcPath 源文件路径（包括文件名）
	 * @param targetPath 目标路径（解压之后文件存放路径）
	 */
	public static boolean unZip(String srcPath, String targetPath) {
		boolean flag = false;
		try {
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(srcPath));
			BufferedInputStream Bin = new BufferedInputStream(Zin);
			File Fout = null;
			ZipEntry entry;
			try {
				while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
					Fout = new File(targetPath, entry.getName());
					if (!Fout.exists()) {
						(new File(Fout.getParent())).mkdirs();
					}
					FileOutputStream out = new FileOutputStream(Fout);
					BufferedOutputStream Bout = new BufferedOutputStream(out);
					int b;
					while ((b = Bin.read()) != -1) {
						Bout.write(b);
					}
					Bout.close();
					out.close();
					//System.out.println(Fout + "解压成功");
					flag = true;
				}
				Bin.close();
				Zin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/*public static void main(String[] args) {
		String path = "E:\\test\\product";
		//fileToZip(path, path, "test");
		
		String path2 = "E:\\test\\product\\test.zip";
		String path3 = "E:\\";
		unZip(path2,path3);
	}*/
}
