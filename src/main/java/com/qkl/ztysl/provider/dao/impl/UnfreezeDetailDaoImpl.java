package com.qkl.ztysl.provider.dao.impl;


import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.trade.UnfreezeDetail;
import com.qkl.ztysl.provider.dao.UnfreezeDetailDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;

@Repository
public class UnfreezeDetailDaoImpl extends DaoSupport<UnfreezeDetail> implements
		UnfreezeDetailDao {

	protected static final Log logger = LogFactory.getLog(UnfreezeDetailDaoImpl.class);
	
	private String namespace = "UnfreezeDetail";
	
	@Override
	public boolean addUnfreezeDetail(PageData pd) {
		try{
		   getSqlSession().insert(namespace+"."+"add", pd);	
		   return true;
		}catch(Exception e){			
			logger.error("addUnfreezeDetail fail, reason is "+e.getMessage());
			return false;
		}
	}

	@Override
	public boolean modifyUnfreezeDetailStatus(PageData pd) {
		try{
			   getSqlSession().update(namespace+"."+"updatestatus", pd);	
			   return true;
			}catch(Exception e){			
				logger.error("modifyUnfreezeDetailStatus fail, reason is "+e.getMessage());
				return false;
			}
	}

}
