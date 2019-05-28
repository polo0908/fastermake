package com.cbt.controller;


import com.cbt.util.ConvertVideo;
import com.cbt.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于异步转换视频
 * @ClassName SynConvertVideo 
 * @Description 
 * @author zj
 * @date 2018年11月7日 下午12:02:26
 */

public class SynConvertVideo {

	private static Logger logger = LoggerFactory.getLogger(SynConvertVideo.class);
	private static PropertiesUtils reader = new PropertiesUtils("serverPath.properties");
	
	public static void sendRequest(String inputPath,String outputPath,String name) {
		try {			
			new Thread(new SynConvertVideo().new SendHttp(inputPath,reader.getProperty("ffmpeg_path"),outputPath,name)).start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("视频转换失败", e);
		}
	}
	
	
	
	class SendHttp implements Runnable{
		
		private String inputPath;		
		private String ffmpegPath;
		private String outputPath;
		private String name;
		
		SendHttp(String inputPath,String ffmpegPath,String outputPath,String name){
			this.inputPath = inputPath;
			this.ffmpegPath = ffmpegPath;
			this.outputPath = outputPath;
			this.name = name;
		}

		@Override
		public void run() {			
			ConvertVideo.process(inputPath, ffmpegPath, outputPath, name);
		}
		
		
		
		
	}
	
	

}