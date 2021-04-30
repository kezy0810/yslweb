package com.qkl.ztysl.provider.dao;

import java.util.List;

import com.qkl.util.help.pager.PageData;
import com.qkl.ztysl.api.po.log.InterfaceLog;
import com.qkl.ztysl.provider.dbhelper.DAO;

public interface InterfaceLogDao extends DAO<InterfaceLog>{
    
    /**
     * @describe:插入
     * @author: zhangchunming
     * @date: 2016年10月12日下午4:02:32
     * @param pd
     * @return: boolean
     */
    public boolean insert(PageData pd);
    /**
     * @describe:插入
     * @author: zhangchunming
     * @date: 2016年10月12日下午4:02:32
     * @param pd
     * @return: boolean
     */
    public boolean insertSelective(PageData pd);
    /**
     * @describe:根据id查询
     * @author: zhangchunming
     * @date: 2016年10月12日下午4:05:06
     * @param id
     * @return: PageData
     */
    public PageData  selectById(Long id);
    /**
     * @describe:分页查询转账日志
     * @author: zhangchunming
     * @date: 2016年10月12日下午4:01:44
     * @param pd
     * @return: List<PageData>
     */
    public List<PageData>  listPageLog(PageData pd);
    /**
     * @describe:转账日志导出报表
     * @author: zhangchunming
     * @date: 2016年10月12日下午4:03:17
     * @param pd
     * @return: List<PageData>
     */
    public List<PageData>  listAllLog(PageData pd);

}