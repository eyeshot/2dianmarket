package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.Type;
import com.dian.service.sys.TypeService;

import core.service.BaseService;

/**
 */
@Service
public class TypeServiceImpl extends BaseService<Type> implements TypeService {

	private TypeDao typeDao;

	@Resource
	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
		this.dao = typeDao;
	}

}
