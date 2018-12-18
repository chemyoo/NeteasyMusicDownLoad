package com.chemyoo.ui;

import java.io.File;
import java.net.URL;
/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年6月25日 下午7:02:15 
 * @since 2018年6月25日 下午7:02:15 
 * @description 配置文件(config.properties)内容获取，单例模式
 */
public class PropertiesUtils {
	private PropertiesUtils() {}
	
	/**
	 * the folder which contains the java class.
	 * @return
	 */
	public static String getClassFolder() {
		ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
		URL resourceUrl = classLoader.getResource("/");
		if (resourceUrl == null) {
			resourceUrl = classLoader.getResource("");
		}
		return resourceUrl.getPath();
	}

	public static String getWorkFolder() {
		String path = System.getProperty("catalina.home", System.getProperty("user.dir"));
		File file = new File(path);
		File parent = file.getParentFile();
		if(parent != null) {
			path = parent.getAbsolutePath();
		} 
		return path;
	}

	/**
	 * System line separator.
	 * @return {@code /r} in Unix, {@code /n} in Windows.
	 */
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}
	/**
	 * System File separator.
	 * @return {@code /} in Unix, {@code \} in Windows.
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
}
