package com.dian.controller.sys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.dian.core.DianBaseController;
import com.dian.model.sys.Attachment;
import com.dian.model.sys.DianType;
import com.dian.model.sys.Type;
import com.dian.service.sys.AttachmentService;
import com.dian.service.sys.DianTypeService;
import com.dian.service.sys.TypeService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.DianUtils;


@Controller
@RequestMapping("/sys/forestrytype")
public class DianTypeController extends DianBaseController<DianType> {

	@Resource
	private DianTypeService forestryTypeService;
	@Resource
	private AttachmentService attachmentService;
	@Resource
	private TypeService typeService;

	@RequestMapping("/getForestryType")
	public void getForestryType(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		DianType forestryType = new DianType();
		forestryType.setFirstResult(firstResult);
		forestryType.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		forestryType.setSortedConditions(sortedCondition);
		QueryResult<DianType> queryResult = forestryTypeService.doPaginationQuery(forestryType);
		List<DianType> forestryTypeList = forestryTypeService.getForestryTypeList(queryResult.getResultList(), typeService);
		ListView<DianType> forestryTypeListView = new ListView<DianType>();
		forestryTypeListView.setData(forestryTypeList);
		forestryTypeListView.setTotalRecord(queryResult.getTotalCount());
		writeJSON(response, forestryTypeListView);
	}

	@RequestMapping("/deleteForestryType")
	public void deleteForestryType(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = forestryTypeService.deleteByPK(ids);
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

	@RequestMapping("/getForestrytypeById")
	public void getForestrytypeById(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Long id) throws Exception {
		DianType forestryType = forestryTypeService.get(id);
		if(forestryType != null && forestryType.getType() != null && forestryType.getType().length() > 0)
		{
			Type type = typeService.get(new Long(forestryType.getType()));
			forestryType.setTypename(type.getName());
		}
		writeJSON(response, forestryType);
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	@RequestMapping(value = "/uploadAttachement", method = RequestMethod.POST)
	public void uploadAttachement(@RequestParam(value = "uploadAttachment", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestContext requestContext = new RequestContext(request);
		JSONObject json = new JSONObject();
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
					json.put("success", true);
					json.put("data", DateFormatUtils.format(new Date(), "yyyyMM") + "/" + fileName);
					json.put("msg", requestContext.getMessage("g_uploadSuccess"));
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

}
