package com.cbt.wximpl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.http.HttpClient;

import com.cbt.entity.User;
import com.cbt.model.AccessToken;
import com.cbt.model.JsapiToken;
import com.cbt.model.SingleAccessToken;
import com.cbt.model.SingleAccessTokenWeb;
import com.cbt.util.Client;
import com.cbt.util.JsonUtil;
import com.cbt.util.SendHttpsRequestUtil;
import com.cbt.util.WriteProp;

public class Wechatimpl {
	//获取access_token接口调用凭证
	/*public static String access_token = SingleAccessToken.getInstance()
			.getAccessToken().getToken();*/
	public static final String token = "cerongwxapp10020";
	public static Long mils = 5000 + System.currentTimeMillis();
	public static String timestamp = timestamp(mils);
	public static Logger logger = LoggerFactory.getLogger(Wechatimpl.class);
	
	// 创建菜单
	public void createmenu() {
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
//		System.out.println(access_token);
		String createmenu_url = WriteProp.get("createmenu_url");
		try {
			String buttonstr = WriteProp.get("button");
//			System.out.println(buttonstr);
			String s1 = JSONObject.fromObject(buttonstr).toString();
			System.err.println("创建菜单中================"+s1+"================================");
			JSONObject s = SendHttpsRequestUtil.sendPostRequest(
					createmenu_url.replace("ACCESS_TOKEN", access_token),
					buttonstr);
//			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("com.cerong.util.wximpl.createmenu  happend Exception"
					+ e);
		}

	}

	// 获取自动回复规则
	public static JSONObject getautosend() {
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
		String url = WriteProp.get("url_autosend");
		JSONObject s = SendHttpsRequestUtil.sendGetRequest(url.replace("ACCESS_TOKEN",
				access_token));
//		System.out.println(s);
		return s;
	}

	// 获取access_token
	public static AccessToken getAccessToken(String appid, String appsecret) {
		String access_token_url = WriteProp.get("access_token_url");
		AccessToken accessToken = null;
		JSONObject json = SendHttpsRequestUtil.sendGetRequest(access_token_url.replace("APPID",
				appid).replace("SECRET", appsecret));
		if (json != null) {
			String access_token = json.getString("access_token");
			Integer expiresIn = json.getInt("expires_in");
			accessToken = new AccessToken();
			accessToken.setToken(access_token);
			accessToken.setExpiresIn(expiresIn);
		}
		return accessToken;
	}
	
	
	
     	// 获取第三方开发平台access_token
		public static AccessToken getComponentAccessToken(String appid, String appsecret,String code) {
			String access_token_url = WriteProp.get("webapp_access_token_url");
			AccessToken accessToken = null;
			JSONObject json = SendHttpsRequestUtil.sendGetRequest(access_token_url.replace("APPID",
					appid).replace("SECRET", appsecret).replace("CODE", code));
			logger.info("获取第三方开发平台AccessToken"+json);
			if (json != null) {
				String access_token = json.getString("access_token");
				Integer expiresIn = json.getInt("expires_in");
				accessToken = new AccessToken();
				accessToken.setToken(access_token);
				accessToken.setExpiresIn(expiresIn);
			}
			return accessToken;
		}
		
	
	
	
	
	/**JS-SDK
	 * 获取jsapi_access_token
	 * @return
	 */
	public static JsapiToken getJsapiToken() {
		
		String access_token = SingleAccessToken.getInstance().getAccessToken()
				.getToken();

		JSONObject json = SendHttpsRequestUtil.sendPostRequest(WriteProp.get(
				"jsapi_ticket_url").replace("ACCESS_TOKEN", access_token),"");
		JsapiToken jsapiToken = new JsapiToken();
		jsapiToken.setAccess_token(json.getString("ticket"));
		jsapiToken.setExpires_in(json.getString("expires_in"));
		return jsapiToken;
	}

	/**
	 * 获取本公众号的关注者openid列表
	 * @return
	 */
	public static JSONObject getAllpenid() {
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
		String url = WriteProp.get("url_openid");
		JSONObject result = SendHttpsRequestUtil.sendGetRequest(url.replace("ACCESS_TOKEN",
				access_token));
		return result;
	}
	
	/**
	 * 在网页进行授权时，通过返回的code得到openid
	 * @param code
	 * @return
	 */
	public static String getOpenidByCode(String code){
		String appid = WriteProp.get("appid");
		String secret = WriteProp.get("secret");
		JSONObject result = SendHttpsRequestUtil.sendGetRequest(WriteProp.get("pageAuthorization_access_token_url").replace("APPID", appid).replace("SECRET", secret)
				.replace("CODE", code));
		if(null != result)
			return result.getString("openid");
		return null;
	}

	// 获取详细信息并保存到列表
	public static User getUserinfo(String openid) throws UnsupportedEncodingException {
		String access_token = SingleAccessToken.getInstance().getAccessToken().getToken();
		String url = WriteProp.get("userinfo");
		JSONObject s = SendHttpsRequestUtil.sendGetRequest(url.replace("ACCESS_TOKEN",
				access_token).replace("OPENID", openid));
//		System.out.println(s);
		String nickname = s.getString("nickname");
		String encodeNickname = URLEncoder.encode(nickname,"utf-8");//昵称进行unicode编码，解决昵称附带表情无法保存问题
		int sex = s.getInt("sex");
		String city = s.getString("city");
		String country = s.getString("country");
		String province = s.getString("province");
		String headimgurl = s.getString("headimgurl");
		long subscribe_time = s.getLong("subscribe_time");
		String language = s.getString("language");
		String remark = s.getString("remark");
		String groupid = s.getString("groupid");
		String tagid_list = s.getString("tagid_list");
		User user = new User();
		user.setCity(city);
		user.setCountry(country);
		user.setGroupid(groupid);
		user.setHeadimgurl(headimgurl);
		user.setLanguage(language);
		user.setNickname(encodeNickname);
		user.setOpenid(openid);
		user.setProvince(province);
		user.setRemark(remark);
		user.setSex("" + sex);
		// 转换为yyyy-mm-dd hh:mm:ss形式
		Timestamp scurrtest = new Timestamp(subscribe_time * 1000);
		user.setSubscribe_time(scurrtest.toString());
		user.setTagid_list(tagid_list);
		return user;
	}

	
	

	// 解析消息返回json字符串
	public static JSONObject hdmessage(HttpServletRequest req) {
		int len = req.getContentLength();
//		System.out.println("数据流长度:" + len);
		JSONObject json = new JSONObject();
		// 获取HTTP请求的输入流
		try {
			InputStream is = req.getInputStream();
			// 已HTTP请求输入流建立一个BufferedReader对象
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			// BufferedReader br = request.getReader();
			// 读取HTTP请求内容
			String buffer = null;
			StringBuffer sb = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				// 在页面中显示读取到的请求参数
				sb.append(buffer);
			}
			json = (JSONObject) new XMLSerializer().read(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	// 解析json字符串返回xml消息给服务器
	public static JSONObject hdmessagetoxml(HttpServletRequest req) {
		int len = req.getContentLength();
//		System.out.println("数据流长度:" + len);
		JSONObject json = new JSONObject();
		// 获取HTTP请求的输入流
		try {
			InputStream is = req.getInputStream();
			// 已HTTP请求输入流建立一个BufferedReader对象
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			// BufferedReader br = request.getReader();
			// 读取HTTP请求内容
			String buffer = null;
			StringBuffer sb = new StringBuffer();
			while ((buffer = br.readLine()) != null) {
				// 在页面中显示读取到的请求参数
				sb.append(buffer);
			}
			json = (JSONObject) new XMLSerializer().read(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	// 获取时间戳
	public static String timestamp(final Long millis) {
		Long l = System.currentTimeMillis();
		if (millis < l) {
			mils = millis + 5000;
			return mils.toString().substring(0, 10);
		}
		return millis.toString().substring(0, 10);
	}

	public static String sig(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		PrintWriter writer = resp.getWriter();
		writer.println("GET " + req.getRequestURL() + " "
				+ req.getQueryString());
		Map<String, String[]> params = req.getParameterMap();
		String queryString = "";
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				queryString += key + "=" + value + "&";
			}
		}
		// 去掉最后一个空格
		queryString = queryString.substring(0, queryString.length() - 1);
		JSONObject json = (JSONObject) new XMLSerializer().read(queryString);
//		System.out.println(json);
		return queryString;
	}

	public static String checkurl(HttpServletRequest req,
			HttpServletResponse resp) {
		// writer.println("GET " + req.getRequestURL() + " "
		// + req.getQueryString());
		Map<String, String[]> params = req.getParameterMap();
		Map<String, String> map = new HashMap<String, String>();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				map.put(key, value);
			}
		}
		JSONObject obj = JSONObject.fromObject(JsonUtil.map2json(map));
		String echostr = obj.getString("echostr");
		return echostr;
	}

	// sha1加密验证url有效性
	public static boolean getsig(String sig, String timestamp, String nonce) {
		StringBuffer b = new StringBuffer();
		String[] test = { token, timestamp, timestamp };
		Arrays.sort(test);
		for (int i = 0; i < test.length; i++) {
			b.append(test[i]);
		}
//		System.out.println(b);
		byte[] t = DigestUtils.sha(b.toString());
		StringBuffer sb = new StringBuffer();
		for (byte b1 : t) {
			sb.append(b1);
		}
		if (sig.equals(sb.toString())) {
			return true;
		} else {
			return false;
		}
	}

	
	
	public static void main(String[] args) {
		Wechatimpl wechatimpl = new Wechatimpl();
		wechatimpl.createmenu();
	}
	
	
	
	
	
	
	//消息模板中去除不正确消息格式
	public static String changeToFormat(String str){				
		//去除json数据中 "map":{}
		String obj = str.split("\"map\":\\{")[1];
		String replace = obj.replaceFirst("}}", "}"); 
		str = str.replace("\"map\":{", "");
		str = str.replace(obj, replace);
		return str;
	}
	
	
	
//	public static int uploadToWechat(String str){	
//		// 上传图文消息的封面图片
//		WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MEDIA_IMAGE, "jpg", inputStream);
//
//		// 上传图文消息的正文图片(返回的url拼在正文的<img>标签中)
//		WxMediaImgUploadResult imagedMediaRes = wxMpService.getMaterialService().mediaImgUpload(file);
//		String url=imagedMediaRes.getUrl();
//
//		WxMpMassNews news = new WxMpMassNews();
//		WxMpMassNews.WxMpMassNewsArticle article1 = new WxMpMassNews.WxMpMassNewsArticle();
//		article1.setTitle("标题1");
//		article1.setContent("内容1");
//		article1.setThumbMediaId(uploadMediaRes.getMediaId());
//		news.addArticle(article1);
//
//		WxMpMassNews.WxMpMassNewsArticle article2 = new WxMpMassNews.WxMpMassNewsArticle();
//		article2.setTitle("标题2");
//		article2.setContent("内容2");
//		article2.setThumbMediaId(uploadMediaRes.getMediaId());
//		article2.setShowCoverPic(true);
//		article2.setAuthor("作者2");
//		article2.setContentSourceUrl("www.baidu.com");
//		article2.setDigest("摘要2");
//		news.addArticle(article2);
//
//		WxMpMassUploadResult massUploadResult = wxMpService.getMassMessageService().massNewsUpload(news);
//
//		WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
//		massMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
//		massMessage.setMediaId(uploadResult.getMediaId());
//		massMessage.getToUsers().add(openid);
//
//		WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
//		
//	}
	
	
	    /**
	     * 上传图片 
	     * 
	     * @param urlStr
	     * @param textMap
	     * @param fileMap
	     * @return
	     */
	    public static String formUpload(String urlStr, File file) {
	        String res = "";
	        HttpURLConnection conn = null;
	        String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
	        try {
	            URL url = new URL(urlStr);
	            conn = (HttpURLConnection) url.openConnection();
	            conn.setConnectTimeout(5000);
	            conn.setReadTimeout(30000);
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
	            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	            OutputStream out = new DataOutputStream(conn.getOutputStream());

	            // file
	            String inputName = "";
	            String filename = file.getName();
	            String contentType = new MimetypesFileTypeMap().getContentType(file);
	            if (filename.endsWith(".png")) {
	                contentType = "image/png";
	            }
	            if (contentType == null || contentType.equals("")) {
	                contentType = "application/octet-stream";
	            }

	            StringBuffer strBuf = new StringBuffer();
	            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
	            strBuf.append(
	                    "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
	            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
	            out.write(strBuf.toString().getBytes());
	            DataInputStream in = new DataInputStream(new FileInputStream(file));
	            int bytes = 0;
	            byte[] bufferOut = new byte[1024];
	            while ((bytes = in.read(bufferOut)) != -1) {
	                out.write(bufferOut, 0, bytes);
	            }
	            in.close();
	            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
	            out.write(endData);
	            out.flush();
	            out.close();

	            // 读取返回数据
	            StringBuffer strBuf2 = new StringBuffer();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                strBuf2.append(line).append("\n");
	            }
	            res = strBuf2.toString();
	            reader.close();
	            reader = null;
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (conn != null) {
	                conn.disconnect();
	                conn = null;
	            }
	        }
	        return res;
	    }

	    
	    
	    
	    
	    /**
	     * 获取图文消息media_id
	     * @Title formUpload 
	     * @Description
	     * @param
	     * @param
	     * @return
	     * @return String
	     */
	    public static String getMediaId(String url, String json) {
	    	
			String news = Client.sendPost(url, json);
			Map returnMap = JsonUtil.jsonToObject(news, Map.class);
			return returnMap.get("media_id").toString();
			
	    }
	    
	    
	    
	    
	    
	    
	    
	    
}
