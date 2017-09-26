package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.GroupDao;
import com.dian.model.sys.Group;

import core.dao.BaseDao;

/**
 */
@Repository
public class GroupDaoImpl extends BaseDao<Group> implements GroupDao {

	public GroupDaoImpl() {
		super(Group.class);
	}

}
