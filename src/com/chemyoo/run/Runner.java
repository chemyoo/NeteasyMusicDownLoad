package com.chemyoo.run;

import org.apache.log4j.Logger;

import com.chemyoo.ui.DownLoaderUI;

/** 
 * @author Author : jianqing.liu
 * @version version : created time��2018��12��18�� ����1:21:11 
 * @since since from 2018��12��18�� ����1:21:11 to now.
 * @description class description
 */

public class Runner {
	private static final Logger LOG = Logger.getLogger(Runner.class);
	
		public static void main(String[] args) {
			new DownLoaderUI().initSpiderUI();
			LOG.info("��������...");
		}
} 
