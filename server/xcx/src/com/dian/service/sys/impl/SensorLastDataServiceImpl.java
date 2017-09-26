package com.dian.service.sys.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.SensorLastDataDao;
import com.dian.model.sys.SensorLastData;
import com.dian.service.sys.SensorLastDataService;

import core.service.BaseService;


@Service
public class SensorLastDataServiceImpl extends BaseService<SensorLastData> implements SensorLastDataService {

	private SensorLastDataDao sensorLastDataDao;

	@Resource
	public void setSensorLastDataDao(SensorLastDataDao sensorLastDataDao) {
		this.sensorLastDataDao = sensorLastDataDao;
		this.dao = sensorLastDataDao;
	}

}
