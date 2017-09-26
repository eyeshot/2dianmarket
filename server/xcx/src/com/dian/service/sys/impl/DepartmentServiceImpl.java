package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.DepartmentDao;
import com.dian.model.sys.Department;
import com.dian.service.sys.DepartmentService;

import core.service.BaseService;


@Service
public class DepartmentServiceImpl extends BaseService<Department> implements DepartmentService {

	private DepartmentDao departmentDao;

	@Resource
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
		this.dao = departmentDao;
	}

}
