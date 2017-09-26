package com.dian.controller.front;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dian.model.sys.Payorder;
import com.dian.model.sys.WxUser;
import com.dian.service.sys.CommonService;

import core.cache.OpenIDCache;
import core.util.CacheUtil;
import core.util.Constant;
import core.util.DateUtils;
import core.util.Encrypt;
import core.util.HttpUtil;
import core.util.StringUtil;
import core.util.XmlUtil;
import core.wx.IDGenerator;

@Controller
@RequestMapping("/pay/")
public class WeixinPayController {
	
	@Resource
	private CommonService commonService;

	
	private static Logger logger = Logger.getLogger(WeixinPayController.class);
	
	private static Map<String, String> cacheMap = new HashMap<String, String>();
	
	private static Map<String, Map<String, Object>> FULL_SESSION_POOL = new ConcurrentHashMap<>();
	
	/**
	 * 
	 * 购买前检查是否购买过接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="doCheckOrder",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> doCheckOrder(HttpServletRequest request,HttpServletResponse response)
	{
		Map<String, String> ret = new HashMap<String, String>();
		String dianToken = request.getParameter("dian_token");
		String recordid = request.getParameter("recordid");
		String resid = request.getParameter("resid");
		
		try
		{
			logger.info("dian_token : " + dianToken);
			String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
			//String openid = "oJLQe0QzKjVxPacQczwQivcsC7oA";
			String checkSql = "select id from payorder where openid='"+openid+"' and resourceid='"+resid+"' and recordid='"+recordid+"'";
			List<Object[]> checkList = commonService.doExecuteSql(checkSql);
			if(checkList != null && checkList.size() > 0)
			{
				ret.put("data", "1");
			} else 
			{
				ret.put("data", "0");
			}
		} catch(Exception e)
		{
			logger.error("doCheckOrder has error:", e);
		}
		
		return ret;
	}
	
	
	/**
	 * 
	 * 微信统一下单接口
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value="doPreOrder",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> doPreOrder(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> ret = new HashMap<String, String>();
		String total = request.getParameter("total");
		String dianToken = request.getParameter("dian_token");
		String recordid = request.getParameter("recordid");
		String resid = request.getParameter("resid");
		
		logger.info("dian_token : " + dianToken);
		//CacheUtil.getCacheMap().put(thirdSession, openid + "#" + session_key);
		String openid = OpenIDCache.getCacheMap().get(dianToken).split("#")[0];
		logger.info("openid : " + openid);
		logger.info("total: " + total);
		logger.info("recordid: " + recordid);
		logger.info("resid: " + resid);
		try
		{
			String nonce_str = IDGenerator.next();
			String body = "购买代码";
			//根据日期和随机数生成订单号
			String out_trade_no = DateUtils.getDateTime("yyyyMMddHHmmss") + StringUtil.getRandomStr(10000);
			CacheUtil.getCacheMap().put(out_trade_no, total);
			String spbill_create_ip = request.getRemoteAddr();
			String time_start = DateUtils.getDateTime("yyyyMMddHHmmss");
			String time_expire = DateUtils.formatDate(DateUtils.getDatetimeAfterMinute(new Date(), 10), "yyyyMMddHHmmss");
			String notify_url = "https://www.2dian.com/xcx/pay/callback.action";
			String trade_type = "JSAPI";
			//String orderNumber = DateUtils.getDateTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "") + get10RandomStr();
			Map<String, Object> paramMap = new TreeMap<String, Object>();
			paramMap.put("appid", Constant.XCX_SHANG_APPID);
			paramMap.put("mch_id", Constant.XCX_SHANG_SHANGHUHAO); 
			paramMap.put("nonce_str", nonce_str);
			paramMap.put("body", body);
			paramMap.put("out_trade_no", out_trade_no);
			paramMap.put("total_fee", total);
			paramMap.put("spbill_create_ip", spbill_create_ip);
			paramMap.put("time_start", time_start);
			paramMap.put("time_expire", time_expire);
			paramMap.put("notify_url", notify_url);
			paramMap.put("trade_type", trade_type);
			paramMap.put("openid", openid);
			StringBuffer signStr = new StringBuffer();
			for(Entry<String, Object> entry : paramMap.entrySet())
			{
				signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			signStr.append("key=").append(Constant.XCX_SHANG_KEY);
			logger.info("signStr :" + signStr);
			
			//String sign = Encrypt.md5(signStr.toString().substring(0, signStr.length() - 1)).toUpperCase();
			String sign = Encrypt.md5(signStr.toString()).toUpperCase();
			paramMap.put("sign", sign);
			logger.info("sign: " + sign);
			
			logger.info("orderPre paramMap: " + paramMap.toString());
			
			Document doc = DocumentHelper.createDocument();
			Element rootElement = DocumentHelper.createElement("xml");
			doc.add(rootElement);
			Element appIDEle = DocumentHelper.createElement("appid");
			appIDEle.addText(Constant.XCX_SHANG_APPID);
			rootElement.add(appIDEle);
			
			Element mchIDEle = DocumentHelper.createElement("mch_id");
			mchIDEle.addText(Constant.XCX_SHANG_SHANGHUHAO);
			rootElement.add(mchIDEle);
			
			Element bodyEle = DocumentHelper.createElement("body");
			bodyEle.addText(body);
			rootElement.add(bodyEle);
			
			Element nonceEle = DocumentHelper.createElement("nonce_str");
			nonceEle.addText(nonce_str);
			rootElement.add(nonceEle);
			
			Element notifyUrlEle = DocumentHelper.createElement("notify_url");
			notifyUrlEle.addText(notify_url);
			rootElement.add(notifyUrlEle);
			
			Element outTradeNo = DocumentHelper.createElement("out_trade_no");
			outTradeNo.addText(out_trade_no);
			rootElement.add(outTradeNo);
			
			Element totalFeeEle = DocumentHelper.createElement("total_fee");
			totalFeeEle.addText(total);
			rootElement.add(totalFeeEle);
			
			Element spbillCreateIp = DocumentHelper.createElement("spbill_create_ip");
			spbillCreateIp.addText(spbill_create_ip);
			rootElement.add(spbillCreateIp);
			
			Element timeStartEle = DocumentHelper.createElement("time_start");
			timeStartEle.addText(time_start);
			rootElement.add(timeStartEle);
			
			Element timeExpireEle = DocumentHelper.createElement("time_expire");
			timeExpireEle.addText(time_expire);
			rootElement.add(timeExpireEle);
			
			Element tradeTypeEle = DocumentHelper.createElement("trade_type");
			tradeTypeEle.addText(trade_type);
			rootElement.add(tradeTypeEle);
			
			Element signEle = DocumentHelper.createElement("sign");
			signEle.addText(sign);
			rootElement.add(signEle);
			
			Element openIDEle = DocumentHelper.createElement("openid");
			openIDEle.addText(openid);
			rootElement.add(openIDEle);
			
			logger.info("input xml:" + doc.asXML());
			
			//统一下单
			String resData = HttpUtil.requestCommonPostXml(Constant.WEIXIN_COMMON_ORDER_API, doc.asXML());
			logger.info("统一下单 返回值: " + resData);	
			
			try {
				Document rtnDoc = DocumentHelper.parseText(resData);
				Element rtnRoot = rtnDoc.getRootElement(); 
		        Element appid = rtnRoot.element("appid");
		        Element rtnNonceEle = rtnRoot.element("nonce_str");
		        Element prepayIdEle = rtnRoot.element("prepay_id");
		        Element paySignEle = rtnRoot.element("sign");
		        String currentTime = String.valueOf(System.currentTimeMillis());
		        //注意微信调起的支付sign与上面的请求支付sign是不同的
		        String paramStr = "appId="+Constant.XCX_SHANG_APPID+"&nonceStr="+rtnNonceEle.getText()+"&package=prepay_id="+prepayIdEle.getText()+"&signType=MD5&timeStamp="+currentTime+"&key=" + Constant.XCX_SHANG_KEY;
		        logger.info("weixin pay param String: " + paramStr);
		        String paySign = Encrypt.md5(paramStr).toUpperCase();
		        logger.info("paySign: " + paySign);
		        JSONObject rtnObj = new JSONObject();
				rtnObj.put("nonce", rtnNonceEle.getText());
				rtnObj.put("timestamp", currentTime);
				rtnObj.put("package", prepayIdEle.getText());
				rtnObj.put("paySign", paySign);
				
				ret.put("data", rtnObj.toJSONString());
			} catch (DocumentException e) 
			{
				logger.error("parse prorder xml has error:", e);
			}
			String wxUserSql = "select nickname, headimg from wxuser where openid='"+openid+"'";
			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			String nickName = "";
			String avatarurl = "";
			if(wxUserList != null && wxUserList.size() > 0)
			{
				nickName = wxUserList.get(0)[0].toString();
				avatarurl = wxUserList.get(0)[1].toString();
			}
			//保存订单到数据库
			String insertOrderSql = "insert into payorder (openid, appid, nickname, avatarurl, body, ordernum,"
					+ " totalfee, createip, createtime, tradetype, status, resourceid, recordid) values ('"+openid+"','"+Constant.XCX_SHANG_APPID+"', '"+nickName+"',"
							+ " '"+avatarurl+"', '"+body+"', '"+out_trade_no+"', '"+total+"', '"+spbill_create_ip+"', '"+DateUtils.getDateTime()+"','"+trade_type+"', '0','"+resid+"','"+recordid+"')";
			logger.info("insertOrderSql: " + insertOrderSql);
			commonService.doInsertUpdateSql(insertOrderSql);
			
		} catch (Exception e)
		{
			logger.error("doPreOrder interface has error:", e);
		}
		return ret;
	}
	
	/**
	 * 
	 * 微信统一下单接口
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(value="callback",method = RequestMethod.POST)
	@ResponseBody
	public void payCallBack(HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("pay callback invoked...");
		InputStream is = null;
		try {
			//byte[] buffer = new byte[1024];
			//request.getInputStream().read(buffer);
			is = request.getInputStream();
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}
			String weixinBackInfo =  result.toString("UTF-8");
			logger.info("weixinBackInfo:" + new String(weixinBackInfo));
			
			//开始解析微信回调结果
			Document rtnDoc = DocumentHelper.parseText(weixinBackInfo.trim());
			Element rtnRoot = rtnDoc.getRootElement(); 
	        Element returnCodeEle = rtnRoot.element("return_code");
	        Element totalFeeEle = rtnRoot.element("total_fee");
	        Element signEle = rtnRoot.element("sign");
	        Element outTradeNoEle = rtnRoot.element("out_trade_no"); //订单号
	        //判断微信回调结果是否成功
	        if(returnCodeEle != null && returnCodeEle.getText() != null && returnCodeEle.getText().equals("SUCCESS"))
	        {
	        	//向微信反馈最终结果
				Document doc = DocumentHelper.createDocument();
				Element rootElement = DocumentHelper.createElement("xml");
				doc.add(rootElement);
				
	        	String weixinBackSign = XmlUtil.getWeixinMd5(weixinBackInfo.trim());
	        	if(!weixinBackSign.equals(signEle.getText())) //sign的二次校验
	        	{
	        		logger.error("二次签名失败，通知微信!!!");
	        		Element rtnEle = DocumentHelper.createElement("return_code");
					rtnEle.addText("FAIL");
					rootElement.add(rtnEle);
					
					Element rtnMsgEle = DocumentHelper.createElement("return_msg");
					rtnMsgEle.addText("签名失败");
					rootElement.add(rtnMsgEle);
					response.getWriter().write(doc.asXML());
					return;
	        	}
	        	String totalFee = CacheUtil.getCacheMap().get(outTradeNoEle.getText());
	        	if(totalFee == null || !totalFee.equals(totalFeeEle.getText()))
	        	{
	        		logger.error("支付金额验证失败，通知微信!!!");
	        		Element rtnEle = DocumentHelper.createElement("return_code");
					rtnEle.addText("FAIL");
					rootElement.add(rtnEle);
					
					Element rtnMsgEle = DocumentHelper.createElement("return_msg");
					rtnMsgEle.addText("金额验证失败");
					rootElement.add(rtnMsgEle);
					response.getWriter().write(doc.asXML());
					return;
	        	}
	        	
	        	//根据订单号查询该订单
	        	String selectOrderSql = "select * from payorder where ordernum='"+outTradeNoEle.getText()+"'";
	        	List<Object[]> orderList = commonService.doExecuteSql(selectOrderSql);
	        	if(orderList != null && orderList.size() > 0)
	        	{
	        		logger.info("微信回调校验成功,修改订单状态成功");
	        		logger.info("order Info: " + orderList.get(0));
	        		//修改成已确认
	        		String updateOrderSql = "update payorder set status=1 where ordernum='"+outTradeNoEle.getText()+"'";
	        		logger.info("updateOrderSql : " + updateOrderSql);
	        		commonService.doInsertUpdateSql(updateOrderSql);
	        		try
	        		{
	        			//发送站内信,通知用户代码卖出了一份
	        			String codeSql = "select openid, resourcename, money, resourceid from market_code where id=" + orderList.get(0)[14] ;
		    			List<Object[]> codeList = commonService.doExecuteSql(codeSql);
		    			if(codeList != null && codeList.size() > 0)
		    			{
		    				//代码成功卖出后，给用户增加金额
			        		String updateMoneySql = "update wxuser set money=money + "+codeList.get(0)[2]+"   where openid='"+codeList.get(0)[0]+"'";
			        		logger.info("updateMoneySql : " + updateMoneySql);
			        		commonService.doInsertUpdateSql(updateMoneySql);
		    				
			        		/**
		    				String wxUserSql = "select nickname, headimg from wxuser where openid='"+openid+"'";
			    			List<Object[]> wxUserList = commonService.doExecuteSql(wxUserSql);
			    			String nickName = "";
			    			if(wxUserList != null && wxUserList.size() > 0)
			    			{
			    				nickName = wxUserList.get(0)[0].toString();
			    			}
			    			**/
			    			String currentTime = DateUtils.getDateTime();
			    			String title = "恭喜您，代码又卖出一份!"; 
			    			String content = "用户:"+orderList.get(0)[3]+"购买了您的代码:" + codeList.get(0)[1]+ "(" + currentTime + ")";
			    			String insertMsgSql = "insert into market_msg (title, content,toopenid, fromopenid,createtime, isread, parentid, taskid) values( '"+title+"','"+content+"','"+codeList.get(0)[0]+"','"+orderList.get(0)[1]+"', '"+currentTime+"', '0','0','0')";
			    			logger.info("insertMsgSql : " + insertMsgSql);
			    			commonService.doInsertUpdateSql(insertMsgSql);
			    			
