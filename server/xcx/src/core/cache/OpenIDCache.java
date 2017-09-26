package core.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * @author eyeshot
 */
public class OpenIDCache{

	private static OpenIDCache cacheManager = null;
	
	private static Logger logger = Logger.getLogger(OpenIDCache.class);
	
	private static Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();
	
	private OpenIDCache() 
	{
		
	}

	public static OpenIDCache getOpenIDCacheManager() {
		if(cacheManager == null)
			cacheManager = new OpenIDCache();
		return cacheManager;
	}
	
	public static Map<String, String> getCacheMap()
	{
		return cacheMap;
	}

	
	
	public static void setProperties(){
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			
			File fileNum = new File("openIDCache.properties");
			System.out.println("filePath:" + fileNum.getAbsolutePath());
			if (!fileNum.exists()) {		
				System.out.println("file not exist");
				fileNum.createNewFile();
			} else 
			{
				System.out.println("file exist");
			}
			Properties prop = new Properties();
	        fis = new FileInputStream("openIDCache.properties");    
	        prop.load(fis);
	        System.out.println("cacheMap.entrySet().size: " + cacheMap.entrySet().size());
	        
	        for (Entry<String, String> entry : cacheMap.entrySet()) {  	        	
	        	prop.setProperty(entry.getKey(),String.valueOf(entry.getValue()));
	            logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
	        }  
	        fos = new FileOutputStream("openIDCache.properties");       
	        prop.store(fos, "Copyright (c) Boxcode Studio");  
	        
	        
		} catch (IOException e) {
			System.out.println("setProperties  has error : e" + e);
			logger.error("setProperties  has error : ", e);
		}finally {
			
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if (fis != null) {
					fis = null;
				}
				if (fos != null) {
					fos = null;
				}
			}
			
			
		}
	
	}
	
	public static void getProperties(){
		
		//System.out.println("Path:" + OpenIDCache.class.getClass().getClassLoader().getResource("/").getPath());
		
		File fileNum = new File("openIDCache.properties");
		System.out.println("filePath:" + fileNum.getAbsolutePath());
		if (!fileNum.exists()) {			
			return ;
		}
		FileInputStream fis = null;
		try {
			
			Properties prop = new Properties();
	        fis = new FileInputStream("openIDCache.properties");    
	        prop.load(fis);
	        
	        Enumeration enu2=prop.propertyNames();
	        while(enu2.hasMoreElements()){
	            String key = (String)enu2.nextElement();
	            System.out.println("key:" + key + ", value:" + prop.getProperty(key));
	            logger.info("key:" + key + ", value:" + prop.getProperty(key));
	            // System.out.println(key);
	            cacheMap.put(key, (prop.getProperty(key)));
	        } 
	        
			
		} catch (Exception e) {
			logger.error("getProperties  has error : ", e);
		}finally {
			
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				
				if (fis != null) {				
					fis = null;	
				}
				
			}
			
		}
		
		
		
	}
	
	
	
	
}
