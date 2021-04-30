package com.qkl.ztysl.provider.dao.impl;

import org.springframework.stereotype.Repository;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.acc.AccLimitdef;
import com.qkl.ztysl.provider.dao.AccLimitdefDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;

@Repository
public class AccLimitdefDaoImpl extends DaoSupport<AccLimitdef> implements AccLimitdefDao {

protected static final Log logger = LogFactory.getLog(AccLimitdefDaoImpl.class);
    
    private String namespace = "AccLimitdef";

    @Override
    public PageData getAccLimit(PageData pd) {
        return getSqlSession().selectOne(namespace+"."+"findAccLimit", pd);   
    }
}
