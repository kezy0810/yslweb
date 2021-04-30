package com.qkl.ztysl.provider.dao;

import java.util.List;
import java.util.Map;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.api.po.acc.LPTeam;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface LPaccDao extends DAO<AccDetail> {
	
	/**
	 * 查询LP会员ymd余额
	 * @param userCode
	 * @return
	 */
	public long findLPBalance(String userCode);
	
	
	/**
	 * 查询LP会员累计购买股权的奖励
	 * @param userCode
	 * @return
	 */
	
	public long findtotalReward(String userCode);
	
	/**
	 * 查询推荐LP购买股权的奖励
	 * @param userCode
	 * @return
	 */
	public long findrefReward(String userCode);
	
	
	/**
	 * 查询我购买股权的奖励
	 * @param userCode
	 * @return
	 */
	public long findmyReward(String userCode);
	
	/**
	 * 查询LP会员的奖励信息列表
	 * @param map
	 * @return
	 */
	public List<PageData> findRewardInfo(Page page);
	

}
