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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dian.core.Constant;
import com.dian.core.DianBaseController;
import com.dian.model.sys.DianType;
import com.dian.model.sys.Servicegroup;
import com.dian.model.sys.Type;
import com.dian.service.sys.DianTypeService;
import com.dian.service.sys.ServiceGroupService;
import com.dian.service.sys.TypeService;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/wz/api/")
public class BActicleController extends DianBaseController<DianType> {

	@Resource
	private TypeService typeService;
	
	@Resource
	private ServiceGroupService serviceGroupSerivce;
	
	@Resource
	private DianTypeService dianTypeService;

	
	private static Logger logger = Logger.getLogger(BActicleController.class);
	
	@RequestMapping(value = "/getPageList", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getPageList(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String pageSize = json.get("pageSize");
		String pageIndex = json.get("pageIndex");
		String typeID = json.get("typeID");
		try
		{
			int start = ( new Integer(pageIndex) - 1) * new Integer(pageSize);
			String sql = "SELECT * FROM forestry_type where type='" + typeID + "' limit "+start+","+pageSize+"";
			List<Object[]> list = dianTypeService.doExecuteSql(sql);
			
			JSONArray jsonArr = new JSONArray();
			for(Object[] oneObj : list)
			{
				logger.info("oneObj:" + oneObj);
				JSONObject detail = new JSONObject();
				detail.put("name", oneObj[1]);
				detail.put("id", oneObj[0]);
				jsonArr.add(detail);
				
			}
			
			ret.put("data", jsonArr);
		} catch(Exception e)
		{
			logger.error("getPageList has error: ", e);
		}
		

		return ret;
	}
	
	@RequestMapping(value = "/getDetail", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Map<String, Object> getDetail(HttpServletRequest request, @RequestBody Map<String, String> json) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		String id = json.get("id");
		try
		{
			DianType oneData = dianTypeService.get(new Long(id));
			JSONObject detail = new JSONObject();
			detail.put("name", oneData.getName());
			detail.put("detail", oneData.getDescription());
			
			ret.put("data", detail);
		} catch(Exception e)
		{
			logger.error("getDetail has error: ", e);
		}
		
		return ret;
	}
	
	
	

}
