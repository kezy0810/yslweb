package com.qkl.ztysl.provider.dao.impl;

import org.springframework.stereotype.Repository;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.ztysl.api.po.sys.SysMaxnum;
import com.qkl.ztysl.provider.dao.SysMaxnumDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;



@Repository
public class SysMaxnumDaoImpl extends DaoSupport<SysMaxnum> implements SysMaxnumDao {

	protected static final Log logger = LogFactory.getLog(UserDaoImpl.class);
	
	private String namespace = "SysMaxnum";
	
	
	@Override
	public SysMaxnum findMaxnum(String notype) {
		SysMaxnum sysMaxnum =getSqlSession().selectOne(namespace+"."+"find", notype);
		return sysMaxnum;
	}

	@Override
	public void modifyMaxnum(SysMaxnum sysMaxnum) {
		getSqlSession().update(namespace+"."+"update", sysMaxnum);	

	}

	
}