			    			//添加消费记录
			    			String resourcename = codeList.get(0)[1] != null ? codeList.get(0)[1].toString() : "";
			    			String sellTitle = "出售代码:" + resourcename;
			    			String money = codeList.get(0)[2] != null ? codeList.get(0)[2].toString() : "";
			    			String resourceid = codeList.get(0)[3] != null ? codeList.get(0)[3].toString() : "";
			    			String insertSellsql = "insert into market_sell (title, sellid, buyerid, createtime, money, resourceid, resourcename) "
			    					+ "values ('"+sellTitle+"', '"+codeList.get(0)[0]+"', '"+orderList.get(0)[1]+"', '"+currentTime+"',"+money+",'"+resourceid+"','"+resourcename+"')";
			    			commonService.doInsertUpdateSql(insertSellsql);
			    			logger.info("insertSellsql : " + insertSellsql);
		    			}
		    			
	        		} catch(Exception e)
	        		{
	        			logger.error("buy code send message has error: ", e);
	        		}
	        		
	        	}
	        	
	        	logger.info("支付成功!!!");
	        	//验证通过，通知微信支付成功
				Element rtnEle = DocumentHelper.createElement("return_code");
				rtnEle.addText("SUCCESS");
				rootElement.add(rtnEle);
				
				Element rtnMsgEle = DocumentHelper.createElement("return_msg");
				rtnMsgEle.addText("OK");
				rootElement.add(rtnMsgEle);
				
				response.getWriter().write(doc.asXML());
	        	
	        } else 
	        {
	        	logger.error("微信回调报告失败，具体原因如下:");
	        	 Element returnMsgEle = rtnRoot.element("return_msg");
	        	 if(returnMsgEle != null)
	        	 {
	        		 String msg = returnMsgEle.getText();
	        		 logger.error("weixin return_msg : " + msg);
	        	 }
	        }
			
			
			
		} catch (Exception e) {
			logger.error("payCallBack has error:", e);
		} finally
		{
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e) {
					logger.error("close inputStream error:", e);
				}
			}
		}
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
	
	//获取随机的5位字符串
	public static String get10RandomStr()
	{
		String originStr = "1234567890";
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < 5; i++)
		{
			int randomNumber = new Random().nextInt(10);
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
		//System.out.println(encodeUrl("http://www.2dian.com/spriderproject/wx/webchat?actID=3"));
		//System.out.println(get10RandomStr());
		String resultXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wxf219e6b969b14df6]]></appid><mch_id><![CDATA[1464913102]]></mch_id><nonce_str><![CDATA[8rj4sTzCvfEd5fSr]]></nonce_str><sign><![CDATA[7B1321B4F6C59E4E1C702A3E5B40F9A1]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx201708081557377672d5e9570037525534]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
		
		try {
			Document doc = DocumentHelper.parseText(resultXml);
			Element root = doc.getRootElement(); 
	        Element message = root.element("return_code");
	        System.out.println(message.getText());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	

}
