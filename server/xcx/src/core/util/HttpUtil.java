package core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import core.wx.WxRuntimeException;




public class HttpUtil {


	
	
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	
	public static String request(String url, String paramJson)
	{
		// 声明请求httpclient对象
		CloseableHttpClient client = null;
		
		// 声明http结果对象
		CloseableHttpResponse response = null;
		
		// 声明返回结果
		String result = "";
		try {

			// 创建请求httpclient对象
			client = buildHttpClient(false);
			
			// 创建一个post请求
			HttpPost post = new HttpPost(url);
			
			// 设置头部-编码
			post.setHeader(HTTP.CONTENT_ENCODING, "UTF-8");
			
			// 设置头部-数据类型
			post.setHeader(HTTP.CONTENT_TYPE,"application/json");
			
			// 设置超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000)
					.setConnectTimeout(5000).build();
			post.setConfig(requestConfig);
			
			// 非空判断,并封装数据
			if(null != paramJson && paramJson.length() > 0){
				StringEntity se = new StringEntity(paramJson,"UTF-8");
				se.setContentEncoding("UTF-8");   
				se.setContentType("application/json"); 
				post.setEntity(se);
			}
			
			// 发送请求
			response  = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			
			// 执行请求成功
			if (code == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// 输入流关闭，同时会自动触发http连接的release
					result = EntityUtils.toString(entity, "UTF-8");
				}
			}
			
			System.out.println("request:"+paramJson);
			System.out.println("result:" + result);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			// 最后关闭资源
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		return result;
	}
	
	
	/**
	 * 创建HttpClient 
	 * @param isHttps 是否https地址
	 * @return
	 */
	public static CloseableHttpClient buildHttpClient(boolean isHttps) {
		CloseableHttpClient client = null;
		try {
			if (isHttps) {
				SSLContext context = SSLContext.getInstance("TLS");
				X509TrustManager tm = new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
					public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
					public X509Certificate[] getAcceptedIssuers() {return null;}
				}; 
				context.init(null, new TrustManager[] { tm }, null);
				HostnameVerifier verifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(context);
				client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			} else {
				client = HttpClientBuilder.create().build();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	
	 public static String requestCommonByHttpGet(String url, String paramJson) throws java.net.SocketTimeoutException
	  	{
	  		// 声明请求httpclient对象
	  		CloseableHttpClient client = null;
	  		
	  		// 声明http结果对象
	  		CloseableHttpResponse response = null;
	  		
	  		// 声明返回结果
	  		String result = "";
	  		try {
	  			
	  			// 创建请求httpclient对象
	  			client = buildHttpClient(false);
	  			
	  			// 创建一个get请求
	  			HttpGet get = new HttpGet(url);
	  			
	  			// 设置头部-编码
	  			get.setHeader(HTTP.CONTENT_ENCODING, "UTF-8");
	  			
	  			// 设置头部-数据类型
	  			//post.setHeader(HTTP.CONTENT_TYPE,"application/json");
	  			
	  			// 设置超时时间
	  			RequestConfig requestConfig = RequestConfig.custom()
	  					.setSocketTimeout(10000)
	  					.setConnectTimeout(10000).build();
	  			get.setConfig(requestConfig);
	  			
	  			// 非空判断,并封装数据
	  			if(null != paramJson && paramJson.length() > 0){
	  				StringEntity se = new StringEntity(paramJson,"UTF-8");
	  				se.setContentEncoding("UTF-8");   
	  				se.setContentType("application/json"); 
	  				//post.setEntity(se);
	  			}
	  			
	  			// 发送请求
	  			response  = client.execute(get);
	  			int code = response.getStatusLine().getStatusCode();
	  			HttpEntity entity = response.getEntity();
	  			// 执行请求成功
	  			if (code == HttpStatus.SC_OK) {
	  				if (entity != null) {
	  					// 输入流关闭，同时会自动触发http连接的release
	  					result = EntityUtils.toString(entity, "UTF-8");
	  				}
	  			} else 
	  			{
	  				get.abort();
	  				if (entity != null) {
	  					// 输入流关闭，同时会自动触发http连接的release
	  					result = EntityUtils.toString(entity, "UTF-8");
	  				}
	  			}
	  			
	  			if(paramJson != null)
	  			{
	  				logger.info("request:"+paramJson);
	  			}
	  			
	  			//System.out.println("result:" + result);
	  			
	  		}catch (IOException e) {
	  			logger.error("request URL is : " + url);
	  			logger.error("requestCommonByHttpGet error:", e);
	  		}finally{
	  			// 最后关闭资源
	  			HttpClientUtils.closeQuietly(response);
	  			HttpClientUtils.closeQuietly(client);
	  		}
	  		return result;
	  	}
	     
	     public static String requestCommonByHttpGet(String url, String paramJson, boolean isHttps)
	   	{
	   		// 声明请求httpclient对象
	   		CloseableHttpClient client = null;
	   		
	   		// 声明http结果对象
	   		CloseableHttpResponse response = null;
	   		
	   		// 声明返回结果
	   		String result = "";
	   		try {
	   			
	   			// 创建请求httpclient对象
	   			client = buildHttpClient(isHttps);
	   			
	   			// 创建一个get请求
	   			HttpGet get = new HttpGet(url);
	   			
	   			// 设置头部-编码
	   			get.setHeader(HTTP.CONTENT_ENCODING, "UTF-8");
	   			
	   			// 设置头部-数据类型
	   			//post.setHeader(HTTP.CONTENT_TYPE,"application/json");
	   			
	   			// 设置超时时间
	   			RequestConfig requestConfig = RequestConfig.custom()
	   					.setSocketTimeout(15000)
	   					.setConnectTimeout(15000).build();
	   			get.setConfig(requestConfig);
	   			
	   			// 非空判断,并封装数据
	   			if(null != paramJson && paramJson.length() > 0){
	   				StringEntity se = new StringEntity(paramJson,"UTF-8");
	   				se.setContentEncoding("UTF-8");   
	   				se.setContentType("application/json"); 
	   				//post.setEntity(se);
	   			}
	   			
	   			// 发送请求
	   			response  = client.execute(get);
	   			int code = response.getStatusLine().getStatusCode();
	   			HttpEntity entity = response.getEntity();
	   			// 执行请求成功
	   			if (code == HttpStatus.SC_OK) {
	   				if (entity != null) {
	   					// 输入流关闭，同时会自动触发http连接的release
	   					result = EntityUtils.toString(entity, "UTF-8");
	   				}
	   			} else 
	   			{
	   				get.abort();
	   				if (entity != null) {
	   					// 输入流关闭，同时会自动触发http连接的release
	   					result = EntityUtils.toString(entity, "UTF-8");
	   				}
	   			}
	   			
	   			if(paramJson != null)
	   			{
	   				logger.info("request:"+paramJson);
	   			}
	   			
	   			//System.out.println("result:" + result);
	   			
	   		}catch (Exception e) {
	   			logger.error("request URL is : " + url);
	   			logger.error("requestCommonByHttpGet error:", e);
	   		}finally{
	   			// 最后关闭资源
	   			HttpClientUtils.closeQuietly(response);
	   			HttpClientUtils.closeQuietly(client);
	   		}
	   		return result;
	   	}
	     
	    public String post(String url, String content) {
	    	 CloseableHttpClient httpClient = buildHttpClient(true);
	    	 RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();
	         HttpPost httpPost = new HttpPost(url);
	         httpPost.setConfig(requestConfig);
	         httpPost.addHeader("Content-Type", "text/xml");

	         if (content != null) {
	             StringEntity entity = new StringEntity(content, "UTF-8");
	             httpPost.setEntity(entity);
	         }
	         CloseableHttpResponse response = null;
	         try {
	        	 response = httpClient.execute(httpPost);
	             StatusLine statusLine = response.getStatusLine();
	             HttpEntity entity = response.getEntity();
	             if (statusLine.getStatusCode() >= 300) {
	                 EntityUtils.consume(entity);
	                 throw new WxRuntimeException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
	             }
	             String responseContent = entity == null ? null : EntityUtils.toString(entity, Consts.UTF_8);
	             return responseContent;
	         } catch (IOException ex) {
	             logger.error("http post failed : " + url, ex);
	             throw new WxRuntimeException(999, ex.getMessage());
	         } finally {
		 			try {
		 				httpPost.abort();
		 				// 最后关闭资源
		 	  			HttpClientUtils.closeQuietly(response);
		 	  			HttpClientUtils.closeQuietly(httpClient);
		 			} catch (Exception e) {
		 				e.printStackTrace();
		 			}
		 		}
	     } 
	     
	     
	     public static String requestCommonPost(String url, Map<String, Object> paramMap) throws Exception 
	 	{
	 		// 创建默认的 HttpClient 实例
	 		HttpClient httpClient = buildHttpClient(false);
	 		String content = "";
	 		
	 		RequestConfig requestConfig = RequestConfig.custom()  
	 		        .setConnectTimeout(6000) //设置连接超时时间，单位毫秒。
	 		        .setConnectionRequestTimeout(6000)  //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性
	 		        .setSocketTimeout(6000) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
	 		        .build(); 
	 		
	 		HttpPost httpPost = new HttpPost(url);
	 		httpPost.setConfig(requestConfig);
	 		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
	 		//formParams.add(new BasicNameValuePair("insId", Constant.XINLIAN_INSID));
	 		
	 	    Iterator iter = paramMap.entrySet().iterator();
	 	    while (iter.hasNext()) 
	 	   {
	 		    Map.Entry entry = (Map.Entry) iter.next();
	 		    Object key = entry.getKey();
	 		    Object val = entry.getValue();
	 		    formParams.add(new BasicNameValuePair(key.toString(), val.toString()));
	 	   }
	 		
	 		HttpResponse httpResponse = null;
	 		try {
	 			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
	 			httpPost.setEntity(urlEncodedFormEntity);
	 			logger.info("execurting request: " + httpPost.getURI());
	 			httpResponse = httpClient.execute(httpPost);
	 			HttpEntity resEntity = httpResponse.getEntity();
	 			int statusCode = httpResponse.getStatusLine().getStatusCode();
	 			if (statusCode == HttpStatus.SC_OK) {
	 				logger.info("服务器正常响应.........");
	 				if (resEntity != null) {
	 					content = EntityUtils.toString(resEntity, "UTF-8");
	 					//logger.info("Response content:" + content);
	 				}
	 				EntityUtils.consume(resEntity);
	 			} else 
	 			{
	 				content = EntityUtils.toString(resEntity, "UTF-8");
	 				logger.error("request by post has error:" + content);
	 			}
	 		} catch (UnsupportedEncodingException e) {
	 			e.printStackTrace();
	 		} catch (ClientProtocolException e) {
	 			e.printStackTrace();
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		} finally {
	 			try {
	 				httpPost.abort();
	 				// 最后关闭资源
	 	  			HttpClientUtils.closeQuietly(httpResponse);
	 	  			HttpClientUtils.closeQuietly(httpClient);
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			}
	 		}
	 		return content;

	 	}     
	     
	   public static String requestCommonPostXml(String url, String xml) throws Exception 
		 	{
		 		// 创建默认的 HttpClient 实例
		 		HttpClient httpClient = buildHttpClient(false);
		 		String content = "";
		 		
		 		RequestConfig requestConfig = RequestConfig.custom()  
		 		        .setConnectTimeout(6000) //设置连接超时时间，单位毫秒。
		 		        .setConnectionRequestTimeout(6000)  //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性
		 		        .setSocketTimeout(6000) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用
		 		        .build(); 
		 		
		 		HttpPost httpPost = new HttpPost(url);
		 		httpPost.setConfig(requestConfig);
		 		
		 		HttpResponse httpResponse = null;
		 		try {
		 			StringEntity myEntity = new StringEntity(xml, "UTF-8");  
		 			httpPost.setEntity(myEntity);
		 			logger.info("execurting request: " + httpPost.getURI());
		 			httpResponse = httpClient.execute(httpPost);
		 			HttpEntity resEntity = httpResponse.getEntity();
		 			int statusCode = httpResponse.getStatusLine().getStatusCode();
		 			if (statusCode == HttpStatus.SC_OK) {
		 				logger.info("服务器正常响应.........");
		 				if (resEntity != null) {
		 					content = EntityUtils.toString(resEntity, "UTF-8");
		 					//logger.info("Response content:" + content);
		 				}
		 				EntityUtils.consume(resEntity);
		 			} else 
		 			{
		 				content = EntityUtils.toString(resEntity, "UTF-8");
		 				logger.error("request by post has error:" + content);
		 			}
		 		} catch (UnsupportedEncodingException e) {
		 			e.printStackTrace();
		 		} catch (ClientProtocolException e) {
		 			e.printStackTrace();
		 		} catch (IOException e) {
		 			e.printStackTrace();
		 		} finally {
		 			try {
		 				httpPost.abort();
		 				// 最后关闭资源
		 	  			HttpClientUtils.closeQuietly(httpResponse);
		 	  			HttpClientUtils.closeQuietly(httpClient);
		 			} catch (Exception e) {
		 				e.printStackTrace();
		 			}
		 		}
		 		return content;

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
	
	
	
}
