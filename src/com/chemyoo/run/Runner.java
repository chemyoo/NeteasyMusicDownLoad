package com.chemyoo.run;

import org.apache.log4j.Logger;

import com.chemyoo.ui.DownLoaderUI;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2018年12月18日 下午1:21:11 
 * @since since from 2018年12月18日 下午1:21:11 to now.
 * @description class description
 */

public class Runner {
	private static final Logger LOG = Logger.getLogger(Runner.class);
	
		public static void main(String[] args) {
			new DownLoaderUI().initSpiderUI();
			LOG.info("启动服务...");
		}
} 
