package com.qkl.ztysl.provider.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.user.UserLevelcnt;
import com.qkl.ztysl.provider.dao.UserLevelcntDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;
@Repository
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
	public List<PageData> findUserLevelcntlist(PageData pd) {
		List<PageData> lvcntinfo = getSqlSession().selectList(namespace+"."+"findlvcntlistPage", pd);
		return lvcntinfo;
	}

	@Override
	public UserLevelcnt findUserLevelcnt(PageData pd) {
		return getSqlSession().selectOne(namespace+"."+"find", pd);
		 
	}

}
