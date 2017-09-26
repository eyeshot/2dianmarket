package com.dian.controller.front;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dian.core.Constant;
import com.dian.core.DianBaseController;
import com.dian.model.sys.MarketTask;
import com.dian.model.sys.MarketTaskImg;
import com.dian.model.sys.Type;
import com.dian.service.sys.CommonService;
import com.dian.service.sys.MarketTaskImgService;
import com.dian.service.sys.MarketTaskService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.DateHandle;
import core.util.DateUtils;
import core.util.DianUtils;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/front/upload/")
public class FileController extends DianBaseController<Type>  {
	
	
	@Resource
	private MarketTaskImgService taskImgService;
	
	@Resource
	private MarketTaskService marketTaskService;
	
	@Resource
	private CommonService commonService;

	Logger logger = Logger.getLogger(FileController.class);
	
	@RequestMapping(value = "/img", method = RequestMethod.POST, headers={"content-type=multipart/form-data"} , consumes = "multipart/form-data")
	@ResponseBody
	public Map<String, Object> doUploadImg(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@RequestParam("parentID") String parentID,
			@RequestParam("temppaths") String temppaths,
			@RequestParam("imgType") String imgType
			) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String format = "jpg";
			if(temppaths != null && temppaths.length() > 0)
			{
				format = temppaths.split("\\.")[1];
				logger.info("format : " + format);
			}
			final  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String fileName = sdf.format(new Date()) + DianUtils.getRandomString(3) + "." + format;
			String fileDirectory = DateFormatUtils.format(new Date(), "yyyyMM");
			File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/task/upload/" + fileDirectory));
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.transferTo(new File(filePath.getAbsolutePath() + "/" + fileName));
			String rtnName = "/static/task/upload/" + fileDirectory + "/" + fileName;
			
