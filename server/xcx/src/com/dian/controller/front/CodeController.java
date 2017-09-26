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
import com.dian.model.sys.MarketCode;
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
@RequestMapping("/front/code/")
public class CodeController extends DianBaseController<Type>  {
	
	
	@Resource
	private MarketTaskImgService taskImgService;
	
	@Resource
	private MarketTaskService marketTaskService;
	
	@Resource
	private MarketCodeService marketCodeService;
	
	@Resource
	private CommonService commonService;

	Logger logger = Logger.getLogger(CodeController.class);
	
	
	
	@RequestMapping(value = "/saveCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doSaveCode(HttpServletRequest request, @RequestBody Map<String, String> json
			) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			
			String title = json.get("title");
			String money = json.get("money");
			String content = json.get("content");
			String itemTypeIndex = json.get("itemTypeIndex");
			String fileName = json.get("fileName");
			String fileID = json.get("fileID");
			
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			
			MarketCode mt = new MarketCode();
			mt.setTitle(title);
			mt.setDes(content);
			mt.setMoney(money);
			mt.setType(itemTypeIndex);
			mt.setCreatetime(DateUtils.getDateTime());
			mt.setOpenid(openid);
			mt.setResourceid(fileID);
			mt.setResourcename(fileName);
			marketCodeService.persist(mt);
			logger.info("id: " + mt.getId());
			ret.put("data", mt.getId());
			
		} catch(Exception e)
		{
			logger.error("doSaveCode has error:" , e);
		}
		return ret;
	}
	
	@RequestMapping(value = "/doDeleteCode", method = RequestMethod.POST)
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
			String checkSql = "delete from market_code where openid='"+openid+"' and id=" + mainID;
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
	
	@RequestMapping(value = "/getCodeList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getCodeList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String keyword = json.get("keyword");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "test123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_code  order by createtime desc limit "+start+","+pageSize+"";
			if(keyword != null && keyword.length() > 0)
			{
				sql = "SELECT * FROM market_code where title like '%"+keyword+"%' or des like '%"+keyword+"%' order by createtime desc limit "+start+","+pageSize+"";
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
				
				String wxUserSql = "select nickname, headimg from wxuser where openid='"+oneObj[6].toString()+"'";
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
	
	
	@RequestMapping(value = "/getFileList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getFileList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "oJLQe0QzKjVxPacQczwQivcsC7oA";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_resource where openid='"+openid+"' order by createtime desc limit "+start+","+pageSize+"";
			List<Object[]> list = marketTaskService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				JSONObject detail = new JSONObject();
				detail.put("id", oneObj[0]);
				detail.put("name", oneObj[4]);
				detail.put("createtime", oneObj[5]);
				jsonArr.add(detail);
				
			}
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getFileList has error: ", e);
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
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			String mainID = json.get("mainID");
			String createTime = DateUtils.getDateTime();
			
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
	
}
