package com.qkl.ztysl.provider.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.trade.TradeDetail;
import com.qkl.ztysl.provider.dao.TradeDetailDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;


@Repository
public class TradeDetailDaoImpl extends DaoSupport<TradeDetail> implements TradeDetailDao {

	protected static final Log logger = LogFactory.getLog(TradeDetailDaoImpl.class);
	
	private String namespace = "TradeDetail";
	    
	@Override
	public void addTradeDetail(PageData pd) {
		getSqlSession().insert(namespace+"."+"add", pd);	
		
	}

	@Override
	public void modifyTradeDetail(PageData pd) {
		getSqlSession().update(namespace+"."+"update", pd);	
		
	}

	@Override
	public void modifyTradeStatus(PageData pd) {
		getSqlSession().update(namespace+"."+"updatestatus", pd);
		
	}

	@Override
	public List<PageData> findTradeInfo(Page page) {
		List<PageData> tradeinfo = getSqlSession().selectList(namespace+"."+"findTradelistPage", page);
		return tradeinfo;
	}

	@Override
	public int findTradeCount(PageData pd) {
		/*int tradecount = getSqlSession().selectOne(namespace+"."+"findTradeCount", pd);*/
		return 0;
	}

	@Override
	public BigDecimal findTradeAmnt(String userCode) {
		BigDecimal findTradeAmnt = getSqlSession().selectOne(namespace+"."+"findTradeAmnt", userCode);
		return findTradeAmnt;
	}
}
