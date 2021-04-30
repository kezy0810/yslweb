package com.qkl.ztysl.provider.dao;

import java.util.List;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface LPmemberDao extends DAO<UserDetail> {
	
	/**
	 * 查询LP会员数量
	 * @param userDetail
	 * @return
	 */
	public long findLPmemberNum(String userCode);
	
	
	/**
	 * 查询LP会员信息
	 * @param userDetail
	 * @return
	 */
	public List<PageData> findLPmemberInfo(Page page);

}
