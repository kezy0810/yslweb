package com.qkl.ztysl.provider.dao.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.user.UserFriendship;
import com.qkl.ztysl.provider.dao.UserFriendshipDao;
import com.qkl.ztysl.provider.dbhelper.DaoSupport;




/**UserFriendshipDao的实现
 * <p>Description：UserFriendshipDao的实现  </p>
 * @project_Name qkl_ymd_web
 * @class_Name UserFriendshipDaoImpl.java
 * @author kezhiyi
 * @date 2016年8月17日
 * @version v1.0
 */
@Repository
public class UserFriendshipDaoImpl extends DaoSupport<UserFriendship> implements UserFriendshipDao {

	protected static final Log logger = LogFactory.getLog(UserDaoImpl.class);
	
	private String namespace = "UserFriendship";
	
	
	@Override
	public int findIsExistUpFriendship(String tRecomuserid) {
		Integer recomuserId =new Integer(tRecomuserid); 
		int eCnt=  getSqlSession().selectOne(namespace+"."+"findIsExist", recomuserId);
		return eCnt;
	}

	@Override
	public UserFriendship findUpFriendship(String tRecomuserid) {
		Integer recomuserId =new Integer(tRecomuserid); 
		UserFriendship tUserFriendship =getSqlSession().selectOne(namespace+"."+"findUpFriendshipByRecomuserid", recomuserId);
		return tUserFriendship;
	}

	@Override
	public void addUserFriendship(UserFriendship userFriendship) {
		getSqlSession().insert(namespace+"."+"add", userFriendship);			
	}

	@Override
	public void modifyCalflag(UserFriendship userFriendship) {
		getSqlSession().update(namespace+"."+"updatecalflag", userFriendship);		
	}

	@Override
	public UserFriendship findMaxFriendship(String tRecomuserid) {
		Integer recomuserId =new Integer(tRecomuserid); 
		UserFriendship tUserFriendship =getSqlSession().selectOne(namespace+"."+"findMaxFriendshipByRecomuserid", recomuserId);
		return tUserFriendship;
	}

	@Override
    public boolean isFriend(PageData pd) {
        return getSqlSession().selectOne(namespace+"."+"isFriend", pd)==null?false:true;
    }
	
	
	
}
