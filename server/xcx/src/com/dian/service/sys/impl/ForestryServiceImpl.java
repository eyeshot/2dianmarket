package com.dian.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dian.dao.sys.AttachmentDao;
import com.dian.dao.sys.DianDao;
import com.dian.dao.sys.DianTypeDao;
import com.dian.model.sys.Attachment;
import com.dian.model.sys.DianVue;
import com.dian.service.sys.DianService;

import core.service.BaseService;


@Service
public class ForestryServiceImpl extends BaseService<DianVue> implements DianService {

	private DianDao forestryDao;
	@Resource
	private AttachmentDao attachmentDao;

	@Resource
	private DianTypeDao forestryTypeDao;

	@Resource
	public void setForestryDao(DianDao forestryDao) {
		this.forestryDao = forestryDao;
		this.dao = forestryDao;
	}

	@Override
	public List<DianVue> getForestryList(List<DianVue> resultList) {
		List<DianVue> forestryList = new ArrayList<DianVue>();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String imagePath = request.getContextPath();
		for (DianVue entity : resultList) {
			DianVue forestry = new DianVue();
			forestry.setId(entity.getId());
			forestry.setEpcId(entity.getEpcId());
			forestry.setName(entity.getName());
			forestry.setPlantTime(entity.getPlantTime());
			forestry.setEntryTime(entity.getEntryTime());
			forestry.setForestryTypeName(forestryTypeDao.get(entity.getForestryType().getId()).getName());
			forestry.setForestryTypeId(entity.getForestryType().getId());

			List<Attachment> attachmentList = attachmentDao.queryByProerties("forestrytypeId", entity.getTypeId());
			StringBuilder sb = new StringBuilder("");
			for (int i = 0; i < attachmentList.size(); i++) {
				sb.append("<img src='" + imagePath + "/static/img/upload/" + attachmentList.get(i).getFilePath() + "' width = 300 height = 200 />");
			}

			forestry.setForestryTypeImagePath(sb.toString());
			forestryList.add(forestry);
		}
		return forestryList;
	}
	
	@Override
	public List<Object[]> queryExportedForestry(Long[] ids) {
		return forestryDao.queryExportedForestry(ids);
	}

}
