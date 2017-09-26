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
public class DownUpController extends DianBaseController<SysUser>{
	
	Logger logger = Logger.getLogger(DownUpController.class);
	
	@Resource
	private CommonService commonService;
	
	
	@RequestMapping(value = "up")
	public ModelAndView up()
	{
		logger.info("got to up page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxlogin");

		return mav;
	}
	
	@RequestMapping(value = "/upout")
	public ModelAndView upout(HttpServletRequest request, HttpServletResponse response) 
	{
		String token = request.getParameter("token");
		logger.info("login out got to wxlogin page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxlogin");
		if(Login.FULL_SESSION_POOL.containsKey(token)) 
		{
			Login.FULL_SESSION_POOL.remove(token);
		}

		return mav;
	}

	@RequestMapping(value = "/down")
	public ModelAndView down() 
	{
		logger.info("got to wxdown page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxdown");
		mav.addObject("msg", "hello kitty");

		return mav;
	}
	
	@RequestMapping(value = "/downout")
	public ModelAndView loginout(HttpServletRequest request, HttpServletResponse response) 
	{
		String token = request.getParameter("token");
		logger.info("login out got to wxdown page!");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("wxdown");
		if(Login.FULL_SESSION_POOL.containsKey(token)) 
		{
			Login.FULL_SESSION_POOL.remove(token);
		}

		return mav;
	}
	
	@RequestMapping(value = "/downlist")
	@ResponseBody
	public ModelAndView downlist(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mav = new ModelAndView();
		try
		{
			logger.info("got to downlist page!");
			
			String token = request.getParameter("token");
			logger.info("token: " + token);
			if(token == null)
			{
				mav.setViewName("wxdown");
				return mav;
			}
			String openID = Login.FULL_SESSION_POOL.get(token);
			logger.info("openID: " + openID);
			
			if(openID == null || openID.length() == 0)
			{
				mav.setViewName("wxdown");
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
			mav.addObject("nickname", nickName);
			mav.addObject("headimg", avatarurl);
			mav.addObject("token", token);
			
			
			//String openID = "oJLQe0QzKjVxPacQczwQivcsC7oA";
			
			String orderFileSql = "select b.id,b.originalname from payorder a , market_resource b"
					+ " where (a.resourceid=b.id and a.status=1 and b.openid='"+openID+"') or b.id in (select resourceid from market_code where money=0) ";
			List<Object[]> fileList = commonService.doExecuteSql(orderFileSql);
			if(fileList != null && fileList.size() > 0)
			{
				Map<String, String> map = new HashMap<String, String>();
				for(Object[] obj : fileList)
				{
					map.put(obj[0].toString(), obj[1].toString());
				}
				mav.addObject("map", map);
			}
			mav.setViewName("downlist");
			//mav.addObject("token", token);
			
		} catch(Exception e)
		{
			logger.error("go wxhome has error: " , e);
		}
		
		return mav;
	}
	
	@RequestMapping(value="/download",method = RequestMethod.GET)    
	   public @ResponseBody void downloadFile(HttpServletRequest request,HttpServletResponse response) 
			   throws IllegalStateException, IOException 
	{    
		logger.info("begin download...");
		String key = request.getParameter("key");
		String token = request.getParameter("token");
		String openID = Login.FULL_SESSION_POOL.get(token);
		logger.info("openID: " + openID);
		
		if(openID == null || openID.length() == 0)
		{
			PrintWriter pw = response.getWriter();
			try
			{
				pw.write("请先登录");
				pw.close();
			} catch(Exception e)
			{
				
			} finally
			{
				pw.close();
			}
			
			logger.error("download fail as user not login");
			return ;
		}
		String selectResourceSql = "select filename, originalname from market_resource where id=" + key;
		List<Object[]> resList = commonService.doExecuteSql(selectResourceSql);
		if(resList == null || resList.size() <= 0)
		{
			PrintWriter pw = response.getWriter();
			try
			{
				pw.write("资源不存在!");
				pw.close();
			} catch(Exception e)
			{
				
			} finally
			{
				pw.close();
			}
			logger.error("key: " + key);
			logger.error("download fail as resource not exist");
			return ;
		}
		String fileName = resList.get(0)[0].toString();
		String originalName = resList.get(0)[1].toString();
		//String fileName = "20170908103928674135.zip";
		if (fileName != null) {
         String realPath = request.getServletContext().getRealPath("static/code/");
         logger.info("path: " + realPath);
         File file = new File(realPath, fileName);
         if (file.exists()) {
             response.setContentType("application/force-download");// 设置强制下载不打开
             response.addHeader("Content-Disposition",
                     "attachment;fileName=" + originalName);// 设置文件名
             byte[] buffer = new byte[1024];
             FileInputStream fis = null;
             BufferedInputStream bis = null;
             try {
                 fis = new FileInputStream(file);
                 bis = new BufferedInputStream(fis);
                 OutputStream os = response.getOutputStream();
                 int i = bis.read(buffer);
                 while (i != -1) {
                     os.write(buffer, 0, i);
                     i = bis.read(buffer);
                 }
             } catch (Exception e) 
             {
                 logger.error("download file has error", e);
             } finally {
                 if (bis != null) {
                     try {
                         bis.close();
                     } catch (IOException e) 
                     {
                     	logger.error("close bis has error", e);
                     }
                 }
                 if (fis != null) {
                     try {
                         fis.close();
                     } catch (IOException e) 
                     {
                     	logger.error("close fis has error", e);
                     }
                 }
             }
         }
      }
   }
	
	

}
