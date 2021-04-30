package com.qkl.ztysl.provider.dao;

import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface UserDao extends DAO<User> {

	
	
	/** 根据手机号查用户注册信息
	 * @return findUserByPhone  根据手机号查用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public User findUserByPhone(String phone);
	/** 根据userCode查用户注册信息
	 * @return findUserByUserCode  根据userCode查用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public User findUserByUserCode(String userCode);
	/** 根据id查用户注册信息
	 * @return findUserById  根据id查用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public User findUserById(int id);
	/** 根据手机号查用户是否存在
	 * @return findUserByPhone  根据手机号查用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public int findUserIsExist(String phone);
	/** 添加用户注册信息
	 * @return addUser 用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void addUser(User user);
	/** 修改用户注册信息
	 * @return modifyUser 用户注册信息
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */ 
	public void modifyUser(User user);
	/** 修改密码
	 * @return modifyPwd  修改密码
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyPwd(String phone,String password);
	/** 修改密码
     * @return modifypwdByUserCode  修改密码
     * @create author zhangchunming
     * @create date 2016年9月6日
     */
	public void modifypwdByUserCode(String userCode,String password);
	
	/** 修改手机号
	 * @return modifyPhone  修改手机号
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyPhone(String userCode,String phone);
	/** 修改登录锁标记
	 * @return modifyLoginLockStaus   修改登录锁标记
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifyLoginLockStaus(String phone, String status);
	/** 修改短信锁标记
	 * @return modifySmsLockStaus   修改短信锁标记
	 * @create author kezhiyi
	 * @create date 2016年8月17日
	 */
	public void modifySmsLockStaus(String phone, String status);
	
	
}
