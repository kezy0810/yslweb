package com.qkl.ztysl.provider.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.ComAccMy;
import com.qkl.ztysl.provider.dao.ComAccMyDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;



@Repository
public class ComAccMyDaoImpl extends DaoSupport<ComAccMy> implements ComAccMyDao {

	protected static final Log logger = LogFactory.getLog(ComAccMyDaoImpl.class);
	private String namespace="ComAccMy";
	
	@Override
	public BigDecimal findTB(String userCode) {
		BigDecimal cad=null;
		try {
			cad = getSqlSession().selectOne(namespace+"."+"findTB", userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return cad;
	}

	@Override
	public BigDecimal findJB(String userCode) {
		BigDecimal cad = getSqlSession().selectOne(namespace+"."+"findJB", userCode);
		return cad;
	}

	@Override
	public List<PageData> findAllPage(Page page) {
		List<PageData> list=null;
		try {
			list = getSqlSession().selectList(namespace+"."+"findAllPage",page);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return list;
	}

	@Override
	public BigDecimal findReward(String userCode) {
		BigDecimal cad=null;
		try {
			cad = getSqlSession().selectOne(namespace+"."+"findReward", userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return cad;
	}

	@Override
	public BigDecimal findWReward(String userCode) {
		BigDecimal cad = getSqlSession().selectOne(namespace+"."+"findWReward", userCode);
		return cad;
	}

	@Override
	public BigDecimal findTTReward(String userCode) {
		BigDecimal cad=null;
		try {
			cad = getSqlSession().selectOne(namespace+"."+"findTTReward", userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return cad;
	}

    @Override
    public PageData getAmnt(PageData pd) {
        return getSqlSession().selectOne(namespace+"."+"getAmnt", pd);
    }

    @Override
    public BigDecimal findFFReward(String userCode) {
        return getSqlSession().selectOne(namespace+"."+"findFFReward", userCode);
    }

	@Override
	public int insertOutAcc(PageData pd) {
		int insert = getSqlSession().insert(namespace+"."+"insertOutAcc",pd);
		return insert;
	}

	@Override
	public List<PageData> listPageAccOut(Page page) {
		return getSqlSession().selectList(namespace+"."+"listPageAccOut",page);
	}

}
