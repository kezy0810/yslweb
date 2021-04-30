package com.qkl.ztysl.provider.dao;

import java.math.BigDecimal;
import java.util.List;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.trade.TradeDetail;
import com.qkl.ztysl.provider.dbhelper.DAO;


public interface TradeDetailDao extends DAO<TradeDetail> {

	/** 添加交易信息明细
	 * @return addTradeDetail 添加交易信息明细
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */ 
	public void addTradeDetail(PageData pd);
	/** 修改交易信息明细
	 * @return modifyTradeDetail 修改交易信息明细
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */
	public void modifyTradeDetail(PageData pd);
	/** 修改交易状态
	 * @return modifyTradeStatus 修改交易状态
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */
	public void modifyTradeStatus(PageData pd);
	
	
	/**
	 * 查询交易信息列表
	 * @return
	 */
	public List<PageData> findTradeInfo(Page page);
	/***
	 * 查询交易次数
	 * @param pd
	 * @return
	 */
	public int findTradeCount(PageData pd);
	
	/**
     * 查询单个用户的交易额总量
     * @param userCode
     * @return
     */
    
    public BigDecimal findTradeAmnt(String userCode);
	
	
}
