package com.qkl.ztysl.provider.dao;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface UserDetailDao extends DAO<UserDetail> {

	
	
	/** 添加用户明细信息
	 * @return addUserDetail 用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void addUserDetail(UserDetail userDetail);
	
	/** 修改用户明细信息
	 * @return modifyUserDetail 修改用户明细信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyUserDetail(UserDetail userDetail);
	
	
	/** 根据手机号查用户明细信息
	 * @return findUserDetailByPhone  根据手机号查用户明细信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public UserDetail findUserDetailByPhone(String phone);
	/** 根据用户名查用户明细信息
	 * @return findUserDetailByUserName  根据用户名查用户明细信息
	 * @create author kezhiyi
	 * @create date 2020年10月07日
	 */
	public UserDetail findUserDetailByUserName(String userName);
	/** 根据手机号查用户明细信息
	 * @return findUserDetailByPhone  根据手机号查用户明细信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public UserDetail findDetailByRefcode(String refcode);
	
	/** 根据userCode查用户明细信息
	 * @return findUserDetailByUserCode  根据userCode查用户明细信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public UserDetail findUserDetailByUserCode(String userCode);
	/** 根据id查用户明细信息
	 * @return findUserById  根据id查用户明细信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public UserDetail findUserDetailById(int id);
	/** 根据userName查用户是否存在
	 * @return findUserByPhone  根据userName查用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public int findUserDetailIsExist(String userName);
	/** 实名认证
	 * @return modifyRealStatus   实名认证
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyRealStatus(String userCode, String realName, String idNo);
	/** 修改手机号
	 * @return modifyPhone  修改手机号
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyPhone(String userCode,String phone);
	/** 修改密码
	 * @return modifyPwd  修改密码
	 * @create author kezhiyi
	 * @create date 2020年10月08日
	 */
	public void modifyPwd(Integer userId,String password);
	
	/**
	 * @author: zhangchunming
	 * @date: 2016年9月1日下午8:00:13
	 * @param: userCode
	 * @param: img_addrss
	 * @return void
	 */
	public void modifyUserHeadPic(String userCode,String imgAddrss);
	
	
	public UserDetail findDetail(String userCode);
	/**
	 * @describe:修改关联人手机号
	 * @author: zhangchunming
	 * @date: 2016年9月27日下午12:07:39
	 * @param refPhone
	 * @return: void
	 */
	public void updateRefPhone(PageData pd);
	/**
	 * @describe:修改冻结标志
	 * @author: zhangchunming
	 * @date: 2016年9月30日下午3:09:46
	 * @param pd
	 * @return: void
	 */
	public void updateFreezeFlag(PageData pd);
	

}
