package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.MarketTaskImgDao;
import com.dian.model.sys.MarketTaskImg;
import com.dian.model.sys.Type;

import core.dao.BaseDao;

/**
 */
@Repository
public class MarketTaskImgDaoImpl extends BaseDao<MarketTaskImg> implements MarketTaskImgDao {

	public MarketTaskImgDaoImpl() {
		super(MarketTaskImg.class);
	}

}
