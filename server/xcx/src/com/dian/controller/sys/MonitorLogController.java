package com.dian.controller.sys;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dian.core.DianBaseController;
import com.dian.model.sys.MonitorLog;
import com.dian.service.sys.MonitorLogService;


@Controller
@RequestMapping("/sys/monitorlog")
public class MonitorLogController extends DianBaseController<MonitorLog> {

	@Resource
	private MonitorLogService monitorLogService;

	@RequestMapping("/getSensorMonitorLog")
	public void getSensorMonitorLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<MonitorLog> sensorMonitorLogList = monitorLogService.querySensorMonitorLog();
		writeJSON(response, sensorMonitorLogList);
	}

}
