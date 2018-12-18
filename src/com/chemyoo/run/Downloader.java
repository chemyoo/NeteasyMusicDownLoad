package com.chemyoo.run;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import javax.swing.JLabel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import com.chemyoo.ui.PropertiesUtils;


/** 
 * @author Author : jianqing.liu
 * @version version : created time��2018��12��18�� ����1:39:52 
 * @since since from 2018��12��18�� ����1:39:52 to now.
 * @description class description
 */
public class Downloader extends Thread{
	
	private String fileName;
	
	private String musicId;
	
	private String musicUrl;
	
	private String savePath;
	
	private JLabel message;
	
	private static final String API_URL = "http://music.163.com/song/media/outer/url?id=${id}.mp3";
	protected static Random random = new Random();
	
	public Downloader( String musicUrl, String fileName, String savePath, JLabel message) {
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
		} else {
			musicId = url.substring(url.replace('\\', '/').lastIndexOf('/') + 1);
		}
		if(!url.endsWith(".mp3")) {
			message.setForeground(Color.RED);
			message.setText("�������޷���������...");
			message.setVisible(true);
			return;
		}
		InputStream in = null;
		HttpURLConnection httpConnection = null;
		if(!isNotBlank(fileName)) {
			fileName = DigestUtils.md5Hex(musicId) + ".mp3";
		} 
		if(!fileName.endsWith(".mp3")) {
			fileName += ".mp3";
		} 
		File file = new File(savePath + PropertiesUtils.getFileSeparator() + fileName);
		File parent = file.getParentFile();
		
		if(!parent.exists()) parent.mkdirs();
		
		try (FileOutputStream write = new FileOutputStream(file)){
			String userAgent = new String []{"Mozilla/4.0","Mozilla/5.0","Opera/9.80"}[random.nextInt(3)];
			URL uri = new URL(url);
			httpConnection = (HttpURLConnection) uri.openConnection();
			
			// ��������������ʱ����λ�����룩  
			httpConnection.setConnectTimeout(60 * 1000);
			// ���ô�������ȡ���ݳ�ʱ����λ�����룩 
			httpConnection.setReadTimeout(90 * 1000);
			
			
			httpConnection.setRequestProperty("User-agent", userAgent);
			
			// ������վ
			httpConnection.connect();
			
			// ��ַ����ʧ�ܾͼ�������һ����ִַ�С�
			if(httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				in = httpConnection.getErrorStream();
				if(in == null)
					in = httpConnection.getInputStream();
				
				Logger.getLogger("Downloader").info("��ַ��" + url + "����ʧ�ܣ�" 
						+ IOUtils.toString(in, "gb2312"));
				throw new IllegalAccessError("��ַ����ʧ��...");
			} else {
				in = httpConnection.getInputStream();
				int available = httpConnection.getContentLength();
				String size = String.format("�ļ���С%.2fMB��", available * 1F / (1024 * 1024));
				long count = 0;
				message.setForeground(Color.GREEN);
				byte [] bytes = new byte[in.available()];
				message.setVisible(true);
				while (in.read(bytes) != -1) {
					write.write(bytes);
					count += bytes.length;
					bytes = new byte[in.available()];
					message.setText(size + "���ؽ��ȣ�" +(count * 100 / available) + "%");
				}
			}
			message.setText("���سɹ���");
		} catch (Exception e) {
			message.setForeground(Color.RED);
			message.setText("����ʧ�ܣ�������鿴/logs�µ���־��" );
			message.setVisible(true);
			Logger.getLogger("Downloader").error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
//			���ļ������ͷź����سɹ��������ļ��ֱ��ʱ�ʶ		
			in = null;
			if(httpConnection != null)
				httpConnection.disconnect();
		}
	}
	
}
