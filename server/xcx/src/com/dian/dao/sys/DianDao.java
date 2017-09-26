package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.DianVue;

import core.dao.Dao;


public interface DianDao extends Dao<DianVue> {
	
	List<Object[]> queryExportedForestry(Long[] ids);

}
