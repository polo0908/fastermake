package com.cbt.model;

import java.util.Map;


public class PushTouser<T> {
   
	private T touser;      //用于设定图文消息的接收者
	private Mpnews mpnews;               //用于设定即将发送的图文消息
	private String msgtype;              //消息类型
	private int send_ignore_reprint;     //图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。




	public T getTouser() {
		return touser;
	}
	public void setTouser(T touser) {
		this.touser = touser;
	}
	public Mpnews getMpnews() {
		return mpnews;
	}
	public void setMpnews(Mpnews mpnews) {
		this.mpnews = mpnews;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public int getSend_ignore_reprint() {
		return send_ignore_reprint;
	}
	public void setSend_ignore_reprint(int send_ignore_reprint) {
		this.send_ignore_reprint = send_ignore_reprint;
	}
	@Override
	public String toString() {
		return "PushTouser [touser=" + touser + ", mpnews=" + mpnews + ", msgtype="
				+ msgtype + ", send_ignore_reprint=" + send_ignore_reprint
				+ "]";
	}
	
	
	
	
}
