package core.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MapTest {

	public static void main(String[] args) 
	{
		testLinkedMapHash();
		testTreeMap();
		testHashMap();

	}
	
	public static void testLinkedMapHash()
	{
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(5, "语文");
		map.put(3, "数学");
		map.put(1, "英语");
		map.put(2, "物理");
		map.put(4, "音乐");
		
		Iterator<Entry<Integer, String>> ite = map.entrySet().iterator();
		
		while(ite.hasNext())
		{
			Entry<Integer, String> entry = ite.next();
			System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
		}
		System.out.println("*********************");
	}
	
	public static void testTreeMap()
	{
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(5, "语文");
		map.put(3, "数学");
		map.put(1, "英语");
		map.put(2, "物理");
		map.put(4, "音乐");
		
		Iterator<Entry<Integer, String>> ite = map.entrySet().iterator();
		
		while(ite.hasNext())
		{
			Entry<Integer, String> entry = ite.next();
			System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
		}
		System.out.println("*********************");
		
	}
	
	public static void testHashMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("5", "语文");
		map.put("3", "数学");
		map.put("1", "英语");
		map.put("2", "物理");
		map.put("4", "音乐");
		
		Iterator<Entry<String, String>> ite = map.entrySet().iterator();
		
		while(ite.hasNext())
		{
			Entry<String, String> entry = ite.next();
			System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
		}
		System.out.println("*********************");
		
	}

}
