package core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 加密工具类
 * 
 * md5加密出来的长度是32位
 * 
 * sha加密出来的长度是40位
 * 
 * @author Varro
 * 
 */
public class Encrypt {

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// md5加密测试
		String md5_1 = md5("1111");
		String md5_2 = md5("马洋");
		//System.out.println(md5_1 + "\n" + md5_2);
		
		// 场景加去请求豆子的MD5值
		String a="40343"; //公司号
		String b="50202"; //apiID
		String c="杨书民"; //姓名
		String d="130434198205162435"; //身份证号
		String orderId = "001"; //自定义ID
		String secret = "k9sj3ExwKbG2HO5aMTAP"; //秘钥
		
		LinkedHashMap<String, String> data = new LinkedHashMap<String ,String>();
		data.put("a", "40343");
		data.put("b", "50202");
		data.put("c", "杨书民");
		data.put("d", "130434198205162435");
		//System.out.println(getMd5String(data));
		/**
		//场景加的客户来场景加请求的MD5值
		String a="658995"; //公司号
		String b="60005"; //apiID
		String c="%E5%BC%A0%E4%B8%89"; //姓名
		String d="xxx1923091922"; //身份证号
		String orderId = "001"; //自定义ID
		String secret = "hyPHyFEbO4VsbQclSGz6"; //秘钥
		**/
		
		String temp = a+"|"+b+"|"+c+"|"+d+"|"+orderId+"|"+secret;
		//sb.append(cmpID).append("|").append(apiID).append("|").append(name).append("|").append(identityNo).append("|").append(orderID).append("|").append(secret);
		//40343  50202 %E6%9D%A8%E4%B9%A6%E6%B0%91 130434198205162435 001 k9sj3ExwKbG2HO5aMTAP
		//String md5Sign = Encrypt.md5(temp);
		String encodeName = getEncode("丁健锋");
		System.out.println(encodeName);
		System.out.println(md5("312536|60006|"+encodeName+"||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		
		System.out.println(md5("312536|60006|﻿"+encodeName+"||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		
		System.out.println(md5("312536|60006|丁健锋||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		System.out.println(md5("312536|60006|﻿丁健锋||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		System.out.println(md5("312536|60006|%E4%B8%81%E5%81%A5%E9%94%8B||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		System.out.println(getXinlianCardMD51("312536", "60006", "%E4%B8%81%E5%81%A5%E9%94%8B", "", "310228198003193618", "shujutang", "iHx1MggbSobtuknMvx0h"));
		
		//System.out.println(Encrypt.md5("312536|60006|丁健锋||310228198003193618|shujutang|iHx1MggbSobtuknMvx0h"));
		//System.out.println(md5Sign);
		
		
		LinkedHashMap<String, String> data3 = new LinkedHashMap<String ,String>();
		data3.put("companyNo", "335518");
		data3.put("apiID", "60012");
		data3.put("name", "%E6%B1%A4%E5%90%89%E9%A1%BA");
		data3.put("idNumber", "220106197610039612");
		data3.put("orderId", "a4f2566b3170517170046186a7");
		data3.put("secret", "P5ks26zQUqDkOMbxHgEA");
		String md5Sign = Encrypt.getMd5String(data3);
		System.out.println("md5Sign : " + md5Sign);
		
		
		LinkedHashMap<String, String> data5 = new LinkedHashMap<String ,String>();
		data5.put("companyNo", "335518");
		data5.put("apiID", "60012");
		data5.put("name", "%E7%8E%8B%E5%86%B0");
		data5.put("idNumber", "231282199010077336");
		data5.put("orderId", "a4f2566b3170517163043186a1");
		data5.put("secret", "P5ks26zQUqDkOMbxHgEA");
		String md5Sign2 = Encrypt.getMd5String(data5);
		System.out.println("md5Sign2 : " + md5Sign2);
		System.out.println("md5Sign2 length : "  + md5Sign2.length());
	}
	
	public static String getEncode(String str)
	{
		String rtn = "";
		try {
			 rtn = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return rtn;
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}


	/**
	 * md5
	 * 
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	public static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}
	/**
	 * 返回上海豆子的用户身份信息查询的md5值
	 * @return
	 */
	public static String getDouziIdentityMD5(String cmpID, String apiID, String name, String identityNo, String orderID, String secret)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(cmpID).append("|").append(apiID).append("|").append(name).append("|").append(identityNo).append("|").append(orderID).append("|").append(secret);
		return Encrypt.md5(sb.toString());
	}
	
	/**
	 * 返回信联征信的银行卡查询的md5值(三要素：姓名、卡号、身份证号)
	 * cmpID 公司ID号（必填）
	 * apiID apiID号（必填）
	 * name 银行卡户主姓名（选填）
	 * cardNo 银行卡卡号（必填）
	 * orderID 唯一流水号 (选填)
	 * cidNo 身份证号 (选填)
	 * secret 场景加提供的秘钥(必填)
	 * @return
	 */
	public static String getXinlianCardMD51(String cmpID, String apiID, String name, String cardNo,String idNumber, String orderID, String secret)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(cmpID).append("|").append(apiID).append("|").append(name).append("|").append(cardNo).append("|").append(idNumber).append("|").append(orderID).append("|").append(secret);
		return Encrypt.md5(sb.toString());
	}
	
	/**
	 * 返回信联征信的银行卡查询的md5值(四要素：姓名、卡号、身份证号、手机号)
	 * cmpID 公司ID号（必填）
	 * apiID apiID号（必填）
	 * name 银行卡户主姓名（选填）
	 * cardNo 银行卡卡号（必填）
	 * orderID 唯一流水号 (选填)
	 * cidNo 身份证号 (选填)
	 * mobileNo 手机号 (选填)
	 * secret 场景加提供的秘钥(必填)
	 * @return
	 */
	public static String getXinlianCardMD52(String cmpID, String apiID, String name, String cardNo, String idNumber, String mobileNo, String orderID, String secret)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(cmpID).append("|").append(apiID).append("|").append(name).append("|").append(cardNo).append("|").append(idNumber).append("|").append(mobileNo).append("|").append(orderID).append("|").append(secret);
		return Encrypt.md5(sb.toString());
	}
	
	
	/**
	 * 返回91征信
	 */
	public static String getCreditMD5(String cmpID, String apiID, String name,String idNumber,String callbackURL, String orderID, String secret)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(cmpID).append("|").append(apiID).append("|").append(name).append("|").append(idNumber).append("|").append(callbackURL).append("|").append(orderID).append("|").append(secret);
		return Encrypt.md5(sb.toString());
	}
	
	/**
	 * 通用根据参数来生成MD5串
	 */
	
	public static String getMd5String(LinkedHashMap<String, String> data)
	{
		if(data == null || data.size() <= 0)
			return "";
		
		StringBuffer sb = new StringBuffer("");
		Iterator<Map.Entry<String, String>> ite = data.entrySet().iterator();
		while(ite.hasNext())
		{
			Map.Entry<String, String> entry = ite.next();
			sb.append(entry.getValue()).append("|");
		}
		
		return md5(sb.toString().substring(0, sb.toString().length() - 1));
	}

}
