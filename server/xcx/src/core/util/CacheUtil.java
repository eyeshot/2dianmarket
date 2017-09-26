package core.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {
	
	private static Map<String, String> thirdMap = new HashMap<String, String>();
	
	public static Map<String, String> getCacheMap()
	{
		return thirdMap;
	}
	
	public static String getUserId(String thirdSession)
	{
		String userInfo = CacheUtil.getCacheMap().get(thirdSession);
		String userid = "";
		if(userInfo != null && userInfo.length() > 0)
		{
			userid = userInfo.split("#")[0];
		}
		return userid;
	}

	public static void main(String[] args)
	{

	}

}
