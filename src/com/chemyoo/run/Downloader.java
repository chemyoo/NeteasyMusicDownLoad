package com.chemyoo.run;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JLabel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import com.chemyoo.ui.PropertiesUtils;


/** 
 * @author Author : jianqing.liu
 * @version version : created time：2018年12月18日 下午1:39:52 
 * @since since from 2018年12月18日 下午1:39:52 to now.
 * @description class description
 */
public class Downloader extends Thread {
	
	private String fileName;
	
	private String musicId;
	
	private String musicUrl;
	
	private String savePath;
	
	private JLabel message;
	
	private static final String API_URL = "http://music.163.com/song/media/outer/url?id=${id}.mp3";
	protected static Random random = new Random();
	
	// 公平信号量，信号量至少为一个
	private final Semaphore semaphore = new Semaphore(1, true);
	
	private JButton excuting;
	
	private Logger logger = Logger.getLogger(Downloader.class.getName());
	
	public Downloader(String musicUrl, String fileName, String savePath, JLabel message, JButton excuting) {
		this.fileName = fileName;
		this.savePath = savePath;
		this.message = message;
		String[] split = musicUrl.split("[?]");
		if(split.length > 1 && musicUrl.toLowerCase().contains("id")) {
			String[] params = split[1].split("&");
			for(String s : params) {
				if(s.startsWith("id") || s.startsWith("ID")) {
					this.musicId = s.substring(s.toLowerCase().indexOf("id=") + 3);
				}
			}
		}
		this.musicUrl = musicUrl;
		this.excuting = excuting;
	}
	
	private static boolean isNotBlank(String...args) {
		for(String arg : args) {
			if(arg == null || "".equals(arg.trim())) {
				return false;
			}
		}
		return true;
	}


	@Override
	public void run() {
		String url = musicUrl;
		if(musicId != null) {
			url = API_URL.replace("${id}", musicId);
			if(!isNotBlank(fileName)) fileName = musicId;
		} else {
			fileName = url.substring(url.replace('\\', '/').lastIndexOf('/') + 1);
		}
		String ext = this.getFileExt(url);
		if(ext == null || ext.length() > 5) {
			this.setMessage(Color.RED, "此链接无法下载...");
			this.setEnabled();
			return;
		} 
		
		File file = new File(savePath + PropertiesUtils.getFileSeparator() + getFileName(ext));
		File parent = file.getParentFile();
		
		if(!parent.exists()) parent.mkdirs();
		
		InputStream in = null;
		HttpURLConnection httpConnection = null;
		try (FileOutputStream write = new FileOutputStream(file)){
			semaphore.acquire();
			String userAgent = new String []{"Mozilla/4.0","Mozilla/5.0","Opera/9.80"}[random.nextInt(3)];
			URL uri = new URL(url);
			httpConnection = (HttpURLConnection) uri.openConnection();
			
			// 设置连接主机超时（单位：毫秒）  
			httpConnection.setConnectTimeout(60 * 1000);
			// 设置从主机读取数据超时（单位：毫秒） 
			httpConnection.setReadTimeout(90 * 1000);
			
			httpConnection.setRequestProperty("User-agent", userAgent);
			
			// 连接网站
			httpConnection.connect();
			
			// handle what can not connect the url.
			if(httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				in = httpConnection.getErrorStream();
				if(in == null)
					in = httpConnection.getInputStream();
				
				logger.error("网址：" + url + "访问失败：" 
						+ IOUtils.toString(in, "gb2312"));
				throw new IllegalAccessError("网址连接失败...");
			} else {
				in = httpConnection.getInputStream();
				long available = httpConnection.getContentLengthLong();
				String size = String.format("文件大小%.2fMB，", available * 1F / (1024 * 1024));
				long count = 0;
				message.setForeground(Color.BLUE);
				byte [] bytes = new byte[in.available()];
				message.setVisible(true);
				while (in.read(bytes) != -1) {
					write.write(bytes);
					count += bytes.length;
					bytes = new byte[in.available()];
					message.setText(size + "正在下载：" +(count * 100 / available) + "%");
				}
				message.setText(size + "下载完成：100%");
			}
		} catch (Exception e) {
			this.setMessage(Color.RED, "下载失败，详情请查看/logs下的日志。");
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
			in = null;
			if(httpConnection != null)
				httpConnection.disconnect();
			semaphore.release();
			this.setEnabled();
		}
		//https://music.163.com/song?id=30064263&userid=135693455
	}
	
	private String getFileExt(String path) {
		String name = path.substring(path.replace('\\', '/').lastIndexOf('/') + 1);
		if(!name.contains(".")) return null;
		return name.substring(name.lastIndexOf('.'));
	}
	
	private void setMessage(Color color, String msg) {
		if(color != null) message.setForeground(color);
		
		message.setText(msg);
		message.setVisible(true);
	}
	
	private void setEnabled() {
		excuting.setEnabled(true);
	}
	
	private String getFileName(String ext) {
		if(!isNotBlank(fileName)) {
			fileName = DigestUtils.md5Hex(fileName) + ext;
		} 
		if(this.getFileExt(fileName) == null) {
			fileName += ext;
		}
		return fileName;
	}
}
