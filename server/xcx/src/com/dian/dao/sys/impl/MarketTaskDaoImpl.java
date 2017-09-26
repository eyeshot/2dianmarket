package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.MarketTaskDao;
import com.dian.model.sys.MarketTask;
import com.dian.model.sys.Type;

import core.dao.BaseDao;

/**
 */
@Repository
public class MarketTaskDaoImpl extends BaseDao<MarketTask> implements MarketTaskDao {

	public MarketTaskDaoImpl() {
		super(MarketTask.class);
	}

}
