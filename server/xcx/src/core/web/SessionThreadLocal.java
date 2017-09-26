package core.web;

import com.dian.model.sys.SysUser;


public class SessionThreadLocal {

	private static ThreadLocal<SysUser> ADMINUSERTHREADLOCAL = new ThreadLocal<SysUser>();

	public static SysUser getThreadSysUser() {
		return ADMINUSERTHREADLOCAL.get();
	}

	public static void setThreadSysUser(SysUser sysUser) {
		ADMINUSERTHREADLOCAL.set(sysUser);
	}

}
