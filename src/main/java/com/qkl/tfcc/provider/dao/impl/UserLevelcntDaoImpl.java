package com.qkl.tfcc.provider.dao.impl;

import java.util.List;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.tfcc.api.po.user.UserLevelcnt;
import com.qkl.tfcc.provider.dao.UserLevelcntDao;
import com.qkl.tfcc.provider.dbhelper.DaoSupport;
import com.qkl.util.help.pager.PageData;

public class UserLevelcntDaoImpl extends DaoSupport<UserLevelcnt> implements
		UserLevelcntDao {

	protected static final Log logger = LogFactory.getLog(UserLevelcntDaoImpl.class);
	
	private String namespace = "UserLevelcnt";
	

	@Override
	public void addUserLevelcnt(UserLevelcnt userLevelcnt) {
		getSqlSession().insert(namespace+"."+"add", userLevelcnt);
	}

	@Override
	public void modifyUserLevelcnt(UserLevelcnt userLevelcnt) {
		getSqlSession().update(namespace+"."+"update", userLevelcnt);
	}

	@Override
	public List<PageData> findUserLevelcnt(PageData pd) {
		List<PageData> lvcntinfo = getSqlSession().selectList(namespace+"."+"findlvcntlistPage", pd);
		return lvcntinfo;
	}

}
