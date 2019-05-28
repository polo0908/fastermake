package com.cbt.controller;

import com.cbt.entity.*;
import com.cbt.enums.OrderStatusEnum;
import com.cbt.model.*;
import com.cbt.service.*;
import com.cbt.sina.OAuth4Code;
import com.cbt.sina.Oauth;
import com.cbt.sina.Timeline;
import com.cbt.sina.http.AccessToken;
import com.cbt.sina.http.ImageItem;
import com.cbt.sina.model.Status;
import com.cbt.sina.model.WeiboException;
import com.cbt.sina.util.BareBonesBrowserLaunch;
import com.cbt.util.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;


/**
 * 实现类控制
 */

@Controller
@RequestMapping("sina")
public class SinaController {

	
    
    @Autowired
    private QuoteInfoService quoteInfoService;
    @Autowired
    private QuoteProductService quoteProductService;
    @Autowired
    private FactoryInfoService factoryInfoService;
    @Autowired
    private FactoryUserService factoryUserService;
    @Autowired
	private SupplierQuoteInfoService supplierQuoteInfoService;



	static Logger logger = LoggerFactory.getLogger(OAuth4Code.class);
	private static String code = "";



	@RequestMapping("sendWeibo")
	public String sendWeibo(HttpServletRequest req, HttpServletResponse resp) {
		Oauth oauth = new Oauth();
		try {
			BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
			logger.info("code: " + code);
			AccessToken accessTokenCode = oauth.getAccessTokenByCode(code);
			String accessToken = accessTokenCode.getAccessToken();
			Timeline timeline = new Timeline(accessToken);


			byte[] bytes = ImageUtil.image2byte("G:\\apache-tomcat-7.0.57\\wtpwebapps\\project_complaint\\1&20181120200117655.png");
			ImageItem imageItem = new ImageItem(bytes);
			Status message = timeline.share("测试微博");

		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * get code 获取新浪回调code
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping("getCode")
	public void getCode(HttpServletRequest req, HttpServletResponse res) {
        //获取code
		code = req.getParameter("code");
	}





		public static void main(String args[]) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException{

			//新浪微博登录页面
			String baseUrl = "https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F";


			//打开
			WebClient webClient = new WebClient(BrowserVersion.CHROME);

			webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");

			//webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.122 Mobile Safari/537.36");

			HtmlPage page = webClient.getPage(baseUrl);

			//等待页面加载
			Thread.sleep(1000);

			//获取输入帐号的控件
			HtmlInput usr = (HtmlInput) page.getElementById("loginName");

			usr.setValueAttribute("panda@sourcing-cn.com");

			//获取输入密码的控件
			HtmlInput pwd = (HtmlInput) page.getElementById("loginPassword");

			pwd.setValueAttribute("KR888888");

			//点击登录
			DomElement button = page.getElementById("loginAction");


			page =(HtmlPage) button.click();


			//等待页面加载
			Thread.sleep(1000);


			//获取到“写微博”这个按钮，因为这个按钮没有name和id,获取所有<a>标签
			DomNodeList<DomElement> button2 = page.getElementsByTagName("a");


			//跳转到发送微博页面
			page =(HtmlPage)button2.get(4).click();

			//等待页面加载
			Thread.sleep(1000);



			//获取发送控件 标签为<a>中的2个
			DomNodeList<DomElement> button3 = page.getElementsByTagName("a");
			//获取文本宇
			HtmlTextArea content =(HtmlTextArea) page.getElementById("txt-publisher");

			DomElement fasong = button3.get(1);

			content.focus();

			Date date = new Date();

			//填写你要发送的内容
			content.setText("使用JAVA发送微博！！！！\n"+date);



			//改变发送按钮的属性，不能无法发送
			fasong.setAttribute("class", "fr txt-link");

			//发送！！！
			page = (HtmlPage)fasong.click();


			Thread.sleep(5000);

			System.out.println(page.asText());


		}

	
}
