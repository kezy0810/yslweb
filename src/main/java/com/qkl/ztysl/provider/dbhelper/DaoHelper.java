/**
 * 
 */
package com.qkl.ztysl.provider.dbhelper;

import java.util.Date;

public interface DaoHelper {
	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public abstract Date getCurrentTime();

	/**
	 * 
	 * @param name
	 * @param type
	 * @param callback
	 * @return
	 */
	Object doInTransaction(String name, int type, CallbackHandle callback);
}
