package com.qkl.tfcc.provider.dao;

import com.qkl.tfcc.api.po.trade.TradeDetail;
import com.qkl.tfcc.provider.dbhelper.DAO;


public interface TradeDetailDao extends DAO<TradeDetail> {

	/** 添加交易信息明细
	 * @return addTradeDetail 添加交易信息明细
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */ 
	public void addTradeDetail(TradeDetail tradeDetail);
	/** 修改交易信息明细
	 * @return modifyTradeDetail 修改交易信息明细
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */
	public void modifyTradeDetail(TradeDetail tradeDetail);
	/** 修改交易状态
	 * @return modifyTradeStatus 修改交易状态
	 * @create author kezhiyi
	 * @create date 2016年9月6日
	 */
	public void modifyTradeStatus(TradeDetail tradeDetail);
	
	
}
