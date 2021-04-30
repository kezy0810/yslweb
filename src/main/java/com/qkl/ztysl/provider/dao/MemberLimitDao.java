package com.qkl.ztysl.provider.dao;

import com.qkl.ztysl.api.po.acc.MemberLimit;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface MemberLimitDao extends DAO<MemberLimit> {

	/** 添加会员额度信息
	 * @return addMemberLimit 添加用户账户明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月7日
	 */ 
	public void addMemberLimit(MemberLimit memberLimit);
	
	/** 修改会员额度信息
	 * @return modifyMemberLimit 修改用户账户明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月7日
	 */ 
	public void modifyMemberLimit(MemberLimit memberLimit);
	
	/** 查询会员额度信息
	 * @return findMemberLimit 修改用户账户明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月7日
	 */ 
	public MemberLimit findMemberLimit(String userCode);
	
	
}
