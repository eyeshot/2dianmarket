package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.ServiceGroupDao;
import com.dian.model.sys.Department;
import com.dian.model.sys.Servicegroup;

import core.dao.BaseDao;

/**
 * @author eyeshot
 */
@Repository
public class ServiceGroupDaoImpl extends BaseDao<Servicegroup> implements ServiceGroupDao {

	public ServiceGroupDaoImpl() {
		super(Servicegroup.class);
	}

}
