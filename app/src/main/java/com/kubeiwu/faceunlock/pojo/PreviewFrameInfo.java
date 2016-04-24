package com.kubeiwu.faceunlock.pojo;

public class PreviewFrameInfo {
	private byte[] data;
	private int width;
	private int height;

	public PreviewFrameInfo(byte[] data, int width, int height) {
		super();
		this.data = data;
		this.width = width;
		this.height = height;
	}

	public PreviewFrameInfo() {
		super();
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
