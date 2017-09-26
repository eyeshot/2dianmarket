package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.RoleAuthorityDao;
import com.dian.model.sys.RoleAuthority;

import core.dao.BaseDao;


@Repository
public class RoleAuthorityDaoImpl extends BaseDao<RoleAuthority> implements RoleAuthorityDao {

	public RoleAuthorityDaoImpl() {
		super(RoleAuthority.class);
	}
}
