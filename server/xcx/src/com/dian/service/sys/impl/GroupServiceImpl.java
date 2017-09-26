package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.GroupDao;
import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.Group;
import com.dian.service.sys.GroupService;

import core.service.BaseService;

/**
 */
@Service
public class GroupServiceImpl extends BaseService<Group> implements GroupService {

	private GroupDao groupDao;

	@Resource
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
		this.dao = groupDao;
	}

}
