package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.SensorData;

import core.service.Service;

public interface SensorDataService extends Service<SensorData> {

	List<Object[]> doGetSensorDataStatistics(Short sensorType);

	List<Object[]> doGetEnhanceSensorDataStatistics(List<Object[]> list);

}
