package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.DianTypeDao;
import com.dian.model.sys.DianType;

import core.dao.BaseDao;


@Repository
public class ForestryTypeDaoImpl extends BaseDao<DianType> implements DianTypeDao {

	public ForestryTypeDaoImpl() {
		super(DianType.class);
	}

}
