package com.cbt.sina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cbt.sina.Oauth;
import com.cbt.sina.model.WeiboException;
import com.cbt.sina.util.BareBonesBrowserLaunch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuth4Code {
	static Logger logger = LoggerFactory.getLogger(OAuth4Code.class);

	public static void main(String [] args) throws WeiboException, IOException{
		Oauth oauth = new Oauth();
		BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
		System.out.println(oauth.authorize("code"));
		System.out.print("Hit enter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		logger.info("code: " + code);
		try{
			System.out.println(oauth.getAccessTokenByCode(code));
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				logger.info("Unable to get the access token.");
			}else{
				e.printStackTrace();
			}
		}
	}
}
