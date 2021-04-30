package com.qkl.ztysl.provider.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.qkl.ztysl.api.po.sys.SysGencode;
import com.qkl.ztysl.api.po.user.User;
import com.qkl.ztysl.api.po.user.UserDetail;
import com.qkl.ztysl.api.po.user.UserFriendship;
import com.qkl.ztysl.api.service.acc.api.AccService;
import com.qkl.ztysl.api.service.user.api.UserDetailService;
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
public class UserDetailServiceImpl implements UserDetailService {

    private Logger loger = LoggerFactory.getLogger(UserServiceImpl.class);
    private  List<SysGencode> tSysGencodeAll = new  ArrayList<SysGencode>();
    
    
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDetailDao userDetailDao;    
    @Autowired
    private UserFriendshipDao userFriendshipDao;
    @Autowired
    private UserLoginErrDao userLoginErrDao;
    @Autowired
    private AccService accService;
    @Autowired
    private AccInfoDao accDao;
    @Autowired
    private AccDetailDao accDetailDao;
    @Autowired
    private SysGencodeDao sysGencodeDao;
    @Autowired
    private UserLevelcntDao userLevelcntDao;
    
    @Override
    public UserDetail findUserDetail(String userCode, String versionNo) {
        // TODO Auto-generated method stub
        return userDetailDao.findDetail(userCode);
    }

    
}
