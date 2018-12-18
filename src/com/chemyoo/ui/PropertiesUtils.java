package com.chemyoo.ui;

import java.io.File;
import java.net.URL;
/** 
 * @author ���� : jianqing.liu
 * @version ����ʱ�䣺2018��6��25�� ����7:02:15 
 * @since 2018��6��25�� ����7:02:15 
 * @description �����ļ�(config.properties)���ݻ�ȡ������ģʽ
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
