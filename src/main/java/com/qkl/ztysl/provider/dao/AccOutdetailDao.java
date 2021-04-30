package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.acc.AccOutdetail;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface AccOutdetailDao extends DAO<AccOutdetail> {

	/** 添加转出明细信息
	 * @return addAccOutdetail添加转出明细信息
	 * @create author kezhiyi
	 * @create date 2016年9月13日
	 */ 
	public boolean addAccOutdetail(PageData pd);
	
	/** 修改转出明细信息状态 
	 * @return modifyAccOutdetailStatus修改转出明细信息状态 
	 * @create author kezhiyi
	 * @create date 2016年9月13日
	 */ 
	public void modifyAccOutdetailStatus(PageData pd);
	/**
	 * @describe:转账回调时更新状态
	 * @author: zhangchunming
	 * @date: 2016年10月10日上午10:52:13
	 * @param pd
	 * @return: void
	 */
	public boolean updateStatusByOrderId(PageData pd);
	/**
	 * @describe:根据订单id查询转账记录
	 * @author: zhangchunming
	 * @date: 2016年10月10日下午3:35:38
	 * @param orderId
	 * @return: PageData
	 */
	public PageData getAccOutDetailByOrderId(String orderId);
	/**
	 * @describe:根据订单号查询相关转账信息
	 * @author: zhangchunming
	 * @date: 2016年10月14日下午2:50:51
	 * @param orderId
	 * @return: PageData
	 */
	public PageData getTurnOutInfo(String orderId);
	/**
	 * @describe:根据流水号修改订单状态
	 * @author: zhangchunming
	 * @date: 2016年10月14日下午11:18:54
	 * @param pd
	 * @return: boolean
	 */
	public boolean updateStatusByFlowId(PageData pd);
	/**
	 * @describe:查询是否有转账中的数据
	 * @author: zhangchunming
	 * @date: 2016年10月15日上午11:34:23
	 * @param userCode
	 * @return
	 * @return: boolean
	 */
	public boolean findIsExistTransfering(String userCode);
}
