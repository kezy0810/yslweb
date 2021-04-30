package com.qkl.ztysl.provider.dbhelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Lists;
import com.qkl.util.help.Pagination;
import com.qkl.ztysl.api.entity.ExportCondition;
import com.qkl.ztysl.api.entity.SearchCondition;


/**
 * <pre>
 * 类说明
 * </pre>
 *
 * <b>功能描述：基础dao实现类，其他所有生成的mapper的实现都继承此类</b> 具体功能描述内容，可以为空。 注意事项： 具体注意事项，可以为空。
 */
public class DaoSupport<T> extends SqlSessionDaoSupport implements DAO<T>, ApplicationContextAware  {
	
	private PlatformTransactionManager transactionManager;

	private String mapXmlNameSpace;

	protected boolean isExternalSysMapper = false;


	public void setMapXmlNameSpace(final String mapXmlNameSpace) {
		this.mapXmlNameSpace = mapXmlNameSpace;
	}

	public String getMapXmlNameSpace() {
		if (StringUtils.isEmpty(StringUtils.trimToEmpty(mapXmlNameSpace))) {
			Class<?>[] interfaces = this.getClass().getInterfaces();
			if ((interfaces != null) && (interfaces.length == 1)) {
				this.mapXmlNameSpace = interfaces[0].getName();
			}
		}
		return this.mapXmlNameSpace;
	}

	@Autowired
	public void setTransactionManager(
			final PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	@Autowired
	@Qualifier("sqlSessionFactory")
	public void setSqlSessionFactory(final SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	

	protected String buildStatement(final String mapperid) {
		return buildStatement(mapperid, true);
	}

	protected String buildStatement(final String mapperid, final boolean isEx) {
		if (isEx) {
			return getMapXmlNameSpace() + ".ex." + mapperid;
		}
		return getMapXmlNameSpace() + "." + mapperid;
	}

	/**
	 * @param name
	 *            - Name for TransactionDefinition
	 * @param type
	 *            - TransactionDefinition定义事务传播级别
	 * @param callback
	 * @return
	 */
	@Override
	public Object doInTransaction(final String name, final int type,
			final CallbackHandle callback) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName(name);
		def.setPropagationBehavior(type);

		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			Object result = callback.execute();
			transactionManager.commit(status);
			return result;
		} catch (RuntimeException ex) {
			transactionManager.rollback(status);
			throw ex;
		} catch (Throwable ex) {
			transactionManager.rollback(status);
			throw new RuntimeException(ex);
		}
	}

	

	@Override
	public int deleteByPrimaryKey(final Long itemid) {
		return delete(buildStatement("deleteByPrimaryKey", false), itemid);
	}

	@Override
	public int insert(final T record) {
		return insert(buildStatement("insert", false), record);
	}

	@Override
	public int insertSelective(final T record) {
		return insert(buildStatement("insertSelective", false), record);
	}

	

	@Override
	@SuppressWarnings("unchecked")
	public T selectByPrimaryKey(final Long itemid) {
		return (T) selectOne(buildStatement("selectByPrimaryKey", false),
				itemid);
	}

	@Override
	public int updateByPrimaryKeySelective(final T record) {
		return update(buildStatement("updateByPrimaryKeySelective", false),
				record);
	}

	@Override
	public int updateByPrimaryKey(final T record) {
		return update(buildStatement("updateByPrimaryKey", false), record);
	}

	private Object selectOne(final String statement, final Object obj) {
		return getSqlSession().selectOne(statement, obj);
	}

	private int insert(final String statement, final Object obj) {
		return getSqlSession().insert(statement, obj);
	}

	private int update(final String statement, final Object obj) {
		return getSqlSession().update(statement, obj);
	}

	private int delete(final String statement, final Object obj) {
		return getSqlSession().delete(statement, obj);
	}

	@Override
	public Date getCurrentTime() {
		return new Date();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countByExample(T example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByExample(T example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <W> Pagination<W> selectByPage(String statement, Object param,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <W> Pagination<W> selectByPage(String statement,
			String countStatement, Object param, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <W> List<W> listPage(SearchCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> List<U> listPage(Integer page, Integer pageSize,
			List<Long> orgIdList, U t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(List<Long> orgIdList, T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> listPageShortEx(Integer page, Integer pageSize,
			List<Long> orgIdList, T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countShortEx(List<Long> orgIdList, T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryErrDataCount(ExportCondition tExportCondition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteErrData(ExportCondition tExportCondition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <W> List<W> findErrAllDao(String fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int queryErrDataCountShortEx(ExportCondition tExportCondition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean deleteErrDataShortEx(ExportCondition tExportCondition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <W> List<W> findErrAllDaoShortEx(String fileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <W> List<W> listPageMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> findByIdList(List<Long> idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findByIdListShortEx(List<Long> idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchUpdate(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchUpdateShortEx(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBatchTemp(List<?> orgInfoTemp, T t, String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByIdList(List<Long> idList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <W> List<W> findByIdLists(String idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(String mapperid, Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBatchTempOrgInfo(List<?> list, T t, String tableName) {
		// TODO Auto-generated method stub
		
	}
	
}
