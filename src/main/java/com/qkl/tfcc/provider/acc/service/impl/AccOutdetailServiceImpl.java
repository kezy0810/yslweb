package com.qkl.tfcc.provider.acc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.tfcc.api.service.acc.api.AccOutdetailService;
import com.qkl.tfcc.provider.dao.AccDao;
import com.qkl.tfcc.provider.dao.AccOutdetailDao;
import com.qkl.util.help.pager.PageData;


@Service
public class AccOutdetailServiceImpl implements AccOutdetailService {

	private Logger loger = LoggerFactory.getLogger(AccOutdetailServiceImpl.class);
	
	@Autowired
	private AccOutdetailDao accOutdetailDao;
	@Autowired
	private AccDao accDao;
	
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addAccOutdetail(PageData pd, String versionNo) {
		try{			
			accOutdetailDao.addAccOutdetail(pd);
			return true;
		}catch(Exception e){
			loger.debug("addAccOutdetail fail,reason is "+e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyAccOutdetailStatus(PageData pd, String versionNo) {
		try{			
			accOutdetailDao.modifyAccOutdetailStatus(pd);
			return true;
		}catch(Exception e){
			loger.debug("modifyAccOutdetailStatus fail,reason is "+e.getMessage());
			return false;
		}
	}

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public boolean transferCallBack(PageData pd, String versionNo) {
        loger.info("转出回调-------transferCallBack--------更新数据库------------");
        boolean transferRestult = false;
        PageData accOutDetail = accOutdetailDao.getAccOutDetailByOrderId(pd.getString("orderId"));
        if(accOutDetail==null){//没有创建转出记录，或者orderId有误
            loger.info("转出回调-------转出记录不存在");
            return false;
        }
        pd.put("userCode", accOutDetail.getString("user_code"));
        loger.info("转出回调-------R8返回状态status="+pd.getString("status"));
        if("1".equals(pd.getString("status"))){//status-1成功 0-失敗
            transferRestult = accDao.transferSuccess(pd);
            loger.info("转出回调-------更新账户表accDao.transferSuccess(pd)结果----transferRestult="+transferRestult);
            
            boolean outResult = accOutdetailDao.updateStatusByOrderId(pd);
            loger.info("转出回调-------更新转出记录accOutdetailDao.updateStatusByOrderId(pd)结果----outResult="+outResult);
            
            boolean result = (transferRestult&&outResult);
            loger.info("转出回调-------最终结果----restult="+result);
            return result;
        }else{
            transferRestult = accDao.transferfail(pd);
            loger.info("转出回调-------更新账户accDao.transferfail(pd)结果----transferRestult="+transferRestult);
            boolean outResult = accOutdetailDao.updateStatusByOrderId(pd);
            loger.info("转出回调-------更新转出记录accOutdetailDao.updateStatusByOrderId(pd)结果----outResult="+outResult);
            loger.info("转出回调-------最终结果----restult="+false);
            return false;
        }
    }

}
