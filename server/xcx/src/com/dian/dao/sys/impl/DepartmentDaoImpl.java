package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.DepartmentDao;
import com.dian.model.sys.Department;

import core.dao.BaseDao;


@Repository
public class DepartmentDaoImpl extends BaseDao<Department> implements DepartmentDao {

	public DepartmentDaoImpl() {
		super(Department.class);
	}

}
