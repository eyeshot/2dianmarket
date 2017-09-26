package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.Authority;

import core.dao.Dao;


public interface AuthorityDao extends Dao<Authority> {

	List<Authority> queryByParentIdAndRole(Short role);

	List<Authority> queryChildrenByParentIdAndRole(Long parentId, Short role);

}
