package com.dian.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dian.core.Constant;
import com.dian.model.sys.Servicegroup;
import com.dian.service.sys.ServiceGroupService;

import core.controller.ExtJSBaseController;
import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/sys/sevgroup")
public class ServiceGroupController extends ExtJSBaseController<Servicegroup> implements Constant  {

	
	@Resource
	private ServiceGroupService serviceGroupSerivce;
	
	@RequestMapping("/getList")
	public void getServiceGroupList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Servicegroup serviceGroup = new Servicegroup();
		serviceGroup.setFirstResult(firstResult);
		serviceGroup.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		serviceGroup.setSortedConditions(sortedCondition);
		QueryResult<Servicegroup> queryResult = serviceGroupSerivce.doPaginationQuery(serviceGroup);
		List<Servicegroup> groupList = queryResult.getResultList();
		ListView<Servicegroup> groupListView = new ListView<Servicegroup>();
		groupListView.setData(groupList);
		groupListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, groupListView);
	}
	
	@RequestMapping("/getServiceGroupName")
	public void getEnterpriseName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Servicegroup> groupList = serviceGroupSerivce.doQueryAll();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < groupList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("ItemText", groupList.get(i).getName());
			jsonObject.element("ItemValue", groupList.get(i).getId());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("list", jsonArray);
		writeJSON(response, resultJSONObject);
	}

	@RequestMapping("/deleteSevGruop")
	public void deleteOneData(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Integer[] ids) throws IOException {
		boolean flag = serviceGroupSerivce.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}
	
	@RequestMapping("/getSevGroupById")
	public void getOneDataById(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Integer id) throws Exception {
		Servicegroup group = serviceGroupSerivce.get(id);
		writeJSON(response, group);
	}

	@Override
	@RequestMapping(value = "/saveSevGroup", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(Servicegroup entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		if (CMD_EDIT.equals(parameter.getCmd())) {
			serviceGroupSerivce.update(entity);
		} else if (CMD_NEW.equals(parameter.getCmd())) {
			serviceGroupSerivce.persist(entity);
		}
		
		parameter.setCmd(CMD_EDIT);
		parameter.setSuccess(true);
		writeJSON(response, parameter);
	}
	
}
