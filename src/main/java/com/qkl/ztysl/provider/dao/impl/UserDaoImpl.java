package com.qkl.ztysl.provider.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.DateUtil;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.provider.dao.UserDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;



/**用户的dao的实现
 * <p>Description：户的dao的实现  </p>
 * @project_Name qkl_ymd_web
 * @class_Name UserDaoImpl.java
 * @author kezhiyi
 * @date 2016年8月17日
 * @version v1.0
 */
@Repository
public class UserDaoImpl extends DaoSupport<User> implements UserDao {
	
	protected static final Log logger = LogFactory.getLog(UserDaoImpl.class);
	
	private String namespace = "User";
	

	@Override
	public void addUser(User user) {
		getSqlSession().insert(namespace+"."+"add", user);				
	}


	public void modifyUser(User user) {
		getSqlSession().update(namespace+"."+"update", user);			
	}
 
	@Override
	public User findUserByPhone(String phone) {
		User tUser =getSqlSession().selectOne(namespace+"."+"findUserByPhone", phone);
		return tUser;
	}

	@Override
	public User findUserByUserCode(String userCode) {
	    User tUser =getSqlSession().selectOne(namespace+"."+"findUserByUserCode", userCode);
        return tUser;
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyPwd(String phone, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone",phone);
		map.put("pwdhash",password);
		map.put("modify_time",(Date) DateUtil.getCurrentDate());
		getSqlSession().update(namespace+"."+"updatepwd", map);	
		
	}

	@Override
	public void modifyPhone(String userCode, String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone",phone);
		map.put("usercode",userCode);
		map.put("modify_time",(Date) DateUtil.getCurrentDate());
		getSqlSession().update(namespace+"."+"updatephone", map);	
		
	}

	@Override
	public void modifyLoginLockStaus(String userCode, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifySmsLockStaus(String phone, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone",phone);
		map.put("is_smslocked",status);
		map.put("modify_time",(Date) DateUtil.getCurrentDate());
		if(status.equals("1")){
		map.put("smslock_time", (Date) DateUtil.getCurrentDate());
		}else{
		map.put("smslock_time", null);
		}
		getSqlSession().update(namespace+"."+"updatesms", map);			
	}


	@Override
	public int findUserIsExist(String phone) {
		int eCnt=  getSqlSession().selectOne(namespace+"."+"findIsExist", phone);
		return eCnt;
	}


    @Override
    public void modifypwdByUserCode(String userCode, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userCode",userCode);
        map.put("pwdhash",password);
        getSqlSession().update(namespace+"."+"updatepwdByUserCode", map); 
    }
	
}
