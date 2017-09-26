package com.dian.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dian.core.DianBaseController;
import com.dian.model.sys.SensorLastData;
import com.dian.service.sys.SensorLastDataService;


@Controller
@RequestMapping("/sys/sensorlastdata")
public class SensorLastDataController extends DianBaseController<SensorLastData> {

	@Resource
	private SensorLastDataService sensorLastDataService;

}
