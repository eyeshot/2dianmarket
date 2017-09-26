package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.DepartmentDao;
import com.dian.dao.sys.ServiceGroupDao;
import com.dian.model.sys.Servicegroup;
import com.dian.service.sys.ServiceGroupService;

import core.service.BaseService;

/**
 * @author eyeshot
 */
@Service
public class ServiceGroupServiceImpl extends BaseService<Servicegroup> implements ServiceGroupService {

	private ServiceGroupDao serivceGroupDao;

	@Resource
	public void setDepartmentDao(ServiceGroupDao serviceDao) {
		this.serivceGroupDao = serviceDao;
		this.dao = serviceDao;
	}

}
