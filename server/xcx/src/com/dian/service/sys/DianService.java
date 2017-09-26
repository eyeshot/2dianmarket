package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.DianVue;

import core.service.Service;


public interface DianService extends Service<DianVue> {

	List<DianVue> getForestryList(List<DianVue> resultList);
	
	List<Object[]> queryExportedForestry(Long[] ids);

}
