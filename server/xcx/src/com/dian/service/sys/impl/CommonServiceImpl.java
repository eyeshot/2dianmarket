package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.Type;
import com.dian.service.sys.CommonService;

import core.service.BaseService;

/**
 */
@Service
public class CommonServiceImpl extends BaseService<Type> implements CommonService {

	private TypeDao typeDao;

	@Resource
	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
		this.dao = typeDao;
	}

}
