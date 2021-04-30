package com.qkl.ztysl.provider.acc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.service.acc.api.AccLimitService;
import com.qkl.ztysl.provider.dao.AccLimitdefDao;
/**
 * 账户限额接口
 * @author zhangchunming
 * @date: 2016年9月21日上午9:27:07
 */
public class AccLimitServiceImpl implements AccLimitService{
    
    @Autowired
    private AccLimitdefDao accLimitdefDao;
    @Override
    public PageData getAccLimit(PageData pd, String versionNo) {
        return accLimitdefDao.getAccLimit(pd);
    }
    
	
}
