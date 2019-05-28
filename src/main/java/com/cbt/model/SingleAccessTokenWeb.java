package com.cbt.model;

import com.cbt.model.AccessToken;
import com.cbt.util.WriteProp;
import com.cbt.wximpl.Wechatimpl;

public class SingleAccessTokenWeb {
	// private static final String appid = "wx9cce989937d0dde7";// 测试的appid
	// private static final String appsecret =
	// "50693222b139ac687494b307de550e9c";// 测试的appsecret
	// private static final String appid = "wxc76c92bb4bf80f20";// 你自己的appid
	// private static final String appsecret =
	// "b9fc8eb71f48a91ec80fa22d3f204c86";// 你自己的appsecret
	//private static final String appid = "wx4e74b96744199b97";// 你自己的appid
	//private static final String appsecret = "7abe65af23df4dd9b761ec186ba51fcd";// 你自己的appsecret

	private AccessToken accessToken;
	private static SingleAccessTokenWeb singleAccessTokenWeb = null;
	/**
	 * 私有构造函数
	 */
	private  SingleAccessTokenWeb(String code) {
		accessToken = Wechatimpl.getComponentAccessToken(WriteProp.get("appid_web"), WriteProp.get("secret_web"),code);
		System.out.println(accessToken);
		System.err.println("已重新获取基础支持的access_token:"+accessToken.getToken());
		initThread();
	}
	/**
	 * 获取SingleAccessToken对象
	 *
	 * @return
	 */
	public static SingleAccessTokenWeb getInstance(String code) {
		if (singleAccessTokenWeb == null) {
			synchronized (SingleAccessTokenWeb.class) {
				if (singleAccessTokenWeb == null) {
					singleAccessTokenWeb = new SingleAccessTokenWeb(code);
				}
			}
		}
		return singleAccessTokenWeb;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 开启线程，休眠时间结束设置SingleAccessToken为空
	 */
	private void initThread() {
		new Thread(new Runnable() {
			public void run() {
				try {
					// 睡眠7100秒,两小时之内，获取的token实例为同一个
					Thread.sleep(7100 * 1000);
					singleAccessTokenWeb = null;
				} catch (InterruptedException e) {
					System.err.println("休眠线程被打断！");
					e.printStackTrace();
				}
			}
		}).start();
	}
}
