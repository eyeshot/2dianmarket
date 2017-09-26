package core.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtil {
	
	private static Logger logger = Logger.getLogger(XmlUtil.class);

	public static void main(String[] args) {
		
		String xml = "<xml><appid><![CDATA[wxf219e6b969b14df6]]></appid>" + 
					"<bank_type><![CDATA[CFT]]></bank_type>" +
					"<cash_fee><![CDATA[1]]></cash_fee>" +
					"<fee_type><![CDATA[CNY]]></fee_type>" +
					"<is_subscribe><![CDATA[N]]></is_subscribe>" +
					"<mch_id><![CDATA[1464913102]]></mch_id>"+
					"<nonce_str><![CDATA[wGnRjoXKN9X]]></nonce_str>"+
					"<openid><![CDATA[oKw_x0McoirE6zeF7XsYwoEOfw-c]]></openid>"+
					"<out_trade_no><![CDATA[201708091714489799]]></out_trade_no>"+
					"<result_code><![CDATA[SUCCESS]]></result_code>"+
					"<return_code><![CDATA[SUCCESS]]></return_code>"+
					"<sign><![CDATA[659840BBDA82F6551A69B0D44DDFA12D]]></sign>"+
					"<time_end><![CDATA[20170809171514]]></time_end>"+
					"<total_fee>1</total_fee>"+
					"<trade_type><![CDATA[JSAPI]]></trade_type>"+
					"<transaction_id><![CDATA[4009932001201708095336172747]]></transaction_id>"+
					"</xml>";
		getWeixinMd5(xml);

	}
	
	//从xml中解析出各个元素，并按照微信的规则md5加密
	public static String getWeixinMd5(String xml)
	{
		Document rtnDoc;
		String result = "";
		try {
			Map<String, Object> paramMap = new TreeMap<String, Object>();
			rtnDoc = DocumentHelper.parseText(xml.trim());
			Element rtnRoot = rtnDoc.getRootElement(); 
			List<Element> list = rtnRoot.elements();
			for(Element ele : list)
			{
				if(ele.getName().equals("sign"))
					continue;
				paramMap.put(ele.getName(), ele.getText());
				
			}
			
			StringBuffer signStr = new StringBuffer();
			for(Entry<String, Object> entry : paramMap.entrySet())
			{
				signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			signStr.append("key=").append(Constant.XCX_SHANG_KEY);
			logger.info("getWeixinMd5 params :" + signStr);
			
			result = Encrypt.md5(signStr.toString()).toUpperCase();
			logger.info("getWeixinMd5 sign : " + result);
			
		} catch (Exception e) {
			logger.error("getWeixinMd5 has error:", e);
		}
		
		return result;
		
	}

}
