package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.MarketCodeDao;
import com.dian.model.sys.MarketCode;
import com.dian.service.sys.MarketCodeService;

import core.service.BaseService;

/**
 */
@Service
public class MarketCodeServiceImpl extends BaseService<MarketCode> implements MarketCodeService {

	private MarketCodeDao mDao;

	@Resource
	public void setTypeDao(MarketCodeDao paramDao) {
		this.mDao = paramDao;
		this.dao = paramDao;
	}

}
