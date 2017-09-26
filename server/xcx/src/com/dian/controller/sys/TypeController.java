package com.dian.controller.sys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.dian.core.DianBaseController;
import com.dian.model.sys.Attachment;
import com.dian.model.sys.DianType;
import com.dian.model.sys.Servicegroup;
import com.dian.model.sys.Type;
import com.dian.service.sys.AttachmentService;
import com.dian.service.sys.DianTypeService;
import com.dian.service.sys.ServiceGroupService;
import com.dian.service.sys.TypeService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.DianUtils;

/**
 * @author eyeshot
 */
@Controller
@RequestMapping("/sys/type")
public class TypeController extends DianBaseController<DianType> {

	@Resource
	private DianTypeService forestryTypeService;
	@Resource
	private AttachmentService attachmentService;
	
	@Resource 
	private TypeService typeService;
	
	@Resource 
	private ServiceGroupService groupService;
	
	public Logger logger = Logger.getLogger(TypeController.class);

	@RequestMapping("/getTypeList")
	public void getTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		Type type = new Type();
		type.setFirstResult(firstResult);
		type.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		type.setSortedConditions(sortedCondition);
		QueryResult<Type> queryResult = typeService.doPaginationQuery(type);
		List<Type> forestryTypeList = queryResult.getResultList();
		
        List<Type> invokList = new ArrayList<Type>();
		for(Type info : forestryTypeList)
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("<img src='" + request.getContextPath() + info.getImageurl() + "' width = 350 height = 300 />");
			Type oneInfo = new Type();
			oneInfo.setId(info.getId());
			oneInfo.setName(info.getName());
			oneInfo.setImageurl(sb.toString());
			if(info != null && info.getGroupid() != null && info.getGroupid().length() > 0)
			{
				Servicegroup group = groupService.get(Integer.valueOf(info.getGroupid()));
				oneInfo.setGroupname(group.getName());
			}
			invokList.add(oneInfo);
		}
		
		ListView<Type> typeListView = new ListView<Type>();
		typeListView.setData(invokList);
		typeListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, typeListView);
	}

	@RequestMapping("/deleteType")
	public void deleteType(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = typeService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	@Override
	@RequestMapping(value = "/saveForestrytype", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(DianType entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		if (CMD_EDIT.equals(parameter.getCmd())) {
			forestryTypeService.update(entity);
		} else if (CMD_NEW.equals(parameter.getCmd())) {
			forestryTypeService.persist(entity);
		}
		attachmentService.deleteAttachmentByForestryTypeId(entity.getId());
		String[] content = entity.getDescription().split(" ");
		for (int i = 0; i < content.length; i++) {
			if (content[i].indexOf("/static/img/upload/") != -1) {
				Attachment attachment = new Attachment();
				attachment.setFileName(entity.getName());
				attachment.setFilePath(content[i].substring(content[i].indexOf("2"), content[i].lastIndexOf("\"")));
				attachment.setForestryType(entity);
				attachmentService.persist(attachment);
			}
		}
		parameter.setCmd(CMD_EDIT);
		parameter.setSuccess(true);
		writeJSON(response, parameter);
	}

	@RequestMapping("/gettypeById")
	public void gettypeById(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws Exception {
		Type oneType = typeService.get(id);
		if(oneType != null && oneType.getGroupid() != null && oneType.getGroupid().length() > 0)
		{
			Servicegroup group = groupService.get(Integer.valueOf(oneType.getGroupid()));
			oneType.setGroupname(group.getName());
		}
		writeJSON(response, oneType);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	@RequestMapping(value = "/uploadAttachement", method = RequestMethod.POST)
	public void uploadAttachement(@RequestParam(value = "uploadAttachment", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestContext requestContext = new RequestContext(request);
		JSONObject json = new JSONObject();
		String name = request.getParameter("name");
		String cmd = request.getParameter("cmd");
		String id = request.getParameter("id");
		String orginimg = request.getParameter("orginimg");
		String groupid = request.getParameter("groupID");
		
		if(file.isEmpty() && cmd.equals("edit"))
		{
			logger.info("modify type info");
			Type type = new Type();
			type.setId(new Long(id));
			type.setName(name);
			type.setImageurl(orginimg);
			type.setGroupid(groupid);
			typeService.update(type);
			json.put("msg", requestContext.getMessage("g_uploadSuccess"));
			writeJSON(response, json.toString());
			return;
		}
		
		if (!file.isEmpty()) {
			if (file.getSize() > 2097152) {
				json.put("msg", requestContext.getMessage("g_fileTooLarge"));
			} else {
				try {
					String originalFilename = file.getOriginalFilename();
					String fileName = sdf.format(new Date()) + DianUtils.getRandomString(3) + originalFilename.substring(originalFilename.lastIndexOf("."));
					File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/img/upload/" + DateFormatUtils.format(new Date(), "yyyyMM")));
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					file.transferTo(new File(filePath.getAbsolutePath() + "/" + fileName));
					String rtnName = DateFormatUtils.format(new Date(), "yyyyMM") + "/" + fileName;
					json.put("success", true);
					json.put("data", rtnName);
					json.put("msg", requestContext.getMessage("g_uploadSuccess"));
					
					if(cmd != null && cmd.equals("new"))
					{
						logger.info("create new type info");
						Type type = new Type();
						type.setName(name);
						type.setGroupid(groupid);
						type.setImageurl("/static/img/upload/" + rtnName);
						typeService.persist(type);
					} else 
					{
						logger.info("modify type info");
						Type type = new Type();
						type.setId(new Long(id));
						type.setName(name);
						type.setGroupid(groupid);
						type.setImageurl("/static/img/upload/" + rtnName);
						typeService.update(type);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					json.put("msg", requestContext.getMessage("g_uploadFailure"));
				}
			}
		} else {
			json.put("msg", requestContext.getMessage("g_uploadNotExists"));
		}
		writeJSON(response, json.toString());
	}
	
	
	@RequestMapping("/getTypeListName")
	public void getTypeListName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Type> typeList = typeService.doQueryAll();
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < typeList.size(); i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("ItemText", typeList.get(i).getName());
			jsonObject.element("ItemValue", typeList.get(i).getId());
			jsonArray.add(jsonObject);
		}
		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.element("list", jsonArray);
		writeJSON(response, resultJSONObject);
	}


}
