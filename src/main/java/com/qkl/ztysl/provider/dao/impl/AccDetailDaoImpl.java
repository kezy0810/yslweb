package com.qkl.ztysl.provider.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.provider.dao.AccDetailDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;



/**AccDetailDao的实现
 * <p>AccDetailDao的实现  </p>
 * @project_Name qkl_ymd_web
 * @class_Name AccDetailDaoImpl.java
 * @author kezhiyi
 * @date 2016年8月29日
 * @version v1.0
 */
@Repository
public class AccDetailDaoImpl extends DaoSupport<AccDetail> implements AccDetailDao {

	
	protected static final Log logger = LogFactory.getLog(SmsSendDaoImpl.class);
	
	private String namespace = "AccDetail";

	@Override
	public void addAccDetail(AccDetail accDetail) {
		getSqlSession().insert(namespace+"."+"addAccDetail", accDetail);	
		
	}
	
	@Override
	public void addAccDetaillv(PageData pd) {
		getSqlSession().insert(namespace+"."+"addlv", pd);	
		
	}

    @Override
    public void updateAccDetail(AccDetail accDetail) {
        getSqlSession().update(namespace+"."+"updateAccDetail", accDetail);    
    }

    @Override
    public List<PageData> findAccDetailPage(Page page) {
        return getSqlSession().selectList(namespace+"."+"findAccDetailPage", page);
    }

    @Override
    public List<PageData> findAccDetailList(PageData pd) {
        return getSqlSession().selectList(namespace+"."+"findAccDetailList", pd);
    }

    @Override
    public AccDetail findAccDetail(AccDetail accDetail) {
        return getSqlSession().selectOne(namespace+"."+"findAccDetail", accDetail);
    }
    
    
    //////////////////////////////////////////////////
    //***以下为2020年10月
    //////////////////////////////////////////////////
    
    
    /**
     * 自己做任务的奖励
     * **/
	@Override
	public void addAccDetailSelfTask(PageData pd) {
		// TODO Auto-generated method stub
		getSqlSession().insert(namespace+"."+"addselftask", pd);	

	}

	
	/****
	 * 下级做任务的推荐奖励
	 */
	@Override
	public void addAccDetailRefTask(PageData pd) {
		// TODO Auto-generated method stub
		getSqlSession().insert(namespace+"."+"addreftask", pd);	

	}
    

}
