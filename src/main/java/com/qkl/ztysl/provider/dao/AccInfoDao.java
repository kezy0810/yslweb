package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.acc.AccInfo;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface AccInfoDao extends DAO<AccInfo> {

	
	
	/** 添加用户账户信息
	 * @return addAcc 添加用户账户明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月8日
	 */ 
	public void addAcc(AccInfo accInfo);
	/** 修改用户账户信息
	 * @return modifyAcc  修改用户账户明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月8日
	 */ 
	public void modifyAcc(AccInfo accInfo);
	
	
	/** 查询用户账户信息
	 * @return findAcc  查询用户账户信息
	 * @create author kezhiyi
	 * @create date 2016年9月8日
	 */ 
	public AccInfo findAcc(PageData pd);
	/**
	 * @describe:查询用户账户可用ymd余额
	 * @author: zhangchunming
	 * @date: 2016年9月9日上午10:07:59
	 * @param acc
	 * @return: Integer
	 */
	public Integer getAvailableBalance(AccInfo accInfo);
	/**
	 * @describe:收入
	 * @author: zhangchunming
	 * @date: 2016年9月9日上午10:08:48
	 * @param acc
	 * @return: void
	 */
	public boolean updateIn(AccInfo accInfo);
	/**
	 * @describe:支出
	 * @author: zhangchunming
	 * @date: 2016年9月9日上午10:09:52
	 * @param acc
	 * @return: void
	 */
	public boolean updateOut(AccInfo accInfo);
	/**
	 * @describe:支出
	 * @author: zhangchunming
	 * @date: 2016年9月9日上午10:09:52
	 * @param acc
	 * @return: void
	 */
	public void updatefroze(PageData pd);
	/**
	 * @describe:解冻-比率5%
	 * @author: zhangchunming
	 * @date: 2016年9月30日上午9:41:25
	 * @param ratio
	 * @return: void
	 */
	public boolean thaw(String ratio);
	/**
	 * @describe:转账申请
	 * @author: zhangchunming
	 * @date: 2016年10月10日上午11:12:11
	 * @param pd
	 * @return: boolean
	 */
	public boolean transfering(PageData pd);
	/**
	 * @describe:转账成功
	 * @author: zhangchunming
	 * @date: 2016年10月10日上午11:12:59
	 * @param pd
	 * @return: boolean
	 */
	public boolean transferSuccess(PageData pd);
	/**
	 * @describe:转账失败
	 * @author: zhangchunming
	 * @date: 2016年10月10日上午11:13:33
	 * @param pd
	 * @return: boolean
	 */
	public boolean transferfail(PageData pd);
	
}
