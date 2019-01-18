package com.chemyoo.run;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
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
	
	private String resourceUrl;
	
	private String savePath;
	
	private JLabel message;
	
	protected static Random random = new Random();
	
	// 公平信号量，信号量至少为一个
	private final Semaphore semaphore = new Semaphore(1, true);
	
	private JButton excuting;
	
	private Logger logger = Logger.getLogger(Downloader.class.getName());
	
	private List<Integer> byteCache = new ArrayList<>();
	
	private long downloaded = 0; 
	
	private int downsize = 4 * 1024; 
	
	private long startTime = 0;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private long offset = Calendar.getInstance().getTimeZone().getRawOffset();
	
	public Downloader(String musicUrl, String fileName, String savePath, JLabel message, JButton excuting) {
		this.fileName = fileName;
		this.savePath = savePath;
		this.message = message;
		this.resourceUrl = musicUrl;
		this.excuting = excuting;
	}
	

	@Override
	public void run() {
		this.startTime = System.currentTimeMillis();
		File file = new File(savePath + PropertiesUtils.getFileSeparator() + fileName);
		File parent = file.getParentFile();
		
		if(!parent.exists()) parent.mkdirs();
		
		InputStream in = null;
		HttpURLConnection httpConnection = null;
		try (FileOutputStream write = new FileOutputStream(file)){
			semaphore.acquire();
			String userAgent = new String []{"Mozilla/4.0","Mozilla/5.0","Opera/9.80"}[random.nextInt(3)];
			
			URL uri = new URL(resourceUrl);
			HttpsURLConnection.setDefaultSSLSocketFactory(SelfSSLSocket.getSSLSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			logger.info(uri.getProtocol() + " -> URL.getFile = " + uri.getFile());
			httpConnection = (HttpURLConnection) uri.openConnection();
			
			// 设置连接主机超时（单位：毫秒）  
			httpConnection.setConnectTimeout(60 * 1000);
			// 设置从主机读取数据超时（单位：毫秒） 
			httpConnection.setReadTimeout(90 * 1000);
			
			httpConnection.setRequestProperty("User-agent", userAgent);
			
			// 连接网站
			httpConnection.connect();
			
			// 网址连接失败就继续向下一个网址执行。
			if(httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				in = httpConnection.getErrorStream();
				if(in == null)
					in = httpConnection.getInputStream();
				
				logger.error("网址：" + resourceUrl + "访问失败：" 
						+ IOUtils.toString(in, Charset.forName("utf-8").displayName()));
				throw new IllegalAccessError("网址连接失败...");
			} else {
				in = httpConnection.getInputStream();
				long available = Math.abs(httpConnection.getContentLengthLong());
				String size = getContentSize(available);
				message.setForeground(Color.BLUE);
				message.setVisible(true);
				int c = -1;
				while ((c = in.read()) != -1) {
					byteCache.add(c);
					this.flush(write, downsize, size, available);
				}
				this.flush(write, byteCache.size(), size, available);
				if(available < 2) {
					String fileSize = getContentSize(FileUtils.sizeOf(file));
					message.setText(fileSize + "下载完成：100%，用时：" + getTakeTime());
				} else {
					message.setText(size + "下载完成：100%，用时：" + getTakeTime());
				}
			}
		} catch (Exception | IllegalAccessError e) {
			this.setMessage(Color.RED, "下载失败，详情请查看/logs下的日志。");
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
			// 待文件流被释放后，下载成功	
			in = null;
			if(httpConnection != null)
				httpConnection.disconnect();
			semaphore.release();
			this.setEnabled();
		}
		//https://music.163.com/song?id=30064263&userid=135693455
	}
	
	private String getContentSize(long size) {
		return String.format("文件大小%.2fMB，", size * 1F / (1024 * 1024));
	}
	
	private void flush(FileOutputStream write, int downSize, String size, long available) throws IOException {
		if(this.byteCache.size() >= downSize) {
			byte [] bytes = new byte[downSize];
			for(int i = 0, len = bytes.length; i < len; i++) {
				bytes[i] = this.byteCache.get(i).byteValue();
			}
			write.write(bytes);
			downloaded += bytes.length;
			byteCache.clear();
		} 
		if(available < 2) {
			// 若文件大小位置，则显示下载进度为50%
			available = (long)(downloaded + downloaded * 0.6);
		}
		String progress = String.format("正在下载：%.2f", downloaded * 100F / available) + "%";
		message.setText(size + progress + "，用时：" + getTakeTime());
	}
	
	private void setMessage(Color color, String msg) {
		if(color != null) message.setForeground(color);
		
		message.setText(msg);
		message.setVisible(true);
	}
	
	private void setEnabled() {
		excuting.setEnabled(true);
	}
	
	private String getTakeTime() {
		long take = System.currentTimeMillis() - startTime - offset;
		return dateFormat.format(new Date(take));
	}
	
}
