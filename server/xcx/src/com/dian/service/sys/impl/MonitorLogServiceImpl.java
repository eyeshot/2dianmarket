package com.dian.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dian.dao.sys.MonitorLogDao;
import com.dian.model.sys.MonitorLog;
import com.dian.service.sys.MonitorLogService;

import core.service.BaseService;


@Service
public class MonitorLogServiceImpl extends BaseService<MonitorLog> implements MonitorLogService {

	private MonitorLogDao monitorLogDao;

	@Resource
	public void setMonitorLogDao(MonitorLogDao monitorLogDao) {
		this.monitorLogDao = monitorLogDao;
		this.dao = monitorLogDao;
	}

	@Override
	public List<MonitorLog> querySensorMonitorLog() {
		return monitorLogDao.querySensorMonitorLog();
	}

}
