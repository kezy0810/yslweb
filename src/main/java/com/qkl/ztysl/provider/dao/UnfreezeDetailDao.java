package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.trade.UnfreezeDetail;
import com.qkl.ztysl.provider.dbhelper.DAO;



public interface UnfreezeDetailDao extends DAO<UnfreezeDetail> {

	/** 添加解冻信息明细
	 * @return addUnfreezeDetail 添加解冻信息明细
	 * @create author kezhiyi
	 * @create date 2016年9月13日
	 */ 
	public boolean addUnfreezeDetail(PageData pd);
	/** 修改解冻状态
	 * @return addUnfreezeDetail 修改解冻状态
	 * @create author kezhiyi
	 * @create date 2016年9月13日
	 */ 
	public boolean modifyUnfreezeDetailStatus(PageData pd);
	
	
	
}
