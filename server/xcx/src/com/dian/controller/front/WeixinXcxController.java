package com.dian.controller.front;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dian.model.sys.WxUser;
import com.dian.service.sys.CommonService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.Constant;
import core.util.DateUtils;
import core.util.HttpUtil;
import core.util.WeixinUtil;

@Controller
@RequestMapping("/login/")
public class WeixinXcxController {

	
	@Resource
	private CommonService commonService;
	
	private static Logger logger = Logger.getLogger(WeixinXcxController.class);
	
	private static Map<String, String> cacheMap = new HashMap<String, String>();
	
	private static long userInfoAccessTokenExpireTime = 7200;	
	
	/**
	 * 
	 * 获取用户基本信息的接口(两点市场)
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value="goWebchat",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> doWebChat2(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> ret = new HashMap<String, String>();
		String mycode = request.getParameter("code");
		logger.info("wxCode: " + mycode);
		String session_key = "";
		try
		{
			String resData = HttpUtil.requestCommonByHttpGet("https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.XCX_SHANG_APPID+"&secret="+Constant.XCX_SHANG_SECRET+"&js_code="+mycode+"&grant_type=authorization_code", null, true);
			logger.info("request jscode2session: " + resData);	
			JSONObject accessObj = JSONObject.parseObject(resData);
			session_key = accessObj.getString("session_key");
			String openid = accessObj.getString("openid");
			//String openid = "oswcO0eJrrPRkZHjLEfypgJiL24o";
			//session_key = "abc"; 
			String thirdSession = getRandomStr(); //第三方session
			logger.info("thirdSession: " + thirdSession);
			//CacheUtil.getCacheMap().put(thirdSession, openid + "#" + session_key);
			OpenIDCache.getCacheMap().put(thirdSession, openid + "#" + session_key);
			
			ret.put("code", thirdSession);
			ret.put("statusCode", "200");
		} catch (Exception e)
		{
			logger.error("webchat interface has error:", e);
		}
		return ret;
	}
	
	/**
	 * 
	 * 解密微信用户加密信息入数据库
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value="decodeWxInfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> decodeWxInfo(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, String> ret = new HashMap<String, String>();
		try
		{
			String dianToken = request.getParameter("dianToken");
			String encryptedData = request.getParameter("encryptedData");
			String iv = request.getParameter("iv");
			logger.info("thirdKey : " + dianToken);
			logger.info("encryptedData: " + encryptedData);
			logger.info("iv: " + iv);
			String sessionKey = OpenIDCache.getCacheMap().get(dianToken).split("#")[1];
			logger.info("sessionKey : " + sessionKey);
			JSONObject obj = WeixinUtil.getUserInfo(encryptedData, sessionKey, iv);
			JSONObject watermarkObj = obj.getJSONObject("watermark");
			String appid = watermarkObj.getString("appid");
			String nickName = obj.getString("nickName");
			String avatarUrl = obj.getString("avatarUrl");
			String gender = obj.getString("gender");
			String province = obj.getString("province");
			String city = obj.getString("city");
			String openId = obj.getString("openId");
			
			//保存微信用户信息
			String wxUserSql = "select nickname from wxuser where openid='"+openId+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			if(wxUserList == null || wxUserList.size() <= 0)
			{
				String insertWxUserSql = "insert into wxuser (accesstoken, openid, nickname, headimg, createtime, provice, city, money) "
						+ "values ('"+sessionKey+"', '"+openId+"', '"+nickName+"', '"+avatarUrl+"', '"+DateUtils.getDateTime()+"', '"+province+"', '"+city+"', 0)";
				logger.info(insertWxUserSql);
				commonService.doInsertUpdateSql(insertWxUserSql);
			}
		} catch(Exception e)
		{
			logger.error("decodeWxInfo has error:", e);
		}
		
		
		return ret;
	}
	
	
	//获取随机的16位字符串
	public static String getRandomStr()
	{
		String originStr = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 16; i++)
		{
			int randomNumber = new Random().nextInt(46);
			sb.append(originStr.charAt(randomNumber));
		}
		logger.info("random str : " + sb.toString());
		//System.out.println(sb.toString());
		//System.out.println(sb.toString().length());
		return sb.toString();
	}
	
	public static String encodeUrl(String url)
	{
		String returnStr = "";
		try
		{
			returnStr = URLEncoder.encode(url, "utf-8");
		} catch(Exception e)
		{
			
		}
		return returnStr;
	}
	
	
	
	
	public static void main(String[] args) 
	{
		System.out.println(encodeUrl("http://www.2dian.com/spriderproject/wx/webchat?actID=3"));
	}
	

}
