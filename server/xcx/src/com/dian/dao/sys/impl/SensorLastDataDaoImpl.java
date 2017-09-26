package com.dian.dao.sys.impl;

import org.springframework.stereotype.Repository;

import com.dian.dao.sys.SensorLastDataDao;
import com.dian.model.sys.SensorLastData;

import core.dao.BaseDao;


@Repository
public class SensorLastDataDaoImpl extends BaseDao<SensorLastData> implements SensorLastDataDao {

	public SensorLastDataDaoImpl() {
		super(SensorLastData.class);
	}

}
