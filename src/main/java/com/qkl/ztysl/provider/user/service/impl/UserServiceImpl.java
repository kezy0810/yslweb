package com.qkl.ztysl.provider.user.service.impl;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.util.help.DateUtil;
import com.qkl.util.help.FileUtil;
import com.qkl.util.help.MD5Util;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.common.Constant;
import com.qkl.ztysl.api.po.acc.AccDetail;
import com.qkl.ztysl.api.po.acc.AccInfo;
import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.po.user.UserFriendship;
import com.qkl.ztysl.api.po.user.UserLevelcnt;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.api.service.user.api.UserService;
import com.qkl.ztysl.provider.dao.AccDetailDao;
import com.qkl.ztysl.provider.dao.AccInfoDao;
import com.qkl.ztysl.provider.dao.SysGencodeDao;
import com.qkl.ztysl.provider.dao.UserDao;
import com.qkl.ztysl.provider.dao.UserDetailDao;
import com.qkl.ztysl.provider.dao.UserFriendshipDao;
import com.qkl.ztysl.provider.dao.UserLevelcntDao;
import com.qkl.ztysl.provider.dao.UserLoginErrDao;

@Service
public class UserServiceImpl implements UserService {

	private Logger loger = LoggerFactory.getLogger(UserServiceImpl.class);
	private  List<SysGencode> tSysGencodeAll = new  ArrayList<SysGencode>();
	
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserDetailDao userDetailDao;	
	@Autowired
	private UserFriendshipDao userFriendshipDao;

