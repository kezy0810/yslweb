package com.qkl.ztysl.provider.dao;

import java.util.List;

import com.qkl.ztysl.api.po.acc.BankAccInfo;
import com.qkl.ztysl.provider.dbhelper.DAO;
public interface BankAccDao extends DAO<BankAccInfo> {

	
	/** 根据查收款账户信息
	 * @return findBankAccInfo
	 * @create author kezhiyi
	 * @create date 2016年9月5日
	 */ 
	public BankAccInfo findBankAccInfo(String orgName);
	
	/***
	 * 查询银行信息
	 * @return
	 */
	public BankAccInfo findBankInfo();
	
}
