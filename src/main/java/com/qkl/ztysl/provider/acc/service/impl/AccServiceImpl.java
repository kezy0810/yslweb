package com.qkl.ztysl.provider.acc.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.Validator;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.entity.Page;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.api.po.acc.AccInfo;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.provider.dao.AccDetailDao;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dao.AccLimitdefDao;
import com.qkl.ztysl.provider.dao.UserDao;
import com.qkl.ztysl.provider.dao.UserDetailDao;
import com.qkl.ztysl.provider.dao.UserFriendshipDao;




@Service
public class AccServiceImpl implements AccService {

    private Logger loger = LoggerFactory.getLogger(AccServiceImpl.class);

    @Autowired
    private AccDetailDao accDetailDao;
    @Autowired
    private AccInfoDao accDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDetailDao userDetailDao;
    @Autowired
    private AccLimitdefDao accLimitdefDao;
    @Autowired
    private UserFriendshipDao userFriendshipDao;
    
    @Override
    public boolean addAccDetail(AccDetail accDetail, String versionNo) {
        try{            
            accDetailDao.addAccDetail(accDetail);
            return true;
        }catch(Exception e){
            loger.error("addAccDetail fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addAcc(AccInfo acc, String versionNo) {
        try{            
            accDao.addAcc(acc);
            return true;
        }catch(Exception e){
            loger.error("addAcc fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifyAcc(AccInfo acc, String versionNo) {
        try{            
            accDao.modifyAcc(acc);
            return true;
        }catch(Exception e){
            loger.error("modifyAcc fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public AccInfo findAcc(AccInfo acc, String versionNo) {
    	
//        return accDao.findAcc(acc);
    	  return null;
    }

    @Override
    public boolean modifyAccDetail(AccDetail accDetail, String versionNo) {
        try{            
            accDetailDao.updateAccDetail(accDetail);
            return true;
        }catch(Exception e){
            loger.error("modifyAcc fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public List<PageData> findAccDetailPage(Page page, String versionNo) {
        return accDetailDao.findAccDetailPage(page);
    }

    @Override
    public Integer getAvailableBalance(AccInfo acc, String versionNo) {
        return accDao.getAvailableBalance(acc);
    }

    @Override
    public boolean updateIn(AccInfo acc, String versionNo) {
        try{            
            accDao.updateIn(acc);
            return true;
        }catch(Exception e){
            loger.error("modifyAcc fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateOut(AccInfo acc, String versionNo) {
        try{            
            accDao.updateOut(acc);
            return true;
        }catch(Exception e){
            loger.error("modifyAcc fail,reason is "+e.getMessage());
            return false;
        }
    }

    @Override
    public List<PageData> findAccDetailList(PageData pd, String versionNo) {
        return accDetailDao.findAccDetailList(pd);
    }

    @Override
    @Transactional(propagation =Propagation.REQUIRED)
    public Map<String,String> rewardTfcc(JSONArray jsonArray,UserDetail userDetail,BigDecimal avbAmnt,String versionNo) {
        Map<String,String> map = new HashMap<String,String>();
        StringBuffer successStr = new StringBuffer("[");
        StringBuffer failStr = new StringBuffer("发放失败的手机号：");
        for(int i = 0;i<jsonArray.size();i++){
            JSONObject obj = (JSONObject)jsonArray.get(i);
            String phone = obj.getString("phone").trim();
            String ymdNum = obj.getString("ymdNum").trim();
            if(Validator.isMobile(phone)&&Validator.isMoney4(ymdNum)){
                User tUser = userDao.findUserByPhone(phone);
                if(tUser==null){
                    failStr.append(phone+"-会员不存在；");
                    map.put("failStr", failStr.toString());
                    continue;
                }
                PageData pdFriend = new PageData();
                pdFriend.put("user_code", userDetail.getId());
                pdFriend.put("recomuser_code", tUser.getUserCode());
                pdFriend.put("syscode", Constant.CUR_SYS_CODE);
                /*boolean isFriend = userFriendshipDao.isFriend(pdFriend);
                if(!isFriend){
                    failStr.append(phone+"-会员未绑定该机构；");
                    map.put("failStr", failStr.toString());
                    continue;
                }*/
                BigDecimal limit = null;//账户限额
                BigDecimal totalAmnt = null;
                BigDecimal ymdNumTemp = new BigDecimal(ymdNum);
                //如果可用余额小于发放额度
                if(avbAmnt.compareTo(ymdNumTemp)<0){
                    failStr.append(phone+"-账户余额不足；");
                    map.put("failStr", failStr.toString());
                    return map;
                }
                if(tUser!=null&&"1".equals(tUser.getStatus())&&"1".equals(tUser.getUserType())){
                    PageData pd = new PageData();
                    pd.put("cuy_type", "1");
                    pd.put("acc_no", "01");
                    PageData accLimit = accLimitdefDao.getAccLimit(pd);
                    limit = new BigDecimal(accLimit.get("credit_limit").toString());
                    AccInfo acc = new AccInfo();
                    acc.setUserId(tUser.getId());
                    acc.setSyscode(Constant.CUR_SYS_CODE);
                    
                    PageData pd1 = new PageData();
                    pd1.put("userId", tUser.getId());
                    pd1.put("syscode", Constant.CUR_SYS_CODE);
                    acc =  accDao.findAcc(pd1);
                    totalAmnt =acc.getTotalAmnt();
                }
                if(limit == null){
                    failStr = new StringBuffer("发放失败，系统未设置限额，请联系客服！");
                    map.put("failStr", failStr.toString());
                    return map;
                }
                //判断是否超过限额
                if((limit!=null&&totalAmnt!=null&&totalAmnt.add(ymdNumTemp).compareTo(limit)<=0)||(limit != null&&totalAmnt == null&&ymdNumTemp.compareTo(limit)<=0)){
                    //投资机构账户支出
                	AccInfo accOut = new AccInfo();
                    accOut.setUserId(userDetail.getId());
                    accOut.setAvbAmnt(ymdNumTemp);
                    accOut.setTotalAmnt(ymdNumTemp);
                    accOut.setSyscode(Constant.CUR_SYS_CODE);
                    boolean result = accDao.updateOut(accOut);
                    if(result){
                        //普通用户账户明细收入
                        AccDetail accDetail = new AccDetail();
                        accDetail.setUserId(userDetail.getId());//投资机构用户编码
                        accDetail.setRelaUserid(tUser.getId());//关联用户编码 
                        accDetail.setBounsSource("40");//投资机构发放
                        accDetail.setCaldate(DateUtil.getCurrentDate());
                        accDetail.setCntflag("1");
                        accDetail.setSyscode(Constant.CUR_SYS_CODE);
                        accDetail.setCreateTime(DateUtil.getCurrentDate());
                        accDetail.setModifyTime(DateUtil.getCurrentDate());
                        accDetail.setOperator("hjq");
                        accDetail.setRelaUserlevel("");
                        accDetail.setAccNo("010301");
                        accDetail.setAmnt(ymdNumTemp);
                        accDetail.setStatus("1");
                        accDetailDao.addAccDetail(accDetail);
                        
                        //普通用户账户汇总收入
                        AccInfo accIn = new AccInfo();
                        accIn.setUserId(tUser.getId());
                        accIn.setTotalAmnt(ymdNumTemp);
                        accIn.setSyscode(Constant.CUR_SYS_CODE);
                        accDao.updateIn(accIn);
                        
                        //冻结普通用户
                        PageData userDetailPD = new PageData();
                        userDetailPD.put("phone", phone);
                        userDetailPD.put("freeze_flag", "0");//冻结
                        userDetailDao.updateFreezeFlag(userDetailPD);
                        /*AccDetail taccDetail = accDetailDao.findAccDetail(accDetail);
                        if(taccDetail == null){
                            accDetail.setAmnt(ymdNumTemp);
                            accDetail.setStatus("1");
                            accDetailDao.addAccDetail(accDetail);
                        }else{
                            accDetail.setAmnt(ymdNumTemp);
                            accDetailDao.updateAccDetail(accDetail);
                        }*/
                        successStr.append("{phone:'"+phone+"',ymdNum:'"+ymdNum+"'},");
                    }
                }else{
                    if((limit!=null&&totalAmnt!=null&&totalAmnt.add(ymdNumTemp).compareTo(limit)>0)||limit != null&&totalAmnt == null&&ymdNumTemp.compareTo(limit)>0){
                        failStr.append(phone+"-超出限额；");
                    }
                }
            }else{
                if(!Validator.isMobile(phone)){
                    failStr.append(phone+"-手机号格式有误；");
                }
                if(!Validator.isMoney4(ymdNum)){
                    failStr.append(phone+"-发放额度格式有误；");
                }
                
            }
        }
        if(successStr.toString().equals("[")){
            successStr = null;
        }else{
            successStr = new StringBuffer(successStr.substring(0, successStr.length()-1)+"]");
            map.put("successStr", successStr.toString());
        }
        if(failStr.toString().equals("发放失败的手机号：")){
            failStr.append("无");  
        }
        map.put("failStr", failStr.toString());
        return map;
    }

    @Override
    public AccDetail findAccDetail(AccDetail accDetail, String versionNo) {
    	/*AccDetail findAccDetail = accDetailDao.findAccDetail(accDetail);
        return findAccDetail;*/
    	return null;
    }
    
    public static void main(String[] args) {
        BigDecimal limit = new BigDecimal("1200.00");//账户限额
        BigDecimal totalAmnt = new BigDecimal("1100.00");
        BigDecimal ymdNumTemp = new BigDecimal("99.01");
        if(totalAmnt.add(ymdNumTemp).compareTo(limit)==-1){
            System.out.println("测试相等");
        }
    }

}
