package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.MarketTaskDao;
import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.MarketTask;
import com.dian.service.sys.MarketTaskService;

import core.service.BaseService;

/**
 */
@Service
public class MarketTaskServiceImpl extends BaseService<MarketTask> implements MarketTaskService {

	private MarketTaskDao mDao;

	@Resource
	public void setTypeDao(MarketTaskDao paramDao) {
		this.mDao = paramDao;
		this.dao = paramDao;
	}

}
