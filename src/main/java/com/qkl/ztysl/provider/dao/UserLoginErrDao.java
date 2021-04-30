package com.qkl.ztysl.provider.dao;

import com.qkl.ztysl.api.po.user.LoginErr;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface UserLoginErrDao extends DAO<LoginErr> {

	/** 添加用户登录失败信息
	 * @return addLoginErr 用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void addLoginErr(LoginErr loginErr);
	/** 修改用户登录失败信息
	 * @return modifyLoginErr 用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void modifyLoginErr(LoginErr loginErr);
	/** 查询用户登录失败信息
	 * @return modifyLoginErr 用查询用户登录失败信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void findLoginErrByUserCode(String  userCode);
	
}
