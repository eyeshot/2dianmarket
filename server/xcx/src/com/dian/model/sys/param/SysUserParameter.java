package com.dian.model.sys.param;

import core.extjs.ExtJSBaseParameter;


public class SysUserParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 7656443663108619135L;
	private String $like_userName;
	private String $like_realName;
	private Short $eq_role;
	private String roleName;

	public String get$like_userName() {
		return $like_userName;
	}

	public void set$like_userName(String $like_userName) {
		this.$like_userName = $like_userName;
	}

	public String get$like_realName() {
		return $like_realName;
	}

	public void set$like_realName(String $like_realName) {
		this.$like_realName = $like_realName;
	}

	public Short get$eq_role() {
		return $eq_role;
	}

	public void set$eq_role(Short $eq_role) {
		this.$eq_role = $eq_role;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
