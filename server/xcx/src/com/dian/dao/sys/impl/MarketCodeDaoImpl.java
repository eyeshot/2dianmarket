package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.MarketCodeDao;
import com.dian.model.sys.MarketCode;

import core.dao.BaseDao;

/**
 */
@Repository
public class MarketCodeDaoImpl extends BaseDao<MarketCode> implements MarketCodeDao {

	public MarketCodeDaoImpl() {
		super(MarketCode.class);
	}

}
