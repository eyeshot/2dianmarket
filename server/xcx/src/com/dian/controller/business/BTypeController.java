package com.dian.controller.business;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dian.core.Constant;
import com.dian.core.DianBaseController;
import com.dian.model.sys.Servicegroup;
import com.dian.model.sys.Type;
import com.dian.service.sys.ServiceGroupService;
import com.dian.service.sys.TypeService;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/type/api/")
public class BTypeController extends DianBaseController<Type> {

	@Resource
	private TypeService typeService;
	
	@Resource
	private ServiceGroupService serviceGroupSerivce;

	
	private static Logger logger = Logger.getLogger(BTypeController.class);
	
	@RequestMapping(value = "/getGroupType", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getGroupType(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		try
		{
			List<Servicegroup> list = serviceGroupSerivce.doQueryAll();
			
			Collections.sort(list, new Comparator<Servicegroup>() {

	  	      @Override

	  	      public int compare(Servicegroup o1, Servicegroup o2) 
	  	      {
	  	        return new Integer(o1.getSortnum()).compareTo(new Integer(o2.getSortnum()));

	  	      }

	  	    });
			
			JSONObject obj = new JSONObject();
			obj.put("total", list.size());
			int index = 0;
			JSONArray typeDetail = new JSONArray();
			for(Servicegroup group : list)
			{
				index ++;
				JSONObject detail = new JSONObject();
				detail.put("name", group.getName());
				detail.put("order", group.getSortnum());
				logger.info("group order :" + group.getSortnum());
				JSONArray arr = new JSONArray();
				logger.info("group.getId() : "  + group.getId());
				List<Type> typeList = typeService.queryByProerties("groupid", String.valueOf(group.getId()));
				for(Type type : typeList)
				{
					JSONObject oneTypeObj = new JSONObject();
					oneTypeObj.put("id", type.getId());
					oneTypeObj.put("name", type.getName());
					oneTypeObj.put("img", Constant.HOST + type.getImageurl());
					arr.add(oneTypeObj);
				}
				detail.put("type", arr);
				typeDetail.add(detail);
			}
			
			obj.put("detail", typeDetail);
			
			logger.info("group list :" + obj);
			
			ret.put("data", obj);
		} catch(Exception e)
		{
			logger.error("getGroupType has error: ", e);
		}
		

		return ret;
	}
	
	
	

}
