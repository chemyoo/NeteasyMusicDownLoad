package com.chemyoo.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年5月30日 上午9:47:01 
 * @since 2018年5月30日 上午9:47:01 
 * @description 文件选择框
 */
public class SelectFiles {

	public static File getSavePath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
		//设置最初路径为桌面路径              
		JFileChooser fileChooser = new JFileChooser(fsv.getHomeDirectory());//"F:/pic"
		fileChooser.setDialogTitle("请选择文件夹...");
		fileChooser.setApproveButtonText("确定");
		//只选择文件夹
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//设置文件是否可多选
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);// 去掉显示所有文件的按钮
		fileChooser.setFileFilter(new SelectFileFilter());
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
}
