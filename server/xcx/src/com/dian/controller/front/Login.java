package com.dian.controller.front;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.dian.core.DianBaseController;
import com.dian.model.sys.SysUser;
import com.dian.service.sys.CommonService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.DateUtils;
import core.util.DianUtils;

/**
 * 
 * 
 * @author eyeshot
 * @time 2017年09月06日
 */
@Controller
@RequestMapping("/web/")
public class Login extends DianBaseController<SysUser>{
	
	Logger logger = Logger.getLogger(Login.class);
	
	@Resource
	private CommonService commonService;
	
	public static Map<String, String> FULL_SESSION_POOL = new ConcurrentHashMap<String, String>();

	@RequestMapping(value = "login")
	public ModelAndView login() {
		logger.info("got to Login page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxlogin");
		mav.addObject("msg", "hello kitty");

		return mav;
	}
	
	@RequestMapping(value = "loginout")
	public ModelAndView loginout(HttpServletRequest request, HttpServletResponse response) 
	{
		String token = request.getParameter("token");
		logger.info("got to Login page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxlogin");
		if(FULL_SESSION_POOL.containsKey(token)) 
		{
			FULL_SESSION_POOL.remove(token);
		}

		return mav;
	}
	
	@RequestMapping(value = "wxhome")
	@ResponseBody
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mav = new ModelAndView();
		try
		{
			logger.info("got to home page!");
			String token = request.getParameter("token");
			logger.info("token: " + token);
			if(token == null)
			{
				mav.setViewName("wxlogin");
				return mav;
			}
			String openID = FULL_SESSION_POOL.get(token);
			logger.info("openID: " + openID);
			
			if(openID == null || openID.length() == 0)
			{
				mav.setViewName("wxlogin");
				return mav;
			}
			
			String wxUserSql = "select nickname, headimg from wxuser where openid='"+openID+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			String nickName = "";
			String avatarurl = "";
			if(wxUserList != null && wxUserList.size() > 0)
			{
				nickName = wxUserList.get(0)[0].toString();
				avatarurl = wxUserList.get(0)[1].toString();
			}
			mav.setViewName("wxhome");
			mav.addObject("nickname", nickName);
			mav.addObject("headimg", avatarurl);
			mav.addObject("token", token);
		} catch(Exception e)
		{
			logger.error("go wxhome has error: " + e);
		}
		
		return mav;
		
	}
	
	@RequestMapping("/heart")
	@ResponseBody
	public void doHeart(HttpServletRequest request, HttpServletResponse response) throws IOException 
			
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String jsvalue = request.getParameter("jsvalue");
			logger.info("jsvalue: " + jsvalue);
			if(FULL_SESSION_POOL.containsKey(jsvalue)) //如果缓存中存在该key，则说明用户已经扫描过了
			{
				logger.info("return 1....");
				ret.put("result", "1");
			} else
			{
				ret.put("result", "0");
			}
			
		} catch(Exception e)
		{
			logger.error("doHeart has error:" , e);
		}
		
		writeJSON(response, ret);
	}
	
	@RequestMapping(value = "/scan", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doScan(HttpServletRequest request, @RequestBody Map<String, String> json) 
			
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String dianToken = json.get("dian_token");
			logger.info("dian_token : " + dianToken);
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			logger.info("openID:" + openid);
			String jsvalue = json.get("jsvalue");
			logger.info("scan jsvalue: " + jsvalue);
			
			FULL_SESSION_POOL.put(jsvalue, openid);
			
			ret.put("data", "success");
			
		} catch(Exception e)
		{
			logger.error("doHeart has error:" , e);
		}
		
		return ret;
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public void dotest(HttpServletRequest request, HttpServletResponse response, @RequestBody String param) throws IOException 
			
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String token = request.getParameter("dian_token");
			logger.info("token : " + token);
			String jsvalue = request.getParameter("jsvalue");
			logger.info("jsvalue: " + jsvalue);
			if(FULL_SESSION_POOL.containsKey(jsvalue)) //如果缓存中存在该key，则说明用户已经扫描过了
			{
				ret.put("result", "1");
			} else
			{
				ret.put("result", "0");
			}
			
		} catch(Exception e)
		{
			logger.error("doHeart has error:" , e);
		}
		
		writeJSON(response, ret);
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)    
	   public @ResponseBody void uploadForApp(HttpServletRequest request,HttpServletResponse response,@RequestParam("fileToUpload") MultipartFile file) 
			   throws IllegalStateException, IOException 
	{    
		try
		{
			PrintWriter out = null;
			out = response.getWriter();
			JSONObject obj = new JSONObject();
			logger.info("开始上传文件");
			String token = request.getParameter("token");
			logger.info("token: " + token);
			String openID = FULL_SESSION_POOL.get(token);
			logger.info("openID: " + openID);
			String originalname =  file.getOriginalFilename();
			String fileType = originalname.substring(originalname.lastIndexOf("."));
			final  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String fileName = sdf.format(new Date()) + DianUtils.getRandomString(3) + fileType;
	        
	        File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/code/"));
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
	        logger.info("fileName : " + fileName);
	        // 保存
	        try {
	        	file.transferTo(new File(filePath.getAbsolutePath() + "/" + fileName));
				String rtnNamePath = "/static/code/" + fileName;
	            //file.transferTo(targetFile);
	            String currentTime = DateUtils.getDateTime();
	            //报存上传文件信息到数据库
	            String insertResourceSql = "insert into market_resource (openid, filepath, filename, originalname, createtime) values ('"+openID+"', '"+rtnNamePath+"', '"+fileName+"', '"+originalname+"', '"+currentTime+"')";
	            commonService.doInsertUpdateSql(insertResourceSql);
	        } catch (Exception e) 
	        {
	            logger.error("upload code has error: ", e);
	        }
	        obj.put("msg", "success");
	        out.print(obj);
	        //model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);
		} catch(Exception e)
		{
			logger.error("upload file has error: ", e);
		}
        
       // return ret;  
   }
	

}
