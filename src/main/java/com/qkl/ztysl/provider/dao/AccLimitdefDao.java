package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.acc.AccLimitdef;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface AccLimitdefDao extends DAO<AccLimitdef> {
    
    /**
     * @describe:查询账户限额
     * @author: zhangchunming
     * @date: 2016年9月21日上午9:25:01
     * @param pd
     * @return: PageData
     */
	public PageData getAccLimit(PageData pd);
	
	
}
