package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.ConfigDao;
import com.dian.model.sys.Config;

import core.dao.BaseDao;


@Repository
public class ConfigDaoImpl extends BaseDao<Config> implements ConfigDao {

	public ConfigDaoImpl() {
		super(Config.class);
	}

}