			MarketTaskImg img = new MarketTaskImg();
			img.setMainid(new Long(parentID));
			img.setImg("".getBytes());
			img.setType(imgType);
			img.setTemppath("");
			img.setImgpath(rtnName);
			taskImgService.persist(img);
			ret.put("data", "success");
			
		} catch(Exception e)
		{
			logger.error("doUploadImg has error:" , e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/saveTask", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doSaveTask(HttpServletRequest request, @RequestBody Map<String, String> json
			) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			
			String title = json.get("title");
			String itemTypeIndex = json.get("itemTypeIndex");
			String money = json.get("money");
			String workday = json.get("workday");
			String content = json.get("content");
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			
			MarketTask mt = new MarketTask();
			mt.setTitle(title);
			mt.setDes(content);
			mt.setType(itemTypeIndex);
			mt.setMoney(money);
			mt.setUsetime(workday);
			mt.setCreatetime(DateUtils.getDateTime());
			mt.setStatus("1");
			mt.setOpenid(openid);
			marketTaskService.persist(mt);
			logger.info("id: " + mt.getId());
			ret.put("data", mt.getId());
			
		} catch(Exception e)
		{
			logger.error("doUploadImg has error:" , e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/getTaskList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getTaskList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String keyword = json.get("keyword");
		String dianToken = json.get("dian_token");
		logger.info("dianToken: " + dianToken);
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "test123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_task  order by createtime desc limit "+start+","+pageSize+"";
			if(keyword != null && keyword.length() > 0)
			{
				sql = "SELECT * FROM market_task where title like '%"+keyword+"%' or des like '%"+keyword+"%' order by createtime desc limit "+start+","+pageSize+"";
			}
			List<Object[]> list = marketTaskService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				JSONObject detail = new JSONObject();
				detail.put("id", oneObj[0]);
				detail.put("title", oneObj[1]);
				detail.put("des", oneObj[2]);
				if(oneObj[3] != null && oneObj[3].toString().length() > 0)
				{
					if(oneObj[3].toString().equals("0"))
					{
						detail.put("type", "项目外包");
					} else 
					{
						detail.put("type", "功能外包");
					}
				}
				detail.put("money", oneObj[4]);
				detail.put("workday", oneObj[5]);
				if(oneObj[6] != null && oneObj[6].toString().length() > 0)
				{
					String writeTime = DateHandle.format(oneObj[6].toString());
					detail.put("writetime", writeTime);
				}
				
				if(oneObj[7] != null && oneObj[7].toString().length() > 0)
				{
					if(oneObj[7].toString().equals("1"))
					{
						detail.put("status", "竞标中");
					} else if(oneObj[7].toString().equals("2"))
					{
						detail.put("status", "开发中");
					} else if(oneObj[7].toString().equals("3"))
					{
						detail.put("status", "已完成");
					}
				}
				
				if(oneObj[8] != null && oneObj[8].toString().length() > 0)
				{
					detail.put("onlyID", oneObj[8].toString());
				}
				
				//查询该记录下的图片
				String imgSql = "SELECT * FROM market_taskimg where type= 1 and mainid=" + oneObj[0];
				List<Object[]> imgList = taskImgService.doExecuteSql(imgSql);
				JSONArray  imgArr = new JSONArray();
				int imgIndex = 0;
				if(imgList != null && imgList.size() > 0)
				{
					for(Object[] oneImg : imgList)
					{
						JSONObject imgDetail = new JSONObject();
						imgDetail.put("img", Constant.HOST + oneImg[5]);
						imgDetail.put("imgID", oneObj[0].toString() + imgIndex);
						imgArr.add(imgDetail);
						imgIndex++;
					}
				}
				detail.put("imglist", imgArr);
				
				//查询该记录下的竞标者
				String bidderSql = "SELECT b.headimg FROM market_bidder a, wxuser b  where a.mainid=" + oneObj[0] + " and a.applyid=b.openid";
				List<Object[]> bidderList = taskImgService.doExecuteSql(bidderSql);
				JSONArray  bidderArr = new JSONArray();
				int headimgIndex = 0;
				if(bidderList != null && bidderList.size() > 0)
				{
					for(int i = 0; i < bidderList.size(); i++)
					{
						JSONObject oneDetail = new JSONObject();
						oneDetail.put("headimg", bidderList.get(i));
						oneDetail.put("headimgID", "headimg" + oneObj[0].toString() + imgIndex);
						bidderArr.add(oneDetail);
					}
				}
				detail.put("bidderlist", bidderArr);
				
				String checkSql = "select id from market_collect where collectid='"+openid+"' and mainid=" + oneObj[0];
				List<Object[]> checkList = marketTaskService.doExecuteSql(checkSql);
				if(checkList != null && checkList.size() > 0)
				{
					detail.put("isCollect", "1");
				} else 
				{
					detail.put("isCollect", "0");
				}
				
				String wxUserSql = "select nickname, headimg from wxuser where openid='"+oneObj[8].toString()+"'";
				List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
				String nickName = "";
				String headimg = "";
				if(wxUserList != null && wxUserList.size() > 0)
				{
					nickName = wxUserList.get(0)[0].toString();
					headimg = wxUserList.get(0)[1].toString();
				}
				
				detail.put("nickName", nickName);
				detail.put("headimg", headimg);
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/doCollect", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doCollect(HttpServletRequest request, @RequestBody Map<String, String> json) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String mainID = json.get("mainID");
			//String userOpenid = "test123";
			String createTime = DateUtils.getDateTime();
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			
			//String loginOpenID = "test123";
			String checkSql = "select id from market_collect where collectid='"+openid+"' and mainid=" + mainID;
			List<Object[]> checkList = marketTaskService.doExecuteSql(checkSql);
			if(checkList != null && checkList.size() > 0)
			{
				String deleteSql = "delete from market_collect where collectid='"+openid+"' and mainid=" + mainID;
				marketTaskService.doInsertUpdateSql(deleteSql);
				ret.put("data", "remove");
				return ret;
			}
			
			
			String insertCollectSql = "insert into market_collect (collectid, mainid, createtime) values('"+openid+"', "+mainID+", '"+createTime+"')";
			logger.info("insertCollectSql : " + insertCollectSql);
			marketTaskService.doInsertUpdateSql(insertCollectSql);
			ret.put("data", "success");
			
		} catch(Exception e)
		{
			logger.error("doCollect has error:" , e);
		}
		return ret;
	}
	
	
	@RequestMapping(value = "/doDeleteMarket", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doDeleteMarket(HttpServletRequest request, @RequestBody Map<String, String> json) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String mainID = json.get("mainID");
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			//String openid = "123";
			String checkSql = "delete from market_task where openid='"+openid+"' and id=" + mainID;
			int num = marketTaskService.doInsertUpdateSql(checkSql);
			if(num > 0)
			{
				ret.put("data", "success");
			} else 
			{
				ret.put("data", "fail");
			}
			
		} catch(Exception e)
		{
			logger.error("doDeleteMarket has error:" , e);
			ret.put("data", "fail");
		}
		return ret;
	}
	
	
	@RequestMapping(value = "/doPartake", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doPartake(HttpServletRequest request, @RequestBody Map<String, String> json) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			String mainID = json.get("mainID");
			String content = json.get("content");
			String toOpenID = json.get("onlyID");
			//String userOpenid = "test123";
			String createTime = DateUtils.getDateTime();
			logger.info("toOpenID:" + toOpenID);
			
			//String openid = "test123";
			String checkSql = "select id from market_bidder where applyid='"+openid+"' and mainid=" + mainID;
			List<Object[]> checkList = marketTaskService.doExecuteSql(checkSql);
			if(checkList != null && checkList.size() > 0)
			{
				ret.put("data", "applyed");//本人已经申请过了
				return ret;
			}
			
			if(content == null || content.length() <= 0)
			{
				ret.put("data", "blank");
				return ret;
			}
			
			String checkOuterSql = "select outerid from market_task where id=" + mainID;
			List<Object[]> outerList = marketTaskService.doExecuteSql(checkOuterSql);
			if(outerList != null && outerList.size() > 0)
			{
				Object outeridObj = outerList.get(0);
				String outerID = (String)outeridObj;
				String outerid = outerList.get(0) != null ?(String)outeridObj : "";
				if(!outerid.equals(""))
				{
					ret.put("data", "closed"); //该单已经被其他人申请了
					return ret;
				}
			}
			
			
			String insertSql = "insert into market_bidder (mainid, applyid,advantage, applytime,isuser) values( "+mainID+",'"+openid+"','"+content+"', '"+createTime+"', '0')";
			logger.info("insertMarketBidderSql : " + insertSql);
			marketTaskService.doInsertUpdateSql(insertSql);
			ret.put("data", "success");
			
			//发送站内信
			String wxUserSql = "select nickname, headimg from wxuser where openid='"+openid+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			String nickName = "";
			if(wxUserList != null && wxUserList.size() > 0)
			{
				nickName = wxUserList.get(0)[0].toString();
			}
			String title = "用户" + nickName + "竞标了项目!"; 
			content = "竞争优势是:" + content;
			String insertMsgSql = "insert into market_msg (title, content,toopenid, fromopenid,createtime, isread, parentid,taskid) values( '"+title+"','"+content+"','"+toOpenID+"','"+openid+"', '"+createTime+"', '0','0','"+mainID+"')";
			logger.info("insertMsgSql : " + insertMsgSql);
			marketTaskService.doInsertUpdateSql(insertMsgSql);
			
		} catch(Exception e)
		{
			logger.error("doPartake has error:" , e);
		}
		return ret;
	}
	
}
