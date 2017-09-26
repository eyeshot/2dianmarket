package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.Config;

import core.service.Service;


public interface ConfigService extends Service<Config> {

	List<Config> getConfigList(List<Config> resultList);

}
