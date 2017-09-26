package com.dian.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dian.core.Constant;
import com.dian.core.DianBaseController;
import com.dian.model.sys.Type;
import com.dian.service.sys.CommonService;
import com.dian.service.sys.MarketCodeService;
import com.dian.service.sys.MarketTaskImgService;
import com.dian.service.sys.MarketTaskService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.DateHandle;
import core.util.DateUtils;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/front/my/")
public class MyController extends DianBaseController<Type>  {
	
	
	@Resource
	private MarketTaskImgService taskImgService;
	
	@Resource
	private MarketTaskService marketTaskService;
	
	@Resource
	private MarketCodeService marketCodeService;
	
	@Resource
	private CommonService commonService;

	Logger logger = Logger.getLogger(CodeController.class);
	
	
	@RequestMapping(value = "/buyCodeList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> buyCodeList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT a.* FROM market_code a, payorder b where a.id=b.recordid and b.openid='"+openid+"' and b.status=1  order by b.createtime desc limit "+start+","+pageSize+"";
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
						detail.put("type", "项目代码");
					} else 
					{
						detail.put("type", "功能代码");
					}
				}
				detail.put("money", oneObj[4]);
				detail.put("workday", "");
				if(oneObj[5] != null && oneObj[5].toString().length() > 0)
				{
					String writeTime = DateHandle.format(oneObj[5].toString());
					detail.put("writetime", writeTime);
				}
				
				//查询该记录下的图片
				String imgSql = "SELECT * FROM market_taskimg where type=2 and mainid=" + oneObj[0];
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
				
				String checkSql = "select id from market_collect where collectid='"+openid+"' and mainid=" + oneObj[0];
				List<Object[]> checkList = marketTaskService.doExecuteSql(checkSql);
				if(checkList != null && checkList.size() > 0)
				{
					detail.put("isCollect", "1");
				} else 
				{
					detail.put("isCollect", "0");
				}
				
				if(oneObj[7] != null)
				{
					detail.put("resID", oneObj[7].toString());
				} else 
				{
					detail.put("resID", "");
				}
				
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	
	
	@RequestMapping(value = "/myCodeList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getCodeList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_code where openid='"+openid+"'  order by createtime desc limit "+start+","+pageSize+"";
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
						detail.put("type", "项目代码");
					} else 
					{
						detail.put("type", "功能代码");
					}
				}
				detail.put("money", oneObj[4]);
				detail.put("workday", "");
				if(oneObj[5] != null && oneObj[5].toString().length() > 0)
				{
					String writeTime = DateHandle.format(oneObj[5].toString());
					detail.put("writetime", writeTime);
				}
				
				//查询该记录下的图片
				String imgSql = "SELECT * FROM market_taskimg where type=2 and mainid=" + oneObj[0];
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
				
				String checkSql = "select id from market_collect where collectid='"+openid+"' and mainid=" + oneObj[0];
				List<Object[]> checkList = marketTaskService.doExecuteSql(checkSql);
				if(checkList != null && checkList.size() > 0)
				{
					detail.put("isCollect", "1");
				} else 
				{
					detail.put("isCollect", "0");
				}
				
				if(oneObj[7] != null)
				{
					detail.put("resID", oneObj[7].toString());
				} else 
				{
					detail.put("resID", "");
				}
				
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/myTaskList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getTaskList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_task where openid='"+openid+"'  order by createtime desc limit "+start+","+pageSize+"";
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
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	

	@RequestMapping(value = "/moneyList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> moneyList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT title, createtime, money FROM market_sell  where sellid='"+openid+"' order by createtime desc limit "+start+","+pageSize+"";
			List<Object[]> list = marketTaskService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				JSONObject detail = new JSONObject();
				detail.put("title", oneObj[0]);
				detail.put("createtime", oneObj[1]);
				detail.put("money", oneObj[2]);
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/getMoney", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getMoney(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "456";
		try
		{
			String wxUserSql = "select money from wxuser where openid='"+openid+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			JSONObject detail = new JSONObject();
			if(wxUserList != null && wxUserList.size() > 0)
			{
				Object obj = wxUserList.get(0);
				if(obj == null || (int) obj  == 0)
				{
					detail.put("money", "0.00");
				} else 
				{
					detail.put("money", obj);
				}
				
			}
			ret.put("data", detail);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/getCash", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getCash(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "456";
		String cash = json.get("cash");
		if(cash == null)
		{
			logger.error("cash is null, return fail.");
			ret.put("data", "fail");
			return ret;
		}
		try
		{
			String wxUserSql = "select money from wxuser where openid='"+openid+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			if(wxUserList != null && wxUserList.size() > 0)
			{
				Object obj = wxUserList.get(0);
				if((int) obj  < Integer.valueOf(cash)) //提取金额超限
				{
					logger.error("cash is bigger than have, return fail.");
					ret.put("data", "fail");
					return ret;
				} 
			}
			//记录提现记录
			String currentTime = DateUtils.getDateTime();
			String insertCashSql = "insert into market_getcash (openid, money, createtime, status) values ('"+openid+"', "+cash+", '"+currentTime+"', '0')";
			logger.info("insertCashSql:" + insertCashSql);
			commonService.doInsertUpdateSql(insertCashSql);
			
			//修改用户的金额
			String updateMoneySql = "update wxuser set money=money - "+cash+"   where openid='"+openid+"'";
    		logger.info("updateMoneySql : " + updateMoneySql);
    		commonService.doInsertUpdateSql(updateMoneySql);
			
			ret.put("data", "success");
		} catch(Exception e)
		{
			logger.error("getCash has error: ", e);
		}
		return ret;
	}

	
}
