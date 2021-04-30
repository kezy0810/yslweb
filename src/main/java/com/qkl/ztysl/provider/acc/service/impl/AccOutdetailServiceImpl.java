package com.qkl.ztysl.provider.acc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.service.acc.api.AccOutdetailService;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dao.AccOutdetailDao;


@Service
public class AccOutdetailServiceImpl implements AccOutdetailService {

	private Logger loger = LoggerFactory.getLogger(AccOutdetailServiceImpl.class);
	
	@Autowired
	private AccOutdetailDao accOutdetailDao;
	@Autowired
	private AccInfoDao accDao;
	
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addAccOutdetail(PageData pd, String versionNo) {
	    //更新账户表，转出冻结
        boolean transferResult = accDao.transfering(pd);
        loger.info("调用转账接口---------更新账户表，转出冻结-----------结果--transferResult="+transferResult);
		boolean accOutdetailResult = accOutdetailDao.addAccOutdetail(pd);
		loger.info("调用转账接口---------添加转出记录结果-----------accOutdetailResult="+accOutdetailResult);
		return (transferResult&&accOutdetailResult);
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
        loger.info("转出回调-------transferCallBack--------更新数据库------------pd id is "+pd.getString("orderId"));
        boolean transferRestult = false;
        PageData accOutDetail = accOutdetailDao.getAccOutDetailByOrderId(pd.getString("orderId"));
        if(accOutDetail==null){//没有创建转出记录，或者orderId有误
            loger.info("转出回调-------转出记录不存在");
            return false;
        }
        pd.put("userCode", accOutDetail.getString("user_code"));
        loger.info("转出回调-------R8返回状态status="+pd.getString("status")+",orderid is "+pd.getString("orderId"));
        if("1".equals(pd.getString("status"))){//status-1成功 0-失敗
        	loger.info("转出回调-------更新账户表accDao.transferSuccess(pd) value is -=="+pd.toString());
            transferRestult = accDao.transferSuccess(pd);
            
            loger.info("转出回调-------更新账户表accDao.transferSuccess(pd)结果----transferRestult="+transferRestult+",pd id is "+pd.getString("orderId"));
            
            boolean outResult = accOutdetailDao.updateStatusByOrderId(pd);
            loger.info("转出回调-------更新转出记录accOutdetailDao.updateStatusByOrderId(pd)结果----outResult="+outResult+",pd id is "+pd.getString("orderId"));
            
            boolean result = (transferRestult&&outResult);
            loger.info("转出回调-------最终结果----restult="+result);
            return result;
        }else{
            transferRestult = accDao.transferfail(pd);
            loger.info("转出回调-------更新账户accDao.transferfail(pd)结果----transferRestult="+transferRestult+",pd id is "+pd.getString("orderId"));
            boolean outResult = accOutdetailDao.updateStatusByOrderId(pd);
            loger.info("转出回调-------更新转出记录accOutdetailDao.updateStatusByOrderId(pd)结果----outResult="+outResult+",pd id is "+pd.getString("orderId"));
            loger.info("转出回调-------最终结果----restult="+false);
            return false;
        }
    }

    @Override
    public PageData getTurnOutInfo(String orderId, String versionNo) {
        return accOutdetailDao.getTurnOutInfo(orderId);
    }

    @Override
    public boolean turnOutBack(PageData pd, String versionNo) {
        boolean transResult = accDao.transferfail(pd);
        boolean backResult = accOutdetailDao.updateStatusByFlowId(pd);
        return (backResult&&transResult);
    }

    @Override
    public boolean turnOutUpdate(PageData pd, String versionNo) {
        boolean backResult = accOutdetailDao.updateStatusByFlowId(pd);
        return backResult;
    }

    @Override
    public boolean findIsExistTransfering(String userCode, String versionNo) {
        return accOutdetailDao.findIsExistTransfering(userCode);
    }

}
