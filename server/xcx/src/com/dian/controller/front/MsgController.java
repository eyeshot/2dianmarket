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
import com.dian.core.DianBaseController;
import com.dian.model.sys.Type;
import com.dian.service.sys.CommonService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.DateHandle;
import core.util.DateUtils;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/front/msg/")
public class MsgController extends DianBaseController<Type>  {
	
	
	@Resource 
	private CommonService  commonService;

	Logger logger = Logger.getLogger(MsgController.class);
	
	
	@RequestMapping(value = "/msgList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> msgList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_msg  where  (toopenid='"+openid+"' or fromopenid='"+openid+"') and parentid='0'  order by createtime desc limit "+start+","+pageSize+"";
			List<Object[]> list = commonService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				//如果fromopenid 等于自己 并且 taskid等于 0，则说明这条消息是来自代码买卖的消息，不记录到买代码用户的消息列表中
				if(oneObj[4] != null && oneObj[4].toString().equals(openid) && oneObj[8] != null && oneObj[8].toString().equals("0"))
				{
					continue;
				}
				JSONObject detail = new JSONObject();
				detail.put("id", oneObj[0]);
				detail.put("title", oneObj[1]);
				detail.put("content", oneObj[2]);
				detail.put("fromopenid", oneObj[4]);
				String checkSonSql = "SELECT * FROM market_msg  where toopenid='"+openid+"' and  parentid='"+oneObj[0]+"' and isread=0 ";
				List<Object[]> checkSonList = commonService.doExecuteSql(checkSonSql);
				if(checkSonList != null && checkSonList.size() > 0)
				{
					detail.put("isread", "0");
				} else 
				{
					detail.put("isread", oneObj[6]);
				}
				
				//根据是否有父ID来判断pkcode
				if(oneObj[7] != null && !oneObj[7].toString().equals("0"))
				{
					detail.put("pkcode", oneObj[7]);
				} else 
				{
					detail.put("pkcode", oneObj[0]);
				}
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("msgList has error: ", e);
		}
		return ret;
	}	
	
	@RequestMapping(value = "/msgDetail", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> msgDetail(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String msgid = json.get("msgid");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "123";
		try
		{
			String sql = "SELECT * FROM market_msg  where  id= " + msgid;
			List<Object[]> list = commonService.doExecuteSql(sql);
			
			JSONObject detail = new JSONObject();
			if(list != null && list.size() > 0)
			{
				Object[] oneObj = list.get(0);
				detail.put("id", oneObj[0]);
				detail.put("title", oneObj[1]);
				detail.put("content", oneObj[2]);
				detail.put("fromopenid", oneObj[4]);
				detail.put("isread", oneObj[6]);
				detail.put("taskid", oneObj[8]);
				//如果 parentid和taskid都为0的话，则说明它是一条不需要回复的消息
				if(oneObj[7] != null && oneObj[7].toString().equals("0") && 
						oneObj[8] != null && oneObj[8].toString().equals("0"))
				{
					detail.put("needReport", "0"); //不需要回复
					detail.put("reportOpenid", "");
				} else 
				{
					detail.put("needReport", "1"); //需要回复
					if(oneObj[4] != null && oneObj[4].toString().equals(openid))
					{
						detail.put("reportOpenid", oneObj[3].toString());
					} else 
					{
						detail.put("reportOpenid", oneObj[4].toString());
					}
					
					
				}
				
				if(oneObj[4] != null && oneObj[4].toString().equals(openid))
				{
					detail.put("mymsg", true);
				} else 
				{
					detail.put("mymsg", false);
				}
				
				String wxUserSql = "select nickname, headimg from wxuser where openid='"+oneObj[4]+"'";
				List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
				String nickName = "";
				String heading = "";
				if(wxUserList != null && wxUserList.size() > 0)
				{
					nickName = wxUserList.get(0)[0].toString();
					heading = wxUserList.get(0)[1].toString();
					detail.put("nickname", nickName);
					detail.put("headimg", heading);
				}
			}
			ret.put("data", detail);
			
			//更新该条消息以及该消息下面的所有子消息为已读
			String updateMsgSql = "update market_msg set isread = 1 where (id=" + msgid + " or parentid='"+msgid+"') and toopenid='"+openid+"'" ;
			commonService.doInsertUpdateSql(updateMsgSql);
		} catch(Exception e)
		{
			logger.error("msgList has error: ", e);
		}
		return ret;
	}	
	
	@RequestMapping(value = "/msgDetailList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> msgDetailList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String parentid = json.get("parentid");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "test123";
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM market_msg  where  parentid='"+parentid+"' order by createtime asc ";
			//String sql = "SELECT * FROM market_msg  where  parentid='"+parentid+"' order by createtime asc limit "+start+","+pageSize+"";
			List<Object[]> list = commonService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				String writeTime = DateHandle.format(oneObj[5].toString());
				JSONObject detail = new JSONObject();
				detail.put("id", oneObj[0]);
				detail.put("title", oneObj[1]);
				detail.put("content", oneObj[2] + "(" + writeTime + ")");
				detail.put("fromopenid", oneObj[4]);
				detail.put("isread", oneObj[5]);
				if(oneObj[4] != null && oneObj[4].toString().equals(openid))
				{
					detail.put("mymsg", true);
				} else 
				{
					detail.put("mymsg", false);
				}
				
				String wxUserSql = "select nickname, headimg from wxuser where openid='"+oneObj[4]+"'";
				List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
				String nickName = "";
				String heading = "";
				if(wxUserList != null && wxUserList.size() > 0)
				{
					nickName = wxUserList.get(0)[0].toString();
					heading = wxUserList.get(0)[1].toString();
					detail.put("nickname", nickName);
					detail.put("headimg", heading);
				}
				
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("msgList has error: ", e);
		}
		return ret;
	}	
	
	//消息回复
	@RequestMapping(value = "/msgReport", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> msgReport(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String toOpenID = json.get("fromopenid");
		String content = json.get("content");
		String msgID = json.get("msgid");
		String dianToken = json.get("dian_token");
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		//String openid = "456";
		try
		{
			String title = content;
			String createTime = DateUtils.getDateTime();
			String insertMsgSql = "insert into market_msg (title, content,toopenid, fromopenid,createtime, isread, parentid,taskid) values( '"+title+"','"+content+"','"+toOpenID+"','"+openid+"', '"+createTime+"', '0','"+msgID+"','0')";
			logger.info("insertMsgSql : " + insertMsgSql);
			commonService.doInsertUpdateSql(insertMsgSql);
			ret.put("data", "success");
		} catch(Exception e)
		{
			ret.put("data", "fail");
			logger.error("msgReport has error: ", e);
		}
		return ret;
	}	
	
	@RequestMapping(value = "/checkNewMsg", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> checkNewMsg(HttpServletRequest request, @RequestBody Map<String, String> json)
	{
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			String sql = "SELECT * FROM market_msg  where  toopenid='"+openid+"' and isread='0'";
			List<Object[]> list = commonService.doExecuteSql(sql);
			if(list != null && list.size() > 0)
			{
				ret.put("data", "1"); //存在有未读的消息
			} else 
			{
				ret.put("data", "0"); //不存在有未读的消息
			}
			
			
		} catch(Exception e)
		{
			logger.error("checkNewMsg has error: ", e);
		}
		return ret;
	}	
	
	@RequestMapping(value = "/doChooseOuter", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doChooseOuter(HttpServletRequest request, @RequestBody Map<String, String> json) 
   {
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			String taskID = json.get("taskid");
			
			String toOpenID = json.get("fromopenid");
			String msgID = json.get("msgid");
			
			String dianToken = json.get("dian_token");
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			//String openid = "test123";
			String checkSql = "select outerid,title from market_task where id=" + taskID;
			List<Object[]> checkList = commonService.doExecuteSql(checkSql);
			String taskTitle = "";
			if(checkList != null && checkList.size() > 0)
			{
				Object[] obj = checkList.get(0);
				if(obj[1] != null && obj[1].toString().length() > 0)
				{
					taskTitle = obj[1].toString();
				}
				if(obj[0] != null && obj[0].toString().length() > 0)
				{
					ret.put("data", "choosed"); //已经选定了外包者
					return ret;
				}
				
			}
			//更新项目的接单者ID和项目的状态，现在的状态应该是"开发中"
			String updateSql = "update market_task set outerid='"+toOpenID+"' , status='2' where id=" + taskID;
			logger.info("updateSql : " + updateSql);
			commonService.doInsertUpdateSql(updateSql);
			ret.put("data", "success");
			
			//发送站内信,通知用户中标了
			String wxUserSql = "select nickname, headimg from wxuser where openid='"+openid+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			String nickName = "";
			if(wxUserList != null && wxUserList.size() > 0)
			{
				nickName = wxUserList.get(0)[0].toString();
			}
			String title = "恭喜您，您中标了!"; 
			String content = "您中标的项目是:" + taskTitle + ", 赶快与客户("+nickName+")讨论细节开发吧！(系统消息)";
			
			String createTime = DateUtils.getDateTime();
			String insertMsgSql = "insert into market_msg (title, content,toopenid, fromopenid,createtime, isread, parentid,taskid) values( '"+title+"','"+content+"','"+toOpenID+"','"+openid+"', '"+createTime+"', '0','"+msgID+"','0')";
			logger.info("insertMsgSql : " + insertMsgSql);
			commonService.doInsertUpdateSql(insertMsgSql);
			ret.put("data", "success");
			
		} catch(Exception e)
		{
			logger.error("doColldoChooseOuterect has error:" , e);
		}
		return ret;
	}
	
	
}
