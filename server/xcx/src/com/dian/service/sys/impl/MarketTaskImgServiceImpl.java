package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.MarketTaskImgDao;
import com.dian.dao.sys.TypeDao;
import com.dian.model.sys.MarketTaskImg;
import com.dian.service.sys.MarketTaskImgService;

import core.service.BaseService;

/**
 */
@Service
public class MarketTaskImgServiceImpl extends BaseService<MarketTaskImg> implements MarketTaskImgService {

	private MarketTaskImgDao mDao;

	@Resource
	public void setTypeDao(MarketTaskImgDao paramDao) {
		this.mDao = paramDao;
		this.dao = paramDao;
	}

}
