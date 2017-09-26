package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.Sensor;

import core.dao.Dao;
import core.support.QueryResult;


public interface SensorDao extends Dao<Sensor> {

	List<Sensor> querySensorBySensorType(Short sensorType);

	List<Sensor> querySensorLastData();

	QueryResult<Sensor> querySensorList(Sensor sensor);

	List<Sensor> querySensorLastDataWithEpcId();

	List<Sensor> queryForestrySensorLastData();

}
