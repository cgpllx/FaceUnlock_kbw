package com.konka.project;

public class KonkaSo {
	static {
		System.loadLibrary("KonkaProject");
	}
	/**
	 * 人脸检测
	 * @param buf 人脸图片数据
	 * @param modelPath 模型文件路径
	 * @param w the current view width
	 * @param h the current view height
	 * 
	 * @return 输出数组为图片中包含人脸的区域，每4个数据为1个人脸，4个值分别为：x坐标，y坐标，人脸宽，人脸高
	 * */ 
	public static native int[] FaceDetect(int[] buf, String modelPath, int w, int h);
	
	/**
	 * 年龄估计和性别识别
	 * @param buf 人脸图片数据
	 * @param modelPath 模型文件路径
	 * @param w 
	 * @param h 人脸图片的宽和高
	 * 
	 * @return 	包含两个数据：第一个数据为年龄，为年龄的实际值，等于-1时表示没有检测出人脸；
	 * 			第二个数据为性别，1为男，2为女，-1表示没有检测出人脸
	 * */
	public static native float[] AgeGenderEstimate(int[] buf, String modelPath, int w, int h);
	
	/**
	 * 人脸比对，两张图片的宽和高必须相等
	 * @param buf1 第一张人脸图片数据
	 * @param buf2 第二张人脸图片数据
	 * @param modelPath 模型文件路径
	 * @param w
	 * @param h
	 * 
	 * @return true表示两张人脸图片为同一个人，false表示两张人脸图片为不同人
	 * */
//	public static native boolean FaceVerification(int[] buf1,int[] buf2, String modelPath, int w, int h);
	
	public static native float FaceVerification(int[] buf1,int[] buf2, String modelPath, int w, int h);

}
