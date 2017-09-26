package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.SensorData;

import core.dao.Dao;


public interface SensorDataDao extends Dao<SensorData> {

	List<Object[]> doGetSensorDataStatistics(Short sensorType);

}
