package com.qkl.tfcc.provider.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.tfcc.api.po.user.UserDetail;
import com.qkl.tfcc.provider.dao.UserDetailDao;
import com.qkl.tfcc.provider.dbhelper.DaoSupport;
import com.qkl.util.help.DateUtil;



/**用户的UserDetailDao的实现
 * <p>Description：UserDetailDao的实现  </p>
 * @project_Name qkl_tfcc_web
 * @class_Name UserDetailDaoImpl.java
 * @author kezhiyi
 * @date 2016年8月17日
 * @version v1.0
 */
@Repository
public class UserDetailDaoImpl extends DaoSupport<UserDetail> implements UserDetailDao {

protected static final Log logger = LogFactory.getLog(UserDaoImpl.class);
	
	private String namespace = "UserDetail";
	
	
	@Override
	public void addUserDetail(UserDetail userDetail) {
		getSqlSession().insert(namespace+"."+"add", userDetail);	
		
	}

	@Override
	public void modifyUserDetail(UserDetail userDetail) {
		getSqlSession().update(namespace+"."+"updateUserDetail", userDetail);	
		
	}

	@Override
	public UserDetail findUserDetailByPhone(String phone) {
		UserDetail tUserDetail =getSqlSession().selectOne(namespace+"."+"findByPhone", phone);
		return tUserDetail;
	}

	@Override
	public UserDetail findUserDetailByUserCode(String userCode) {
		return getSqlSession().selectOne(namespace+"."+"findByUserCode", userCode);
	}

	@Override
	public UserDetail findUserDetailById(int id) {
		return getSqlSession().selectOne(namespace+"."+"findById", id);
	}

	@Override
	public void modifyRealStatus(String phone, String realName,
			String idNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone",phone);
		map.put("realname",realName);
		map.put("idno",idNo);
		map.put("realstat","1");
		map.put("modify_time",(Date) DateUtil.getCurrentDate());	
		getSqlSession().update(namespace+"."+"updatereal", map);	
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
	public int findUserDetailIsExist(String phone) {
		int eCnt=  getSqlSession().selectOne(namespace+"."+"findIsExist", phone);
		return eCnt;
	}

    @Override
    public void modifyUserHeadPic(String userCode,String imgAddrss) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_code",userCode);
        map.put("img_addrss",imgAddrss);
        getSqlSession().update(namespace+"."+"updateHeadPic",map);   
    }

}
