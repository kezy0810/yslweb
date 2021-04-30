package com.qkl.ztysl.provider.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.provider.dao.LPaccDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;

@Repository
public class LPaccDaoImpl extends DaoSupport<AccDetail> implements LPaccDao {

	protected static final Log logger = LogFactory.getLog(LPaccDaoImpl.class);
	
	
	private String namespace = "LPTeam";
	
	
	@Override
	public long findLPBalance(String userCode) {
		long ymd=0;
		try {
			ymd = getSqlSession().selectOne(namespace+"."+"findLPBalance",userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return ymd;
	}
	@Override
	public long findtotalReward(String userCode) {
		long tymd=0;
		try {
			tymd = getSqlSession().selectOne(namespace+"."+"findtotalReward",userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return tymd;
	}
	@Override
	public long findrefReward(String userCode) {
		long rymd=0;
		try {
			rymd = getSqlSession().selectOne(namespace+"."+"findrefReward",userCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return rymd;
	}
	@Override
	public long findmyReward(String userCode) {
		long myymd=0;
		try {
			myymd = getSqlSession().selectOne(namespace+"."+"findmyReward",userCode);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return myymd;
	}
	@Override
	public List<PageData> findRewardInfo(Page page) {
		List<PageData> rewardInfo=null;
		try {
			rewardInfo = getSqlSession().selectList(namespace+"."+"findRewardInfoPage",page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据为空");
		}
		return rewardInfo;
	}

	

}
