package com.dian.controller.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dian.core.Constant;
import com.dian.core.DianBaseController;
import com.dian.model.sys.SysUser;
import com.dian.service.sys.SysUserService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.Item;
import core.support.QueryResult;
import core.util.MD5;
import core.web.SystemCache;


@Controller
@RequestMapping("/sys/sysuser")
public class SysUserController extends DianBaseController<SysUser> implements Constant {

	@Resource
	private SysUserService sysUserService;

	@RequestMapping("/login")
	public void login(SysUser sysUserModel, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		SysUser sysUser = sysUserService.getByProerties("userName", sysUserModel.getUserName());
		if (sysUser == null || sysUser.getRole() == 0) { // 用户名有误或已被禁用
			result.put("result", -1);
			writeJSON(response, result);
			return;
		}
		if (!sysUser.getPassword().equals(MD5.crypt(sysUserModel.getPassword()))) { // 密码错误
			result.put("result", -2);
			writeJSON(response, result);
			return;
		}
		sysUser.setLastLoginTime(new Date());
		sysUserService.update(sysUser);
		request.getSession().setAttribute(SESSION_SYS_USER, sysUser);
		result.put("result", 1);
		writeJSON(response, result);
	}

	@RequestMapping("/home")
	public String home(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getSession().getAttribute(SESSION_SYS_USER) == null) {
			return "";
		} else {
			return "back/main";
		}
	}

	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute(SESSION_SYS_USER);
		response.sendRedirect("/forestry/login.jsp");
	}

	@RequestMapping("/resetPassword")
	public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Map<String, Object> result = new HashMap<String, Object>();
		SysUser sysUser = sysUserService.getByProerties("userName", userName);
		if (!sysUser.getPassword().equals(MD5.crypt(oldPassword))) {
			result.put("result", -2);
			writeJSON(response, result);
			return;
		}
		result.put("result", 1);
		sysUser.setPassword(MD5.crypt(newPassword));
		sysUserService.update(sysUser);
		writeJSON(response, result);
	}

	@RequestMapping("/externalVerifySysUser")
	public void externalVerifySysUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		SysUser sysUser = sysUserService.getByProerties(new String[] { "userName", "password" }, new Object[] { username, MD5.crypt(password) });
		if (null == sysUser) {
			writeJSON(response, "{success:false}");
		} else {
			writeJSON(response, "{success:true}");
		}
	}

	@RequestMapping("/getRoleName")
	public void getRoleName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONArray jsonArray = new JSONArray();
		for (Map.Entry<String, Item> roleMap : SystemCache.DICTIONARY.get("SYSUSER_ROLE").getItems().entrySet()) {
			Item item = roleMap.getValue();
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("ItemText", item.getValue());
			jsonObject.element("ItemValue", item.getKey());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("list", jsonArray);
		writeJSON(response, resultJSONObject);
	}

	@Override
	@RequestMapping(value = "/saveSysUser", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(SysUser entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		SysUser checkuserName = sysUserService.getByProerties("userName", entity.getUserName());
		if (null != checkuserName && null == entity.getId()) {
			parameter.setSuccess(false);
		} else {
			if (CMD_EDIT.equals(parameter.getCmd())) {
				entity.setLastLoginTime(checkuserName.getLastLoginTime());
				sysUserService.update(entity);
			} else if (CMD_NEW.equals(parameter.getCmd())) {
				entity.setPassword(MD5.crypt(entity.getPassword()));
				sysUserService.persist(entity);
			}
			parameter.setCmd(CMD_EDIT);
			parameter.setSuccess(true);
		}
		writeJSON(response, parameter);
	}

	@RequestMapping(value = "/getSysUser", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSysUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		SysUser sysUser = new SysUser();
		String userName = request.getParameter("userName");
		if (StringUtils.isNotBlank(userName)) {
			sysUser.set$like_userName(userName);
		}
		String realName = request.getParameter("realName");
		if (StringUtils.isNotBlank(realName)) {
			sysUser.set$like_realName(realName);
		}
		String role = request.getParameter("role");
		if (StringUtils.isNotBlank(role)) {
			sysUser.set$eq_role(Short.valueOf(role));
		}
		sysUser.setFirstResult(firstResult);
		sysUser.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		sysUser.setSortedConditions(sortedCondition);
		QueryResult<SysUser> queryResult = sysUserService.doPaginationQuery(sysUser);
		List<SysUser> forestryList = sysUserService.getSysUserList(queryResult.getResultList());
		ListView<SysUser> forestryListView = new ListView<SysUser>();
		forestryListView.setData(forestryList);
		forestryListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, forestryListView);
	}

	@RequestMapping("/deleteSysUser")
	public void deleteSysUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		if (Arrays.asList(ids).contains(Long.valueOf("1"))) {
			writeJSON(response, "{success:false,msg:'删除项包含超级管理员，超级管理员不能删除！'}");
		} else {
			boolean flag = sysUserService.deleteByPK(ids);
			if (flag) {
				writeJSON(response, "{success:true}");
			} else {
				writeJSON(response, "{success:false}");
			}
		}
	}

	@RequestMapping(value = "/getRoleNameList")
	public void getRoleNameList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List roleNameList = new ArrayList();
		for (Map.Entry<String, Item> roleMap : SystemCache.DICTIONARY.get("SYSUSER_ROLE").getItems().entrySet()) {
			Item item = roleMap.getValue();
			SysUser sysUser = new SysUser();
			sysUser.setRole(Short.valueOf(item.getKey()));
			sysUser.setRoleName(item.getValue());
			roleNameList.add(sysUser);
		}
		ListView roleNameListView = new ListView();
		roleNameListView.setData(roleNameList);
		roleNameListView.setTotalRecord(Long.valueOf(roleNameList.size()));
		writeJSON(response, roleNameListView);
	}

}
