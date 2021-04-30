package com.qkl.ztysl.provider.dao.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.acc.AccInfo;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;


@Repository
public class AccInfoDaoImpl extends DaoSupport<AccInfo> implements AccInfoDao {

protected static final Log logger = LogFactory.getLog(AccInfoDaoImpl.class);
	
	private String namespace = "AccInfo";

    @Override
    public void addAcc(AccInfo accInfo) {
        getSqlSession().insert(namespace+"."+"addAcc", accInfo);    
    }

    @Override
    public void modifyAcc(AccInfo accInfo) {
        getSqlSession().update(namespace+"."+"modifyAcc", accInfo);    
    }

    @Override
    public AccInfo findAcc(PageData accInfo) {
        return (AccInfo)getSqlSession().selectOne(namespace+"."+"findAcc", accInfo);    
    }

    @Override
    public Integer getAvailableBalance(AccInfo accInfo) {
        return (Integer)getSqlSession().selectOne(namespace+"."+"getAvailableBalance", accInfo); 
    }

    @Override
    public boolean updateIn(AccInfo accInfo) {
         return getSqlSession().update(namespace+"."+"updateIn", accInfo)>0; 
    }

    @Override
    public boolean updateOut(AccInfo accInfo) {
        return getSqlSession().update(namespace+"."+"updateOut", accInfo)>0; 
    }

	@Override
	public void updatefroze(PageData pd) {		
		 getSqlSession().update(namespace+"."+"updatefroze", pd); 
	}

    @Override
    public boolean thaw(String ratio) {
       return getSqlSession().update(namespace+"."+"thaw", ratio)>0; 
    }

    @Override
    public boolean transfering(PageData pd) {
        return getSqlSession().update(namespace+"."+"transfering", pd)>0; 
    }

    @Override
    public boolean transferSuccess(PageData pd) {
        return getSqlSession().update(namespace+"."+"transferSuccess", pd)>0; 
    }

    @Override
    public boolean transferfail(PageData pd) {
        return getSqlSession().update(namespace+"."+"transferfail", pd)>0; 
    }
	
}
