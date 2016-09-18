package com.qkl.tfcc.provider.dao;

import java.util.List;

import com.qkl.tfcc.api.po.user.UserLevelcnt;
import com.qkl.tfcc.provider.dbhelper.DAO;
import com.qkl.util.help.pager.PageData;

public interface UserLevelcntDao extends DAO<UserLevelcnt> {

	/** 添加用户级别数量
	 * @return addUserLevelcnt 添加用户级别数量
	 * @create author kezhiyi
	 * @create date 2016年9月18日
	 */ 
	public void addUserLevelcnt(UserLevelcnt userLevelcnt);
	/** 修改用户级别数量
	 * @return modifyUserLevelcnt 修改用户级别数量
	 * @create author kezhiyi
	 * @create date 2016年9月18日
	 */ 
	public void modifyUserLevelcnt(UserLevelcnt userLevelcnt);
	
	
	/** 查询用户级别数量列表
	 * @return addUserLevelcnt 添加用户级别数量列表
	 * @create author kezhiyi
	 * @create date 2016年9月18日
	 */ 
	public List<PageData> findUserLevelcntlist(PageData pd);
	/** 查询用户级别数量
	 * @return addUserLevelcnt 添加用户级别数量
	 * @create author kezhiyi
	 * @create date 2016年9月18日
	 */ 
	public PageData findUserLevelcnt(PageData pd);
	
	
}
