package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.DianType;

import core.service.Service;


public interface DianTypeService extends Service<DianType> {

	List<DianType> getForestryTypeList(List<DianType> resultList, TypeService typeService);

}
