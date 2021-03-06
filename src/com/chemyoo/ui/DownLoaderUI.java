package com.chemyoo.ui;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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
		this.setTitle("网易云音乐下载工具(作者：chemyoo@foxmail.com)");  
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

        JLabel label1=new JLabel("网易云音乐链接*");  
        Dimension preferredSize = new Dimension(98,20);//设置尺寸
        label1.setPreferredSize(preferredSize);
        label1.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField musicUrl = new JTextField();  
        musicUrl.setColumns(31);  
        pane1.add(label1);  
        pane1.add(musicUrl);  
        
        JLabel label4 = new JLabel("文件名(可不填)");  
        label4.setPreferredSize(preferredSize);
        label4.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField fileName = new JTextField();  
        fileName.setColumns(31);  
        pane5.add(label4);  
        pane5.add(fileName);
        
        pane1.setAlignmentX(LEFT_ALIGNMENT);
        pane5.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel label2=new JLabel("本地保存路径*");  
        preferredSize = new Dimension(98,20);//设置尺寸
        label2.setPreferredSize(preferredSize);
        label2.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField path = new JTextField("E:/KuGou/song");  
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
				File file = SelectFiles.getSavePath();
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
				if(isNotBlank(musicUrl.getText()) && isNotBlank(path.getText())) {
					try {
						start.setEnabled(false);
						message.setVisible(false);
						new Downloader(musicUrl.getText(), 
								fileName.getText(), 
								path.getText(), 
								message, 
								start).start();
					} catch (Exception ex) {
						message.setForeground(Color.RED);
						message.setText("下载失败：" + ex.getMessage());
						message.setVisible(true);
					}
				} else {
					message.setText("音乐链接和保存路径都不能为空！");
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

}
