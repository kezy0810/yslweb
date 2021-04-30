package com.qkl.ztysl.provider.dao;

import java.util.List;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.user.CorpTeam;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface CorpTeamDao extends DAO<CorpTeam> {

	/**
	 * 查询投资公司LP会员数量
	 * @param userCode
	 * @return
	 */
	public long findLPNum(String userCode);
	
	/**
	 * 查询投资公司的LP会员信息列表
	 * @param page
	 * @return
	 */
	public List<PageData> findlpInfo(Page page);
	
}
