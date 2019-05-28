package com.cbt.util;


import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 public class ConvertVideo {
     
     private static String inputPath = "";
     
     private static String outputPath = "";
     
     private static String ffmpegPath = "";
    
    public static void main(String args[]) throws IOException {
         
         getPath();
         
         if (!checkfile(inputPath)) {
             System.out.println(inputPath + " is not file");
             return;
         }
         if (process()) {
             System.out.println("ok");
         }
     }
     
     private static boolean process() {
    	 int type = checkContentType(inputPath);
         boolean status = false;
         if (type == 0) {
             System.out.println("直接转成flv格式");
            status = processFLV(inputPath, inputPath, ffmpegPath, outputPath,"1.mp4");// 直接转成flv格式
         } else if (type == 1) {
            String avifilepath = processAVI(type,inputPath, ffmpegPath, outputPath);
             if (avifilepath == null)
                 return false;// 没有得到avi格式
             status = processFLV(avifilepath, inputPath, ffmpegPath, outputPath,"1.mp4");// 将avi转成flv格式
         }
         return status;
	}

	private static void getPath() { // 先获取当前项目路径，在获得源文件、目标文件、转换器的路径
         File diretory = new File("");
         try {
            String currPath = "D:\\soft\\tomcat7FH\\webapps\\picture";
             inputPath = currPath + "\\CHAPETON.avi";
             outputPath = currPath + "\\";
             ffmpegPath = "E:\\newquotation\\ffmpeg\\bin\\";
             System.out.println(currPath);
         }
         catch (Exception e) {
             System.out.println("getPath出错");
         }
     }
    
     public static boolean process(String inputPath,String ffmpegPath,String outputPath,String name) {
         int type = checkContentType(inputPath);
         boolean status = false;
         if (type == 0) {
             System.out.println("直接转成flv格式");
            status = processFLV(inputPath, inputPath, ffmpegPath, outputPath,name);// 直接转成flv格式
         } else if (type == 1) {
            String avifilepath = processAVI(type,inputPath, ffmpegPath, outputPath);
             if (avifilepath == null)
                 return false;// 没有得到avi格式
             status = processFLV(avifilepath, inputPath, ffmpegPath, outputPath,name);// 将avi转成flv格式
         }
         return status;
     }
 
     private static int checkContentType(String inputPath) {
         String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length())
                 .toLowerCase();
         // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
         if (type.equals("avi")) {
             return 0;
         } else if (type.equals("mpg")) {
            return 0;
         } else if (type.equals("wmv")) {
             return 0;
         } else if (type.equals("3gp")) {
             return 0;
         } else if (type.equals("mov")) {
             return 0;
         } else if (type.equals("mp4")) {
             return 0;
         } else if (type.equals("asf")) {
             return 0;
         } else if (type.equals("asx")) {
             return 0;
         } else if (type.equals("flv")) {             return 0;
        }
         // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
         // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
         else if (type.equals("wmv9")) {
             return 1;
         } else if (type.equals("rm")) {
             return 1;
         } else if (type.equals("rmvb")) {
             return 1;
         }
         return 9;
     }
 
     public static boolean checkfile(String path) {
         File file = new File(path);
         if (!file.isFile()) {
             return false;
         }
         return true;
     }
 
    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     private static String processAVI(int type,String inputPath,String ffmpegPath,String outputPath) {
         List<String> commend = new ArrayList<String>();
         commend.add(ffmpegPath + "mencoder");
         commend.add(inputPath);
         commend.add("-oac");         commend.add("lavc");
         commend.add("-lavcopts");
         commend.add("acodec=mp3:abitrate=64");
         commend.add("-ovc");
         commend.add("xvid");
         commend.add("-xvidencopts");
         commend.add("bitrate=600");
         commend.add("-of");
         commend.add("avi");
        commend.add("-o");
         commend.add(outputPath + "a.avi");
         try {
             ProcessBuilder builder = new ProcessBuilder();
             Process process = builder.command(commend).redirectErrorStream(true).start();
             new PrintStream(process.getInputStream());
           new PrintStream(process.getErrorStream());
             process.waitFor();
             return outputPath + "a.avi";
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }
 
     // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     private static boolean processFLV(String oldfilepath,String inputPath,String ffmpegPath,String outputPath,String name) {
 
         if (!checkfile(inputPath)) {
             System.out.println(oldfilepath + " is not file");
            return false;
         }
         File source = new File(inputPath);
         Encoder encoder = new Encoder();
         try {  
        	 MultimediaInfo m = encoder.getInfo(source);
		
         List<String> command = new ArrayList<String>();
         command.add(ffmpegPath+"ffmpeg");
         command.add("-i");
         command.add(oldfilepath);
         command.add("-f");
         command.add("mp4");
         command.add("-vcodec");
         command.add("h264");
         command.add("-profile:v");
         command.add("high");
         command.add("-level:v");
         command.add("3.1");
         command.add("-pix_fmt");
         command.add("yuv420p");
         
        // -pix_fmt yuv420p
        // command.add("-level 3.0");
         
         /*command.add("");
         command.add("5.1");*/
         
//         command.add("-ab");
  //       command.add("56");
  //       command.add("-ar");
  //       command.add("22050");
         command.add("-qscale");
         command.add("8");
         command.add("-r");
        command.add("15");
         command.add("-s");
         command.add((m.getVideo().getSize().getWidth())+"x"+(m.getVideo().getSize().getHeight()));
         command.add(outputPath + name);

         try {
            
            // 方案1
//             Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " + oldfilepath
//                    + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
//                    + outputPath + name);
             
            // 方案2
             Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
             
            new PrintStream(videoProcess.getErrorStream()).start();
             
            new PrintStream(videoProcess.getInputStream()).start();
             
             videoProcess.waitFor();
             
             
             return true;
        } catch (Exception e) {
             e.printStackTrace();
             return false;
         }
    
     } catch (Exception e1) {
			
			e1.printStackTrace();
			 return false;
     }
     }
 }
 
 class PrintStream extends Thread 
 {
     java.io.InputStream __is = null;
     public PrintStream(java.io.InputStream is) 
     {
         __is = is;
     } 
 
     public void run() 
     {
         try 
         {
             while(this != null) 
             {
                 int _ch = __is.read();
                 if(_ch != -1) 
                     System.out.print((char)_ch); 
                 else break;
             }
         } 
         catch (Exception e) 
         {
             e.printStackTrace();
         } 
     }
 
     
     /*public static void main(String[] args) {
    	 String inPath = "D:\\source\\1.avi";
    	 String outPath = "D:\\des\\";
    	 
    	 int type = ConvertVideo.checkContentType(inPath);
         boolean status = false;
         if (type == 0) {
             
            status = ConvertVideo.processFLV("D:\\source\\1.avi", "D:\\source\\1.avi", "D:\\des\\", "D:\\des\\","1.mp4");// 直接转成flv格式
         } else if (type == 1) {
            String avifilepath = ConvertVideo.processAVI(type,"D:\\source\\1.avi", "D:\\des\\", "D:\\des\\");
             if (avifilepath == null)              
             status = ConvertVideo.processFLV(avifilepath, "D:\\source\\1.avi", "D:\\des\\", "D:\\des\\","1.mp4");// 将avi转成flv格式
         }
	}*/
 }