package com.dian.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.DianTypeDao;
import com.dian.model.sys.DianType;
import com.dian.model.sys.Type;
import com.dian.service.sys.DianTypeService;
import com.dian.service.sys.TypeService;

import core.service.BaseService;
import core.util.HtmlUtils;

/**
 */
@Service
public class ForestryTypeServiceImpl extends BaseService<DianType> implements DianTypeService {

	private DianTypeDao forestryTypeDao;

	@Resource
	public void setForestryTypeDao(DianTypeDao forestryTypeDao) {
		this.forestryTypeDao = forestryTypeDao;
		this.dao = forestryTypeDao;
	}

	@Override
	public List<DianType> getForestryTypeList(List<DianType> resultList, TypeService typeService) {
		List<DianType> forestryTypeList = new ArrayList<DianType>();
		for (DianType entity : resultList) {
			DianType forestryType = new DianType();
			forestryType.setId(entity.getId());
			forestryType.setName(entity.getName());
			forestryType.setDescription(entity.getDescription());
			forestryType.setDescriptionNoHtml(HtmlUtils.htmltoText(entity.getDescription()));
			if(entity.getType() != null && entity.getType().length() > 0 )
			{
				Type getType = typeService.get(new Long(entity.getType()));
				forestryType.setTypename(getType != null ? getType.getName():"");
			}
			
			forestryTypeList.add(forestryType);
		}
		return forestryTypeList;
	}

}
