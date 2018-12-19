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
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import com.chemyoo.ui.PropertiesUtils;


/** 
 * @author Author : jianqing.liu
 * @version version : created time��2018��12��18�� ����1:39:52 
 * @since since from 2018��12��18�� ����1:39:52 to now.
 * @description class description
 */
public class Downloader extends Thread {
	
	private String fileName;
	
	private String resourceUrl;
	
	private String savePath;
	
	private JLabel message;
	
	protected static Random random = new Random();
	
	// ��ƽ�ź������ź�������Ϊһ��
	private final Semaphore semaphore = new Semaphore(1, true);
	
	private JButton excuting;
	
	private Logger logger = Logger.getLogger(Downloader.class.getName());
	
	public Downloader(String musicUrl, String fileName, String savePath, JLabel message, JButton excuting) {
		this.fileName = fileName;
		this.savePath = savePath;
		this.message = message;
		this.resourceUrl = musicUrl;
		this.excuting = excuting;
	}
	

	@Override
	public void run() {
		File file = new File(savePath + PropertiesUtils.getFileSeparator() + fileName);
		File parent = file.getParentFile();
		
		if(!parent.exists()) parent.mkdirs();
		
		InputStream in = null;
		HttpURLConnection httpConnection = null;
		try (FileOutputStream write = new FileOutputStream(file)){
			semaphore.acquire();
			String userAgent = new String []{"Mozilla/4.0","Mozilla/5.0","Opera/9.80"}[random.nextInt(3)];
			URL uri = new URL(resourceUrl);
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
				
				logger.error("��ַ��" + resourceUrl + "����ʧ�ܣ�" 
						+ IOUtils.toString(in, "gb2312"));
				throw new IllegalAccessError("��ַ����ʧ��...");
			} else {
				in = httpConnection.getInputStream();
				int available = httpConnection.getContentLength();
				String size = String.format("�ļ���С%.2fMB��", available * 1F / (1024 * 1024));
				long count = 0;
				message.setForeground(Color.BLUE);
				byte [] bytes = new byte[in.available()];
				message.setVisible(true);
				while (in.read(bytes) != -1) {
					write.write(bytes);
					count += bytes.length;
					bytes = new byte[in.available()];
					String progress = String.format("�������أ�%.2f", count * 100F / available) + "%";
					message.setText(size + progress);
				}
				message.setText(size + "������ɣ�100%");
			}
		} catch (Exception e) {
			this.setMessage(Color.RED, "����ʧ�ܣ�������鿴/logs�µ���־��");
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
			// ���ļ������ͷź����سɹ��������ļ��ֱ��ʱ�ʶ		
			in = null;
			if(httpConnection != null)
				httpConnection.disconnect();
			semaphore.release();
			this.setEnabled();
		}
		//https://music.163.com/song?id=30064263&userid=135693455
	}
	
	private void setMessage(Color color, String msg) {
		if(color != null) message.setForeground(color);
		
		message.setText(msg);
		message.setVisible(true);
	}
	
	private void setEnabled() {
		excuting.setEnabled(true);
	}
	
}
