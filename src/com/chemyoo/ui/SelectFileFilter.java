package com.chemyoo.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/** 
 * @author ���� : jianqing.liu
 * @version ����ʱ�䣺2018��6��8�� ����2:30:32 
 * @since 2018��6��8�� ����2:30:32 
 * @description �ļ��й�����
 */
public class SelectFileFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "�ļ��У�directory��";
	}
	
	@Override
	public boolean accept(File f) {
		return f.isDirectory();
	}

}
