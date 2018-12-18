package com.chemyoo.ui;


import org.apache.log4j.Logger;

import com.chemyoo.run.Downloader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/** 
 * @author ���� : jianqing.liu
 * @version ����ʱ�䣺2018��5��30�� ����9:35:07 
 * @since 2018��5��30�� ����9:35:07 
 * @description �û�ͼ�ν���
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
	 * ��ʼ��UI������ʾ
	 */
	public void initSpiderUI() {
		this.setTitle("DownLoaderUI(���ߣ�chemyoo@foxmail.com)");  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setSize(500, 250);
        this.setLocationRelativeTo(null);//���������ʾ  
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

        JLabel label1=new JLabel("��������������:");  
        Dimension preferredSize = new Dimension(98,20);//���óߴ�
        label1.setPreferredSize(preferredSize);
        label1.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField musicUrl = new JTextField();  
        musicUrl.setColumns(31);  
        pane1.add(label1);  
        pane1.add(musicUrl);  
        
        JLabel label4 = new JLabel("�ļ���:");  
        label4.setPreferredSize(preferredSize);
        label4.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField fileName = new JTextField();  
        fileName.setColumns(31);  
        pane5.add(label4);  
        pane5.add(fileName);
        
        pane1.setAlignmentX(LEFT_ALIGNMENT);
        pane5.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel label2=new JLabel("���ر���·��*:");  
        preferredSize = new Dimension(98,20);//���óߴ�
        label2.setPreferredSize(preferredSize);
        label2.setHorizontalAlignment(JTextField.RIGHT);
        final JTextField path = new JTextField();  
        path.setColumns(25);  
        pane2.add(label2);  
        pane2.add(path);  
        
        pane2.setAlignmentX(LEFT_ALIGNMENT);
        
        JButton button = new JButton("ѡ��");
        preferredSize = new Dimension(60,20);//���óߴ�
        button.setPreferredSize(preferredSize);
        pane2.add(button);
        button.addMouseListener(new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File file = SelectFiles.getSavePath();
				if(file == null) {
					path.setText("");
				} else {
					path.setText(file.getAbsolutePath());
				}
			}
		});
        
        final JButton start = new JButton("����");
        preferredSize = new Dimension(90,25);//���óߴ�
        start.setPreferredSize(preferredSize);
        pane3.add(start);
        
        final JLabel message = new JLabel();
		message.setForeground(Color.RED);
		message.setVisible(false);
		pane4.add(message);
        
        start.addMouseListener(new MouseEventAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isNotBlank(musicUrl.getText()) && isNotBlank(path.getText())) {
					try {
						new Downloader(musicUrl.getText(), fileName.getText(), path.getText(), message).start();
					} catch (Exception ex) {
						message.setForeground(Color.RED);
						message.setText("����ʧ�ܣ�" + ex.getMessage());
						message.setVisible(true);
					}
				} else {
					message.setText("����ID�򱣴�·������Ϊ�գ�");
					message.setVisible(true);
					LOG.info("download failure.");
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
