package com.dian.dao.sys;

import java.util.List;

import com.dian.model.sys.MonitorLog;

import core.dao.Dao;


public interface MonitorLogDao extends Dao<MonitorLog> {

	List<MonitorLog> querySensorMonitorLog();

}
