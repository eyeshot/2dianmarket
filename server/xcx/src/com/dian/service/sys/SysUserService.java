package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.SysUser;

import core.service.Service;


public interface SysUserService extends Service<SysUser> {

	List<SysUser> getSysUserList(List<SysUser> resultList);

}
