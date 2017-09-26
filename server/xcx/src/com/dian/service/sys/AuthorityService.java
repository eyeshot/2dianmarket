package com.dian.service.sys;

import java.util.List;

import com.dian.model.sys.Authority;
import com.dian.model.sys.RoleAuthority;

import core.service.Service;


public interface AuthorityService extends Service<Authority> {

	List<Authority> queryByParentIdAndRole(Short role);

	List<Authority> queryChildrenByParentIdAndRole(Long parentId, Short role);

	String querySurfaceAuthorityList(List<RoleAuthority> queryByProerties, Long id, String buttons);

}
