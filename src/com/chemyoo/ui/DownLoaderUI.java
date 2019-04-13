package com.chemyoo.ui;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import com.chemyoo.run.Downloader;
/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年5月30日 上午9:35:07 
 * @since 2018年5月30日 上午9:35:07 
 * @description 用户图形界面
 */
public class DownLoaderUI extends JFrame{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1987615425247905123L;
	
	private static final Logger LOG = Logger.getLogger(DownLoaderUI.class);

	public static final String DEFAULT_PATH = System.getProperty("user.dir");
	
	
	public DownLoaderUI() {
		super();
	}
	
	/**
	 * 初始化UI进行显示
	 */
	public void initSpiderUI() {
		this.setTitle("DownLoaderUI(作者：chemyoo@foxmail.com)");  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setSize(500, 250);
        this.setLocationRelativeTo(null);//窗体居中显示  
        final JPanel contentPane= new JPanel();  
        contentPane.setBorder(new EmptyBorder(20,5,5,5));  
        this.setContentPane(contentPane);  
        contentPane.setLayout(new GridLayout(5,1,5,5));  
        contentPane.setAlignmentY(LEFT_ALIGNMENT);
		JPanel pane1 = new JPanel();
		JPanel pane2 = new JPanel();
		JPanel pane3 = new JPanel();
		JPanel pane4 = new JPanel();
		JPanel pane5 = new JPanel();

        JLabel label1=new JLabel("链接地址*");  
        Dimension preferredSize = new Dimension(98,20);//设置尺寸
        label1.setPreferredSize(preferredSize);
        label1.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField musicUrl = new JTextField();  
        musicUrl.setColumns(31);  
        pane1.add(label1);  
        pane1.add(musicUrl);  
        
        JLabel label4 = new JLabel("文件名*");  
        label4.setPreferredSize(preferredSize);
        label4.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField fileName = new JTextField();  
        fileName.setColumns(31);  
        pane5.add(label4);  
        pane5.add(fileName);
        
        pane1.setAlignmentX(LEFT_ALIGNMENT);
        pane5.setAlignmentX(LEFT_ALIGNMENT);
        
        musicUrl.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = musicUrl.getText().trim();
				try {
					fileName.setText(getFileNameFromUrl(URLDecoder.decode(text, "utf-8")));
				} catch (UnsupportedEncodingException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				this.insertUpdate(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				LOG.info("changedUpdate:" + musicUrl.getText().trim());
			}
		});
        
        JLabel label2=new JLabel("本地保存路径*");  
        preferredSize = new Dimension(98,20);//设置尺寸
        label2.setPreferredSize(preferredSize);
        label2.setHorizontalAlignment(JTextField.RIGHT);
        File[] root = File.listRoots();
        final JTextField path = new JTextField(root[0].getAbsolutePath() + "downloads");
        path.setColumns(25);  
        pane2.add(label2);  
        pane2.add(path);  
        
        pane2.setAlignmentX(LEFT_ALIGNMENT);
        
        JButton button = new JButton("选择");
        preferredSize = new Dimension(60,20);//设置尺寸
        button.setPreferredSize(preferredSize);
        pane2.add(button);
        button.addMouseListener(new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String current = path.getText();
				File file = SelectFiles.getSavePath(current);
				if(file != null) {
					path.setText(file.getAbsolutePath());
				} 
			}
		});
        
        final JButton start = new JButton("下载");
        preferredSize = new Dimension(90,25);//设置尺寸
        start.setPreferredSize(preferredSize);
        pane3.add(start);
        
        final JButton openFile = new JButton("打开");
        preferredSize = new Dimension(90,25);//设置尺寸
        openFile.setPreferredSize(preferredSize);
        pane3.add(openFile);
        
        final JLabel message = new JLabel();
		message.setForeground(Color.RED);
		message.setVisible(false);
		pane4.add(message);
        
        start.addMouseListener(new MouseEventAdapter() {
			/* (non-Javadoc)
			 * @see com.chemyoo.ui.MouseEventAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isNotBlank(musicUrl.getText(), path.getText(), fileName.getText())) {
					try {
						if(getFileExt(fileName.getText()) == null) {
							message.setForeground(Color.RED);
							message.setText("请输入文件名全称（包含拓展名）。");
							message.setVisible(true);
						} else {
							start.setEnabled(false);
							message.setVisible(false);
							new Downloader(musicUrl.getText(), 
									fileName.getText(), 
									path.getText(), 
									message, 
									start).start();
						}
					} catch (Exception ex) {
						message.setForeground(Color.RED);
						message.setText("下载失败：" + ex.getMessage());
						message.setVisible(true);
					}
				} else {
					message.setText("链接地址、文件名和保存路径都不能为空！");
					message.setVisible(true);
					LOG.info("download failure.");
				}
			}
		});
        
        openFile.addMouseListener(new MouseEventAdapter() {
        	@Override
			public void mouseClicked(MouseEvent e) {
        		if(isNotBlank(path.getText())) {
        			File file = new File(path.getText().trim());
        			if(file.exists()) {
        				try {
							Desktop.getDesktop().open(file);
							LOG.info("open file: " + file.getAbsolutePath());
						} catch (IOException e1) {
							message.setText("failed open file. file is not exists");
							message.setVisible(true);
						}
        			} else {
        				message.setText("failed open file. file is not exists");
						message.setVisible(true);
        			}
        		}
        	}
        });
        

//		pane6.setBackground(new Color(255,255,224))
		
		contentPane.add(pane1);  
        contentPane.add(pane5); 
        contentPane.add(pane2); 
        contentPane.add(pane3); 
        contentPane.add(pane4);
        this.setVisible(true);  
	}
	
	private static boolean isNotBlank(String...args) {
		for(String arg : args) {
			if(arg == null || "".equals(arg.trim())) {
				return false;
			}
		}
		return true;
	}
	
	private static String getFileExt(String fileName) {
		if(!fileName.contains(".")) return null;
		return fileName.substring(fileName.lastIndexOf('.'));
	}
	
	private static String getFileNameFromUrl(String url) {
		if(!url.replace('\\', '/').contains("/")) return "";
		return url.substring(url.lastIndexOf('/') + 1);
	}

}
