package com.chemyoo.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/** 
 * @author ���� : jianqing.liu
 * @version ����ʱ�䣺2018��5��30�� ����9:47:01 
 * @since 2018��5��30�� ����9:47:01 
 * @description �ļ�ѡ���
 */
public class SelectFiles {
	
	private SelectFiles() {}

	public static File getSavePath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();  //ע���ˣ�������Ҫ��һ��
		//�������·��Ϊ����·��              
		JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory());//"F:/pic"
		fileChooser.setDialogTitle("��ѡ���ļ���...");
		fileChooser.setApproveButtonText("ȷ��");
		//ֻѡ���ļ���
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//�����ļ��Ƿ�ɶ�ѡ
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);// ȥ����ʾ�����ļ��İ�ť
		fileChooser.setFileFilter(new SelectFileFilter());
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
}
