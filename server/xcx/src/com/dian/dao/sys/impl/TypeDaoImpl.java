package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.Type;

import core.dao.BaseDao;


@Repository
public class TypeDaoImpl extends BaseDao<Type> implements TypeDao {

	public TypeDaoImpl() {
		super(Type.class);
	}

}
