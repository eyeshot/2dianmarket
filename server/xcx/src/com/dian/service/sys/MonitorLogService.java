package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.MonitorLog;

import core.service.Service;


public interface MonitorLogService extends Service<MonitorLog> {

	List<MonitorLog> querySensorMonitorLog();

}