	@Autowired
    private AccService accService;
	@Autowired
    private AccInfoDao accInfoDao;
	@Autowired
    private AccDetailDao accDetailDao;
	@Autowired
	private SysGencodeDao sysGencodeDao;
	@Autowired
	private UserLevelcntDao userLevelcntDao;
	
	
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public Map<String, Object> login(String username, String password,
			String systemCode,String versionNo) {
		Map<String, Object> result = new HashMap<String, Object>();
//		User user = userDetailDao.findUserByPhone(username);
		UserDetail userdetail =userDetailDao.findUserDetailByUserName(username);
		if (null != userdetail) {
			if (userdetail.getTransPassword().equals(MD5Util.getMd5Code(password))) {
				if (userdetail.getStatus().equals(Constant.USING)) {					
						result.put("status", Constant.SUCCESS);
						result.put(Constant.LOGIN_USER, userdetail);						
						result.put("msg", "ok");
					
				} else {
					result.put("status", Constant.FAIL);
					result.put("msg", "该用户已被禁用");
				}
			} else {
				result.put("status", Constant.FAIL);
				result.put("msg", "密码错误");
			}
		} else {
			result.put("status", Constant.FAIL);
			result.put("msg", "该用户不存在");
		}
		return result;
	
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addUser(UserDetail userDetail,String versionNo) {
			userDetailDao.addUserDetail(userDetail);
//			tSysGencodeAll =sysGencodeDao.findAll();
			
			UserDetail muserDetail = userDetailDao.findUserDetailByUserName(userDetail.getUserName());
			
	         //添加用户关系
          createUserFriendship(muserDetail);
            
            //计算注册奖励
//          calRegitAccDetail(muserDetail);
          AccInfo tAccInfo = new AccInfo();
          tAccInfo.setUserId(muserDetail.getId());  
          tAccInfo.setTotalAmnt(new BigDecimal(0));
          tAccInfo.setAvbAmnt(new BigDecimal(0));
          tAccInfo.setHandleAmnt(new BigDecimal(0));
          tAccInfo.setSyscode(Constant.CUR_SYS_CODE);
          tAccInfo.setCreateTime(DateUtil.getCurrentDate());
          tAccInfo.setModifyTime(DateUtil.getCurrentDate());
          tAccInfo.setOperator("sys");
          accInfoDao.addAcc(tAccInfo);
		  return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean addUserFriendShip(UserFriendship userFriendship,String versionNo) {
		userFriendshipDao.addUserFriendship(userFriendship);			
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean realUser(String phone, String realName, String idNo,String versionNo) {
		userDetailDao.modifyRealStatus(phone, realName, idNo);			
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyPwd(String phone, String password,String versionNo) {
		userDao.modifyPwd(phone, password);		
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifypwdById(Integer userId, String password, String versionNo) {
		userDetailDao.modifyPwd(userId, password);		
		return true;
	}
	
	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyPhone(String userCode, String phone,String oldPhone,String versionNo) {
	    userDao.modifyPhone(userCode, phone);	
		userDetailDao.modifyPhone(userCode, phone);
		PageData pd = new PageData();
		pd.put("ref_phone", oldPhone);
		pd.put("new_ref_phone", phone);
		userDetailDao.updateRefPhone(pd);
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyUserDetail(UserDetail userDetail,String versionNo) {
			userDetailDao.modifyUserDetail(userDetail);
//			userDao.modifyPhone(userDetail.getUserCode(), userDetail.getPhone());
			return true;
	}


	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyLockLoginStatus(String phone, String status,Date locktime,String versionNo) {
		userDao.modifyLoginLockStaus(phone, status);
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public boolean modifyLockSmsStatus(String phone, String status,Date locktime,String versionNo) {
		userDao.modifySmsLockStaus(phone, status);
		return true;
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public Map<String, Object> findLockLoginStatus(String phone,long second, String versionNo) {
		return null;
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public Map<String, Object> findLockSmsStatus(String phone,long second, String versionNo) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByPhone(phone);
		if (null != user) {
			result.put("is_smslocked", user.getIsSmslocked());
			result.put("smslock_time", user.getSmslockTime());
		
		} else {
			result.put("is_smslocked", null);
			result.put("smslock_time", null);
		}
		return result;
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public User findbyPhone(String phone, String versionNo) {
		User fUser = userDao.findUserByPhone(phone);
		return fUser;
	}
	
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public UserDetail findbyRefcode(String refcode, String versionNo) {
		UserDetail fUserDetail = userDetailDao.findDetailByRefcode(refcode);
		return fUserDetail;
	}
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public UserDetail findbyUserName(String userName, String versionNo) {
		UserDetail fUserDetail = userDetailDao.findUserDetailByUserName(userName);
		return fUserDetail;
	}
	
	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public UserDetail findbyId(Integer mUserId, String versionNo) {
		UserDetail fUserDetail = userDetailDao.findUserDetailById(mUserId);
		return fUserDetail;
	}


	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public boolean findIsExist(String userName, String versionNo) {
		 int eCnt = userDetailDao.findUserDetailIsExist(userName);
		 if(eCnt>0){
			 return true;
		 }		
		return false;
	}

	@Override
	@Transactional(propagation =Propagation.SUPPORTS)
	public boolean findIsExistUpFriendship(String recomusercode, String versionNo) {
		 int eCnt = userFriendshipDao.findIsExistUpFriendship(recomusercode);
		 if(eCnt>0){
			 return true;
		 }		
		return false;
	}

	@Override
	@Transactional(propagation =Propagation.REQUIRED)
	public UserFriendship findUpFriendship(String tRecomuserid, String versionNo) {
		UserFriendship fUserFriendship = userFriendshipDao.findUpFriendship(tRecomuserid);
		return fUserFriendship;
	}
	
	 @Override
		@Transactional(propagation =Propagation.REQUIRED)
		public UserFriendship findMaxFriendship(String tRecomuserid,
				String versionNo) {
	    	UserFriendship fUserFriendship = userFriendshipDao.findMaxFriendship(tRecomuserid);
			return fUserFriendship;
		}
	
    @Override
    public boolean modifyUserHeadPic(String userCode, String imgAddrss, String versionNo) {
        userDetailDao.modifyUserHeadPic(userCode, imgAddrss);
        return true;
    }

    @Override
    public UserDetail findUserDetailByUserCode(String userCode, String versionNo) {
        return userDetailDao.findUserDetailByUserCode(userCode);
    }

    @Override
    public User findUserByUserCode(String userCode) {
        return userDao.findUserByUserCode(userCode);
    }

    @Override
    public boolean modifypwdByUserCode(String userCode, String password, String versionNo) {
        userDao.modifypwdByUserCode(userCode, password);
        return true;
    }
    
    /**
     * 创建用户关系
     * */
    private void   createUserFriendship(UserDetail mUserDetail){
        String parentRefcode = mUserDetail.getParentRefcode(); 
        Integer UserId = mUserDetail.getId();
       
        
        UserDetail tRefUserDetail =  findbyRefcode(parentRefcode, Constant.VERSION_NO);

        String userType = tRefUserDetail.getUserType();
        String sefLev="1";     
     
         if("1".equals(userType)){
        	
        	 UserFriendship fMaxFriendship =  findMaxFriendship(tRefUserDetail.getId().toString(), Constant.VERSION_NO);//推荐人上级关系最大级数 
        	 if(fMaxFriendship!=null) {
        		 if(fMaxFriendship.getRelaLevel()!=null&&!fMaxFriendship.getRelaLevel().equals("")&&fMaxFriendship.getRelaLevel().equals("A")){
                     sefLev="2";
                 }else if(fMaxFriendship.getRelaLevel()!=null&&!fMaxFriendship.getRelaLevel().equals("")&&fMaxFriendship.getRelaLevel().equals("B")){
                     sefLev="3";
                 }
//                 else if(fMaxFriendship.getRelaLevel()!=null&&!fMaxFriendship.getRelaLevel().equals("")&&fMaxFriendship.getRelaLevel().equals("C")){
//                     sefLev="4";
//                 }
        	 }
             
             UserFriendship tUserFriendship = new UserFriendship();        
             tUserFriendship.setUserId(tRefUserDetail.getId());
             tUserFriendship.setRecomuserId(UserId);    
             tUserFriendship.setRelaLevel("A");
             tUserFriendship.setUserType(userType);
             tUserFriendship.setCalflag("0");
             tUserFriendship.setSyscode(Constant.CUR_SYS_CODE);
             tUserFriendship.setCreateTime(DateUtil.getCurrentDate());
             tUserFriendship.setModifyTime(DateUtil.getCurrentDate());
             userFriendshipDao.addUserFriendship(tUserFriendship);
             /*if(!addUserFriendShip(tUserFriendship, Constant.VERSION_NO)){
                 loger.info("addUserFriendShip fail,tUserFriendship is "+tUserFriendship.toString());
             };*/
//             //用户关系计算器            
//             PageData pd1=new PageData();
//             pd1.put("userId", tRefUserDetail.getId());
//             pd1.put("syscode", Constant.CUR_SYS_CODE);
//             pd1.put("relaLevel","A" );
//             UserLevelcnt tUserLevelcnt= userLevelcntDao.findUserLevelcnt(pd1);            
//             if(tUserLevelcnt==null){
//            	 UserLevelcnt tUserLevelcntA = new UserLevelcnt();
//            	 tUserLevelcntA.setUserCode(tRefUser.getUserCode());
//            	 tUserLevelcntA.setRelaLevel("A");
//            	 tUserLevelcntA.setLevcnt(1);
//            	 tUserLevelcntA.setSyscode(Constant.CUR_SYS_CODE);
//            	 tUserLevelcntA.setCreateTime(DateUtil.getCurrentDate());
//            	 tUserLevelcntA.setModifyTime(DateUtil.getCurrentDate());
//            	 tUserLevelcntA.setOperator("sys");
//            	 userLevelcntDao.addUserLevelcnt(tUserLevelcntA);
//             }else{
//            	 UserLevelcnt tUserLevelcntA = new UserLevelcnt();
//            	 tUserLevelcntA.setUserCode(tRefUser.getUserCode());
//            	 tUserLevelcntA.setRelaLevel("A");            	 
//            	 tUserLevelcntA.setSyscode(Constant.CUR_SYS_CODE);
//            	 tUserLevelcntA.setModifyTime(DateUtil.getCurrentDate());
//            	 tUserLevelcntA.setOperator("sys");
//            	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntA);
//             }
           //普通会员        	 
             if(sefLev.equals("2")){//总共有2层             
                 UserFriendship  fUserFriendship2 =  findUpFriendship(tRefUserDetail.getId().toString(), Constant.VERSION_NO);                  
                 UserFriendship tUserFriendship2 = new UserFriendship();
                 tUserFriendship2.setUserId(fUserFriendship2.getUserId());
                 tUserFriendship2.setRecomuserId(UserId);
                 tUserFriendship2.setRelaLevel("B");//只要有
                 tUserFriendship2.setUserType(userType);
                 tUserFriendship2.setCalflag("0");
                 tUserFriendship2.setSyscode(Constant.CUR_SYS_CODE);
                 tUserFriendship2.setCreateTime(DateUtil.getCurrentDate());
                 tUserFriendship2.setModifyTime(DateUtil.getCurrentDate());
                 userFriendshipDao.addUserFriendship(tUserFriendship2);
                 
                /* if(!addUserFriendShip(tUserFriendship2, Constant.VERSION_NO)){
                     loger.info("addUserFriendShip fail,tUserFriendship2 is "+tUserFriendship2.toString());
                 }; */
               //用户关系计算器            
//                 PageData pd2=new PageData();
//                 pd2.put("userCode", fUserFriendship2.getUserCode());
//                 pd2.put("syscode", Constant.CUR_SYS_CODE);
//                 pd2.put("relaLevel","B" );
//                 UserLevelcnt tUserLevelcnt2= userLevelcntDao.findUserLevelcnt(pd2);            
//                 if(tUserLevelcnt2==null){
//                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
//                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
//                	 tUserLevelcntB.setRelaLevel("B");
//                	 tUserLevelcntB.setLevcnt(1);
//                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntB.setCreateTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setOperator("sys");
//                	 userLevelcntDao.addUserLevelcnt(tUserLevelcntB);
//                 }else{
//                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
//                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
//                	 tUserLevelcntB.setRelaLevel("B");            	 
//                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setOperator("sys");
//                	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntB);
//                 }                
             }
             if(sefLev.equals("3")){//总共有3层
            	 UserFriendship  fUserFriendship2 =  findUpFriendship(tRefUserDetail.getId().toString(), Constant.VERSION_NO);                  
                 UserFriendship tUserFriendship2 = new UserFriendship();
                 tUserFriendship2.setUserId(fUserFriendship2.getUserId());
                 tUserFriendship2.setRecomuserId(UserId);
                 tUserFriendship2.setRelaLevel("B");//只要有
                 tUserFriendship2.setUserType(userType);
                 tUserFriendship2.setCalflag("0");
                 tUserFriendship2.setSyscode(Constant.CUR_SYS_CODE);
                 tUserFriendship2.setCreateTime(DateUtil.getCurrentDate());
                 tUserFriendship2.setModifyTime(DateUtil.getCurrentDate());
                 userFriendshipDao.addUserFriendship(tUserFriendship2);
                 /*if(!addUserFriendShip(tUserFriendship2, Constant.VERSION_NO)){
                     loger.info("addUserFriendShip fail,tUserFriendship2 is "+tUserFriendship2.toString());
                 };*/ 
               //用户关系计算器            
//                 PageData pd2=new PageData();
//                 pd2.put("userCode", fUserFriendship2.getUserCode());
//                 pd2.put("syscode", Constant.CUR_SYS_CODE);
//                 pd2.put("relaLevel","B" );
//                 UserLevelcnt tUserLevelcnt2= userLevelcntDao.findUserLevelcnt(pd2);            
//                 if(tUserLevelcnt2==null){
//                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
//                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
//                	 tUserLevelcntB.setRelaLevel("B");
//                	 tUserLevelcntB.setLevcnt(1);
//                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntB.setCreateTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setOperator("sys");
//                	 userLevelcntDao.addUserLevelcnt(tUserLevelcntB);
//                 }else{
//                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
//                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
//                	 tUserLevelcntB.setRelaLevel("B");            	 
//                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntB.setOperator("sys");
//                	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntB);
//                 } 
            	 
            	 
            	 
//                 UserFriendship tUserFriendship3 = new UserFriendship();
//                 tUserFriendship3.setUserId(fMaxFriendship.getUserId());
//                 tUserFriendship3.setRecomuserId(UserId);
//                 tUserFriendship3.setRelaLevel("C");//只要有
//                 tUserFriendship3.setUserType(userType);
//                 tUserFriendship3.setSyscode(Constant.CUR_SYS_CODE);
//                 tUserFriendship3.setCalflag("0");
//                 tUserFriendship3.setCreateTime(DateUtil.getCurrentDate());
//                 tUserFriendship3.setModifyTime(DateUtil.getCurrentDate());
//                 userFriendshipDao.addUserFriendship(tUserFriendship3);                 
                /* if(!addUserFriendShip(tUserFriendship3, Constant.VERSION_NO)){
                     loger.info("addUserFriendShip fail,tUserFriendship3 is "+tUserFriendship3.toString());
                 };*/
               //用户关系计算器            
//                 PageData pd3=new PageData();
//                 pd3.put("userCode", fMaxFriendship.getUserCode());
//                 pd3.put("syscode", Constant.CUR_SYS_CODE);
//                 pd3.put("relaLevel","C" );
//                 UserLevelcnt tUserLevelcnt3= userLevelcntDao.findUserLevelcnt(pd3);            
//                 if(tUserLevelcnt3==null){
//                	 UserLevelcnt tUserLevelcntC = new UserLevelcnt();
//                	 tUserLevelcntC.setUserCode(fMaxFriendship.getUserCode());
//                	 tUserLevelcntC.setRelaLevel("C");
//                	 tUserLevelcntC.setLevcnt(1);
//                	 tUserLevelcntC.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntC.setCreateTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntC.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntC.setOperator("sys");
//                	 userLevelcntDao.addUserLevelcnt(tUserLevelcntC);
//                 }else{
//                	 UserLevelcnt tUserLevelcntC = new UserLevelcnt();
//                	 tUserLevelcntC.setUserCode(fMaxFriendship.getUserCode());
//                	 tUserLevelcntC.setRelaLevel("C");            	 
//                	 tUserLevelcntC.setSyscode(Constant.CUR_SYS_CODE);
//                	 tUserLevelcntC.setModifyTime(DateUtil.getCurrentDate());
//                	 tUserLevelcntC.setOperator("sys");
//                	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntC);
//                 }
                 
             }
//             if(sefLev.equals("4")){//推荐人自己最大是C级，注册人需要向上查找2级别，注册人总共有3层
//            	 UserFriendship  fUserFriendship2 =  findUpFriendship(tRefUserDetail.getId().toString(), Constant.VERSION_NO);                  
//                 UserFriendship tUserFriendship2 = new UserFriendship();
//                 tUserFriendship2.setUserId(fUserFriendship2.getUserId());
//                 tUserFriendship2.setRecomuserId(UserId);
//                 tUserFriendship2.setRelaLevel("B");//只要有
//                 tUserFriendship2.setUserType(userType);
//                 tUserFriendship2.setCalflag("0");
//                 tUserFriendship2.setSyscode(Constant.CUR_SYS_CODE);
//                 tUserFriendship2.setCreateTime(DateUtil.getCurrentDate());
//                 tUserFriendship2.setModifyTime(DateUtil.getCurrentDate());
//                 userFriendshipDao.addUserFriendship(tUserFriendship2);
//                 /*if(!addUserFriendShip(tUserFriendship2, Constant.VERSION_NO)){
//                     loger.info("addUserFriendShip fail,tUserFriendship2 is "+tUserFriendship2.toString());
//                 };*/ 
//               //用户关系计算器            
////                 PageData pd2=new PageData();
////                 pd2.put("userCode", fUserFriendship2.getUserCode());
////                 pd2.put("syscode", Constant.CUR_SYS_CODE);
////                 pd2.put("relaLevel","B" );
////                 UserLevelcnt tUserLevelcnt2= userLevelcntDao.findUserLevelcnt(pd2);            
////                 if(tUserLevelcnt2==null){
////                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
////                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
////                	 tUserLevelcntB.setRelaLevel("B");
////                	 tUserLevelcntB.setLevcnt(1);
////                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
////                	 tUserLevelcntB.setCreateTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntB.setOperator("sys");
////                	 userLevelcntDao.addUserLevelcnt(tUserLevelcntB);
////                 }else{
////                	 UserLevelcnt tUserLevelcntB = new UserLevelcnt();
////                	 tUserLevelcntB.setUserCode(fUserFriendship2.getUserCode());
////                	 tUserLevelcntB.setRelaLevel("B");            	 
////                	 tUserLevelcntB.setSyscode(Constant.CUR_SYS_CODE);
////                	 tUserLevelcntB.setModifyTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntB.setOperator("sys");
////                	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntB);
////                 } 
//            	 
//            	 
//                 UserFriendship  fUserFriendship3 =  findUpFriendship(fUserFriendship2.getUserId().toString(), Constant.VERSION_NO);  
//                 UserFriendship tUserFriendship3 = new UserFriendship();
//                 tUserFriendship3.setUserId(fUserFriendship3.getUserId());
//                 tUserFriendship3.setRecomuserId(UserId);
//                 tUserFriendship3.setRelaLevel("C");//只要有
//                 tUserFriendship3.setUserType(userType);
//                 tUserFriendship3.setSyscode(Constant.CUR_SYS_CODE);
//                 tUserFriendship3.setCalflag("0");
//                 tUserFriendship3.setCreateTime(DateUtil.getCurrentDate());
//                 tUserFriendship3.setModifyTime(DateUtil.getCurrentDate());
//                 userFriendshipDao.addUserFriendship(tUserFriendship3);                 
//                /* if(!addUserFriendShip(tUserFriendship3, Constant.VERSION_NO)){
//                     loger.info("addUserFriendShip fail,tUserFriendship3 is "+tUserFriendship3.toString());
//                 };*/
//               //用户关系计算器            
////                 PageData pd3=new PageData();
////                 pd3.put("userCode", fUserFriendship3.getUserCode());
////                 pd3.put("syscode", Constant.CUR_SYS_CODE);
////                 pd3.put("relaLevel","C" );
////                 UserLevelcnt tUserLevelcnt3= userLevelcntDao.findUserLevelcnt(pd3);            
////                 if(tUserLevelcnt3==null){
////                	 UserLevelcnt tUserLevelcntC = new UserLevelcnt();
////                	 tUserLevelcntC.setUserCode(fUserFriendship3.getUserCode());
////                	 tUserLevelcntC.setRelaLevel("C");
////                	 tUserLevelcntC.setLevcnt(1);
////                	 tUserLevelcntC.setSyscode(Constant.CUR_SYS_CODE);
////                	 tUserLevelcntC.setCreateTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntC.setModifyTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntC.setOperator("sys");
////                	 userLevelcntDao.addUserLevelcnt(tUserLevelcntC);
////                 }else{
////                	 UserLevelcnt tUserLevelcntC = new UserLevelcnt();
////                	 tUserLevelcntC.setUserCode(fUserFriendship3.getUserCode());
////                	 tUserLevelcntC.setRelaLevel("C");            	 
////                	 tUserLevelcntC.setSyscode(Constant.CUR_SYS_CODE);
////                	 tUserLevelcntC.setModifyTime(DateUtil.getCurrentDate());
////                	 tUserLevelcntC.setOperator("sys");
////                	 userLevelcntDao.modifyUserLevelcnt(tUserLevelcntC);
////                 }
//                 
//             }
         }

        
    }
    /**
     * 计算奖励关系
     * 1-普通会员
       2-网点会员
       3-LP会员
       4-投资公司
       5-众筹会员
     * */
    private void   calRegitAccDetail(UserDetail mUserDetail){
        /*String refPhone =mUserDetail.getRefPhone(); 
        String UserCode = mUserDetail.getUserCode();*/
        String userType = mUserDetail.getUserType();
               
        if(userType.equals("1")){//普通用户
            //普通会员注册奖励计算
           /* if(mUserDetail.getRealStat().equals("1")){
                if(!calPtMemberRegitBouns(mUserDetail)){
                    loger.error("calPtMemberRegitBouns fail ! ");
                };              
            }*/
           //普通会员推荐奖励计算
//            calPtMemberRecmBouns(mUserDetail);
            calPtMemberTjBouns(mUserDetail);
            
        }else if(userType.equals("2")){//网点用户，审核通过后再计算奖励
            
            
            
        }else if(userType.equals("3")){
            
        }else if(userType.equals("4")){
            
        }
        
    }
    
    private void  calPtMemberTjBouns(UserDetail mUserDetail){ 
        /*SysGencode tSysGencodeTJZC1 = new   SysGencode();
        SysGencode tSysGencodeTJZC2 = new   SysGencode();
        SysGencode tSysGencodeTJZC3 = new   SysGencode();
        SysGencode tSysGencodeNEDLEVA = new   SysGencode();
        SysGencode tSysGencodeNEDLEVB = new   SysGencode();
        SysGencode tSysGencodeNEDLEVC = new   SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("REWARD_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeName().equals("TJZC1")){
                    	tSysGencodeTJZC1= tSysGencode;
                    }else if(tSysGencode.getCodeName().equals("TJZC2")){
                    	tSysGencodeTJZC2= tSysGencode;
                    }else if(tSysGencode.getCodeName().equals("TJZC3")){
                    	tSysGencodeTJZC3= tSysGencode;
                    }else if(tSysGencode.getCodeName().equals("NEDLEVA")){
                    	tSysGencodeNEDLEVA= tSysGencode;
                    }else if(tSysGencode.getCodeName().equals("NEDLEVB")){
                    	tSysGencodeNEDLEVB= tSysGencode;
                    }else if(tSysGencode.getCodeName().equals("NEDLEVC")){
                    	tSysGencodeNEDLEVC= tSysGencode;
                    }                   
                }
            }
        }*/
       
        PageData pd = new PageData();
        pd.put("recomuserId", mUserDetail.getId());
        /*pd.put("TJZC1", tSysGencodeTJZC1.getCodeValue());
        pd.put("TJZC2", tSysGencodeTJZC2.getCodeValue());
        pd.put("TJZC3", tSysGencodeTJZC3.getCodeValue());
        pd.put("NEDLEVA", tSysGencodeNEDLEVA.getCodeValue());
        pd.put("NEDLEVB", tSysGencodeNEDLEVB.getCodeValue());
        pd.put("NEDLEVC", tSysGencodeNEDLEVC.getCodeValue());*/
        pd.put("subAccno", "010101");
        pd.put("bounsSource1", "15");
        pd.put("bounsSource2", "1501");
        pd.put("syscode", Constant.CUR_SYS_CODE);
        pd.put("operator","sys");
    	accDetailDao.addAccDetaillv(pd);
    }
    
    
    
    /**
     * 计算普通会员注册奖励关系
     * 
     * */
    private boolean   calPtMemberRegitBouns(UserDetail mUserDetail){                
        SysGencode tSysGencodeBNDMZC5 = new SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeValue().equals("DMZC")){
                        tSysGencodeBNDMZC5= tSysGencode;
                        break;
                    }                   
                }
            }
        }       
        //根据tb_acc_def的定义，设置子账号
        AccDetail tAccDetail = new AccDetail();
        tAccDetail.setUserId(mUserDetail.getId());
        tAccDetail.setAccNo("010101");
        tAccDetail.setBounsSource("10");
        tAccDetail.setRelaUserid(0);
        tAccDetail.setRelaUserlevel("");
        String rsAmnt="0";
        if(tSysGencodeBNDMZC5.getCodeValue()==null||
                tSysGencodeBNDMZC5.getCodeValue().equals("")||
                tSysGencodeBNDMZC5.getCodeValue().equals("null"))
        {
            loger.info("calPtMemberRegitBouns fail ,tSysGencodeBNDMZC5 is null !");
        }else{
            rsAmnt = tSysGencodeBNDMZC5.getCodeValue();
            
        }
        BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
        bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
        tAccDetail.setAmnt(bdAmnt); 
        tAccDetail.setCaldate(DateUtil.getCurrentDate());
        tAccDetail.setStatus("1");
        tAccDetail.setCreateTime(DateUtil.getCurrentDate());
        tAccDetail.setModifyTime(DateUtil.getCurrentDate());
        
        return accService.addAccDetail(tAccDetail,Constant.VERSION_NO);
        
    }
    /**
     * 计算普通会员注册奖励关系
     * 
     * */
    private void  calPtMemberRecmBouns(UserDetail mUserDetail){ 
        SysGencode tSysGencodeBNTJDM1 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM2 = new   SysGencode();
        SysGencode tSysGencodeBNTJDM3 = new   SysGencode();
        if(tSysGencodeAll!=null&&tSysGencodeAll.size()>0){
            for(SysGencode tSysGencode:tSysGencodeAll){
                if(tSysGencode.getGroupCode().equals("BOUND_DEF")){//获取奖金定义
                    if(tSysGencode.getCodeValue().equals("TJDM1")){
                        tSysGencodeBNTJDM1= tSysGencode;
                    }else if(tSysGencode.getCodeValue().equals("TJDM2")){
                        tSysGencodeBNTJDM2= tSysGencode;
                    }else if(tSysGencode.getCodeValue().equals("TJDM3")){
                        tSysGencodeBNTJDM3= tSysGencode;
                    }                   
                }
            }
        }
        
        String refPhone=  "";
        String UserCode = "";
//        String refPhone = mUserDetail.getRefPhone(); 
//        String UserCode = mUserDetail.getUserCode();
    
        String userType = mUserDetail.getUserType();
        
        User tRefUser =  findbyPhone(refPhone, Constant.VERSION_NO);
                
         boolean tUpflag = findIsExistUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
         UserFriendship fUserFriendship= new UserFriendship();//推荐人上级关系      

         String sefLev="";
         if(!tUpflag){//如果推荐人没有上级会员，则自己为A级会员

             sefLev="1";
         }
         if(tUpflag){
             fUserFriendship =  findUpFriendship(tRefUser.getUserCode(), Constant.VERSION_NO);
             if(fUserFriendship.getRelaLevel().equals("A")){
                 sefLev="2";
             }else if(fUserFriendship.getRelaLevel().equals("B")){
                 sefLev="3";
             }else if(fUserFriendship.getRelaLevel().equals("C")){
                 sefLev="4";
             }else{
                 sefLev="4";                    
             }
         }
         
         //推荐奖励 A级别会员
         AccDetail tAccDetail = new AccDetail();
//         tAccDetail.setUserCode(tRefUser.getUserCode());        
//         tAccDetail.setSubAccno("010101");
//         tAccDetail.setBounsSource1("11");//普通用户推荐会员PDM      
//         tAccDetail.setBounsSource2("1101");//普通用户推荐A级会员
//         tAccDetail.setRelaUsercode(UserCode);
         tAccDetail.setRelaUserlevel("A");
        String rsAmnt="0";
        if(tSysGencodeBNTJDM1.getCodeValue()==null||
                tSysGencodeBNTJDM1.getCodeValue().equals("")||
                tSysGencodeBNTJDM1.getCodeValue().equals("null"))
        {
            loger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM1 is null !");
        }else{
            rsAmnt = tSysGencodeBNTJDM1.getCodeValue();
            
        }
        BigDecimal bdAmnt=new BigDecimal(rsAmnt);  
        bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
        tAccDetail.setAmnt(bdAmnt); 
         tAccDetail.setCaldate(DateUtil.getCurrentDate());
         tAccDetail.setCntflag("0");//未纳入计算
         tAccDetail.setStatus("1");
         tAccDetail.setCreateTime(DateUtil.getCurrentDate());
         tAccDetail.setModifyTime(DateUtil.getCurrentDate());
         tAccDetail.setOperator("sys");
         if(! accService.addAccDetail(tAccDetail,Constant.VERSION_NO)){
             loger.info("addAccDetail fail,tAccDetail is "+tAccDetail.toString());
         }; 
         
         
                                         
         if(sefLev.equals("2")){//总共有2层             
             AccDetail tAccDetail2 = new AccDetail();
//             tAccDetail2.setUserCode(fUserFriendship.getUserCode());        
//             tAccDetail2.setSubAccno("010101");
//             tAccDetail2.setBounsSource1("11");//普通用户推荐会员参考PDM       
//             tAccDetail2.setBounsSource2("1102");//普通用户推荐B级会员
//             tAccDetail2.setRelaUsercode(UserCode);
             tAccDetail2.setRelaUserlevel("B");
            rsAmnt="0";
            if(tSysGencodeBNTJDM2.getCodeValue()==null||
                    tSysGencodeBNTJDM2.getCodeValue().equals("")||
                    tSysGencodeBNTJDM2.getCodeValue().equals("null"))
            {
                loger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM2 is null !");
            }else{
                rsAmnt = tSysGencodeBNTJDM2.getCodeValue();
                
            }
            bdAmnt=new BigDecimal(rsAmnt);  
            bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
            tAccDetail2.setAmnt(bdAmnt);    
             tAccDetail2.setCaldate(DateUtil.getCurrentDate());
             tAccDetail2.setCntflag("0");
             tAccDetail2.setStatus("1");
             tAccDetail2.setCreateTime(DateUtil.getCurrentDate());
             tAccDetail2.setModifyTime(DateUtil.getCurrentDate());
             tAccDetail2.setOperator("sys");
             if(! accService.addAccDetail(tAccDetail2,Constant.VERSION_NO)){
                 loger.info("addAccDetail fail,tAccDetail2 is "+tAccDetail2.toString());
             };                  
         }
         if(sefLev.equals("3")){//总共有3层
             UserFriendship  fUserFriendship3 =  findUpFriendship(fUserFriendship.getUserId().toString(), Constant.VERSION_NO); 
             AccDetail tAccDetail3 = new AccDetail();
//             tAccDetail3.setUserCode(fUserFriendship3.getUserCode());       
//             tAccDetail3.setSubAccno("010101");
//             tAccDetail3.setBounsSource1("11");//普通用户推荐会员参考PDM       
//             tAccDetail3.setBounsSource2("1103");//普通用户推荐B级会员
//             tAccDetail3.setRelaUsercode(UserCode);
             tAccDetail3.setRelaUserlevel("C");
            rsAmnt="0";
            if(tSysGencodeBNTJDM3.getCodeValue()==null||
                    tSysGencodeBNTJDM3.getCodeValue().equals("")||
                    tSysGencodeBNTJDM3.getCodeValue().equals("null"))
            {
                loger.info("calPtMemberRecmBouns fail ,tSysGencodeBNTJDM3 is null !");
            }else{
                rsAmnt = tSysGencodeBNTJDM3.getCodeValue();
                
            }
            bdAmnt=new BigDecimal(rsAmnt);  
            bdAmnt.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
            tAccDetail3.setAmnt(bdAmnt);    
             tAccDetail3.setCaldate(DateUtil.getCurrentDate());
             tAccDetail3.setCntflag("0");
             tAccDetail3.setStatus("1");
             tAccDetail3.setCreateTime(DateUtil.getCurrentDate());
             tAccDetail3.setModifyTime(DateUtil.getCurrentDate());
             tAccDetail3.setOperator("sys");
             if(! accService.addAccDetail(tAccDetail3,Constant.VERSION_NO)){
                 loger.info("addAccDetail fail,tAccDetail3 is "+tAccDetail3.toString());
             }; 
        
         }
    
    }


    @Override
    public boolean thaw(String ratio, String versionNo) {
        return accInfoDao.thaw(ratio);
    }

	


	
}
