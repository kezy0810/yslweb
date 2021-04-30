package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.user.UserFriendship;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface UserFriendshipDao extends DAO<UserFriendship> {

	
	/** 判断推荐人是否含有上级会员
	 * @return findIsHaveUpFriendship  判断推荐人是否含有上级会员
	 * @create author kezhiyi
	 * @create date 2016年8月25日
	 */
	public int  findIsExistUpFriendship(String recomusercode);
	/** 查询推荐人上级会员
	 * @return findUpFriendship  查询推荐人上级会员
	 * @create author kezhiyi
	 * @create date 2016年8月25日
	 */
	public UserFriendship  findUpFriendship(String recomusercode);
	/** 查询推荐人最大级数
	 * @return findMaxFriendship  查询推荐人最大级数
	 * @create author kezhiyi
	 * @create date 2016年9月20日
	 */
	public UserFriendship  findMaxFriendship(String recomuserid);
	
	
	/** 添加用户关系信息
	 * @return addUserFriendship 添加用户关系信息
	 * @create author kezhiyi
	 * @create date 2016年8月25日
	 */ 
	public void addUserFriendship(UserFriendship userFriendship);
	
	/** 添加用户关系信息
	 * @return addUserFriendship 添加用户关系信息
	 * @create author kezhiyi
	 * @create date 2016年8月25日
	 */ 
	public void modifyCalflag(UserFriendship userFriendship);
	
	/**
     * @describe:查询用户关系
     * @author: zhangchunming
     * @date: 2016年9月23日下午6:55:54
     * @param pd
     * @return: boolean
     */
    public boolean  isFriend(PageData pd);
	
}
