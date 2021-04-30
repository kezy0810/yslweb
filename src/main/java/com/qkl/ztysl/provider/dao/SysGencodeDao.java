package com.qkl.ztysl.provider.dao;

import java.util.List;
import java.util.Map;

import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface SysGencodeDao extends DAO<SysGencode> {

	
	/** 查询所有代码
	 * @return findAll  查询所有代码
	 * @create author kezhiyi
	 * @create date 2016年8月24日
	 */ 
	public List<SysGencode> findAll();
	/** 根据groupid查询所有代码
	 * @return findAll  根据groupid查询所有代码
	 * @create author kezhiyi
	 * @create date 2016年8月24日
	 */	
	public List<Map<String,Object>> findByGroupCode(String groupid);
	
}
