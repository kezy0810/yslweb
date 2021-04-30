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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.Lists;
import com.qkl.util.help.BeanUtil;
import com.qkl.util.help.Pagination;
import com.qkl.util.help.StringUtil;
import com.qkl.util.help.validator.BeanUtils;
import com.qkl.ztysl.api.entity.ExportCondition;
import com.qkl.ztysl.api.entity.SearchCondition;

/**
 * <pre>
 * 类说明
 * </pre>
 *
 * <b>功能描述：基础dao实现类，其他所有生成的mapper的实现都继承此类</b> 具体功能描述内容，可以为空。 注意事项： 具体注意事项，可以为空。
 */
public class DaoSupport_BAK20160819<T> extends SqlSessionDaoSupport implements DAO<T>, ApplicationContextAware  {
	
	private PlatformTransactionManager transactionManager;

	private String mapXmlNameSpace;

	protected boolean isExternalSysMapper = false;
	
	private ApplicationContext applicationContext;
    @Autowired
    private Map<String, SqlSessionFactory> targetSqlSessionFactorys;
    
    private SqlSessionFactory defaultTargetSqlSessionFactory;
    
    private SqlSession sqlSession;
    
    private boolean externalSqlSession;

	private String shortXmlNameSpace;
	
	/*@Autowired
	private JdbcTemplate jdbcTemplate2;*/

	public String getShortXmlNameSpace() {
		if (StringUtils.isEmpty(StringUtils.trimToEmpty(shortXmlNameSpace))) {
			String xmlNameSpace ="";
			Class<?>[] interfaces = this.getClass().getInterfaces();
			if ((interfaces != null) && (interfaces.length == 1)) {
				xmlNameSpace = interfaces[0].getName();
			}
			String[] shortXmlNameSpaceList = xmlNameSpace.split("\\.");
			int listlength = shortXmlNameSpaceList.length;
			this.shortXmlNameSpace = shortXmlNameSpaceList[listlength-1];		
		}
		return this.shortXmlNameSpace;
		
	}

	public void setShortXmlNameSpace(final String shortXmlNameSpace) {
		this.shortXmlNameSpace = shortXmlNameSpace;
	}

	@Override
    public final SqlSession getSqlSession() {
        if(DbContextHolder.getDbType()==null){
        	DbContextHolder.setDbType(DbContextHolder.ORIDB);
        }
//		SqlSessionFactory targetSqlSessionFactory = targetSqlSessionFactorys.get(DbContextHolder.getDbType());
//        if (targetSqlSessionFactory != null) {
//            setSqlSessionFactory(targetSqlSessionFactory);
//        } else if (defaultTargetSqlSessionFactory != null) {
//            setSqlSessionFactory(defaultTargetSqlSessionFactory);
//            targetSqlSessionFactory = defaultTargetSqlSessionFactory;
//        } else {
//            targetSqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
//            setSqlSessionFactory(targetSqlSessionFactory);
//        }
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
    
        this.sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
        
        return this.sqlSession;
    }
	
    @Override
    protected void checkDaoConfig() {
        //Assert.notNull(getSqlSession(), "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
    }
 
   /* public void setTargetSqlSessionFactorys(Map<String, SqlSessionFactory> targetSqlSessionFactorys) {
        this.targetSqlSessionFactorys = targetSqlSessionFactorys;
    }*/
 
    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;
    }
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

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

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (!this.externalSqlSession) {
			this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		}
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
		this.externalSqlSession = true;
	}

	protected String buildStatement(final String mapperid) {
		return buildStatement(mapperid, true);
	}
	
	protected String buildStatementEx(final String mapperid) {
		return buildStatementEx(mapperid, true);
	}
	
	protected String buildStatementShortEx(final String mapperid) {
		return buildStatementShortEx(mapperid, true);
	}

	protected String buildStatement(final String mapperid, final boolean isEx) {
		if (isEx) {
			return getMapXmlNameSpace() + ".ex." + mapperid;
		}
		return getMapXmlNameSpace() + "." + mapperid;
	}
	
	protected String buildStatementEx(final String mapperid, final boolean isEx) {
		if (isEx) {
			return getMapXmlNameSpace() + "Ex." + mapperid;
		}
		return getMapXmlNameSpace() + "." + mapperid;
	}
	
	protected String buildStatementShortEx(final String mapperid, final boolean isEx) {
		if (isEx) {
			return getShortXmlNameSpace() + "Ex." + mapperid;
		}
		return getShortXmlNameSpace() + "." + mapperid;
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
	public Date getCurrentTime() {
		return new Date();
	}

	/*@Override
	public int countByExample(final E example) {
		return (Integer) selectOne(buildStatement("countByExample", false),
				example);
	}

	@Override
	public int deleteByExample(final E example) {
		return delete(buildStatement("deleteByExample", false), example);
	}*/

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

	/*@Override
	public List<T> selectByExample(final E example) {
		return getSqlSession().selectList(
				buildStatement("selectByExample", false), example);
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public T selectByPrimaryKey(final Long itemid) {
		return (T) selectOne(buildStatement("selectByPrimaryKey", false),
				itemid);
	}

/*	@Override
	public int updateByExampleSelective(@Param("record") final T record,
			@Param("example") final E example) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("record", record);
		para.put("example", example);

		return update(buildStatement("updateByExampleSelective", false), para);
	}

	@Override
	public int updateByExample(@Param("record") final T record,
			@Param("example") final E example) {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("record", record);
		para.put("example", example);

		return update(buildStatement("updateByExample", false), para);
	}*/

	@Override
	public int updateByPrimaryKeySelective(final T record) {
		return update(buildStatement("updateByPrimaryKeySelective", false),
				record);
	}

	@Override
	public int updateByPrimaryKey(final T record) {
		return update(buildStatement("updateByPrimaryKey", false), record);
	}

	@Override
	public <W> Pagination<W> selectByPage(final String statement,
			final Object param, final int pageNo, final int pageSize) {
		return selectByPage(statement, null, param, pageNo, pageSize);
	}

	@Override
	public <W> Pagination<W> selectByPage(final String statement,
			final String countStatement, final Object param, final int pageNo,
			final int pageSize) {
		if ((pageNo > 0) && (pageSize > 0)) {
			int count = 0;
			if (StringUtils.isEmpty(StringUtils.trimToEmpty(countStatement))) {
				MappedStatement mappedStatement = getSqlSession()
						.getConfiguration().getMappedStatement(statement);
				try {
					count = SQLHelp.getCount(mappedStatement, param);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			} else {
				count = getSqlSession().selectOne(countStatement, param);
			}

			// 默认为mysql实现方式，如果兼容其它数据库可以采用mybatis插件的方式拦截RowBounds
			List<W> list = getSqlSession().selectList(statement, param,
					new RowBounds(pageNo, pageSize));
			return new Pagination<W>(pageNo, pageSize, list, count);
		} else {
			// 不符合条件返回空集合
			List<W> list = Lists.newArrayList();
			return new Pagination<W>(pageNo, pageSize, list, list.size());
		}
	}
	protected Map<String, Object> toNullMap(final Map<String, Object> parameter) {
		Iterator<String> iterator = parameter.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (parameter.get(key) == null) {
				parameter.put(key, null);
			}
		}

		return parameter;
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


	/*@Override
	public <W> Pagination<W> selectByPage(@Param("example") final E example,
			SearchCondition condition) {
		return this.selectByPage(this.buildStatement("selectByExample", false),
				example, condition.getCurrentPage(), condition.getPageSize());
	}
	
	@Override
	public <W> Pagination<W> selectByPage(@Param("example") final E example,
			SearchCondition condition,boolean isEx) {
		return this.selectByPage(this.buildStatement("selectByExample", isEx),
				example, condition.getCurrentPage(), condition.getPageSize());
	}
	
	@Override
	public int count() {
		return getSqlSession().selectOne(buildStatement("count"));
	}
	
	@Override
	public <W> List<W> listPage(SearchCondition condition) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageSize", condition.getPageSize());
		map.put("offset", (condition.getCurrentPage() - 1) * condition.getPageSize());
		List<W> list = getSqlSession().selectList(buildStatement("listPage"), map);
		return list;
	}
	

	
	@Override
	public int queryErrDataCountShortEx(ExportCondition tExportCondition) {
		int count =0;
		try{			
			count=getSqlSession().selectOne(this.buildStatementShortEx("findErrCount"), tExportCondition.getBatchno());  	
		}catch(Exception e){
			  logger.info("queryErrDataCount fail ,"+e.getMessage());		
		}
		
		return count;
	}
	
	
	
	
	@Override
	public boolean deleteErrDataShortEx(ExportCondition tExportCondition) {
		try {
			int rs = getSqlSession().delete(
					this.buildStatementShortEx("delErr"), tExportCondition.getBatchno());
			return rs > 0 ? true : false;
		} catch (Exception e) {
			logger.info("deleteErrData fail ," + e.getMessage());
			return false;
		}
	}
	
	
	
	@Override
	public  <W> List<W> findErrAllDaoShortEx(String fileId) {
		return getSqlSession().selectList(this.buildStatementShortEx("findErrAll"),fileId);
		
	}


	@Override
	public void addBatchTemp(final List<?> list, final T t, String tableName) {
		try{
			StringBuffer columnName = new StringBuffer();
			StringBuffer placeholder = new StringBuffer();
			Class<?> cls = t.getClass();
			Field[] fields = cls.getDeclaredFields();
			Method[] methods = cls.getDeclaredMethods();
			for (int j = 1; j <= fields.length; j++) {
				Field field = fields[j - 1];
				String[] temp = field.toString().split("\\.");
	
				if ("serialVersionUID".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("deptName".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("deptCode".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("deptManager".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("sectionName".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("sectionCode".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("sectionManager".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("subsidiaryCode".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("subsidiaryName".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("subsidiaryManager".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("batchno".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("errinfo".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("paymentTypeStr".equals(temp[temp.length - 1])) {
					continue;
				}
				if ("curmonthintimeLong".equals(temp[temp.length - 1])) {
					continue;
				}
				String fieldGetName = BeanUtils.parGetName(field.getName());
				if (!BeanUtils.checkGetMet(methods, fieldGetName)) {
					continue;
				}
					columnName.append(trans(temp[temp.length - 1]) + ",");
					placeholder.append("?,");
			}
			String columnNameStr = columnName.toString();
			columnNameStr = columnNameStr.substring(0, columnNameStr.length() - 1);
			String placeholderStr = placeholder.toString();
			placeholderStr = placeholderStr.substring(0,
					placeholderStr.length() - 1);
			String sql = "insert into " + tableName + "(" + columnNameStr
					+ ") values (" + placeholderStr + ")";
	
			jdbcTemplate2.batchUpdate(sql, new MyBatchPreparedStatementSetter(list));
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	private class MyBatchPreparedStatementSetter implements BatchPreparedStatementSetter{  
	     final List temList;  
	       
	     public MyBatchPreparedStatementSetter(List list){  
	         temList = list;  
	     }  
	     public int getBatchSize() {  
	         return temList.size();  
	     }  
	   
	     public void setValues(PreparedStatement ps, int i) throws SQLException {
			T t = (T) temList.get(i);

			Class<?> cls = t.getClass();
			Method[] methods = cls.getDeclaredMethods();
			Field[] fields = cls.getDeclaredFields();
			int k =1;
			for (int j = 1; j <= fields.length; j++) {
				Field field = fields[j - 1];
				String[] temp = field.toString().split("\\.");
				try {
					if ("serialVersionUID".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("deptName".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("deptCode".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("deptManager".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("sectionName".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("sectionCode".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("sectionManager".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("subsidiaryCode".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("subsidiaryName".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("subsidiaryManager".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("batchno".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("errinfo".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("paymentTypeStr".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("curmonthintimeLong".equals(temp[temp.length - 1])) {
						continue;
					}
					String fieldGetName = BeanUtils.parGetName(field.getName());
					if (!BeanUtils.checkGetMet(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = cls.getMethod(fieldGetName,
							new Class[]{});
					Object fieldVal = fieldGetMet.invoke(t, new Object[]{});
					if (fieldVal != null) {
						String type = field.getType().toString();// 得到此属性的类型
						if (type.endsWith("String")) {
							ps.setString(k, fieldVal.toString());
						}
						if (type.endsWith("BigDecimal")) {
							ps.setBigDecimal(k, BigDecimal.valueOf(Double
									.parseDouble(fieldVal.toString())));
						}
						if (type.endsWith("Long")) {
							ps.setLong(k, Long.parseLong(fieldVal.toString()));
						}
						if (type.endsWith("Integer")) {
							ps.setInt(k, Integer.parseInt(fieldVal.toString()));
						}
						if (type.endsWith("Date")) {
							java.util.Date uDate = (java.util.Date)fieldVal;
							java.sql.Date sDate = new java.sql.Date(uDate.getTime());
							ps.setDate(k, sDate);
						}
					} else {
						String type = field.getType().toString();// 得到此属性的类型
						if (type.endsWith("String")) {
							ps.setString(k, "");
						}
						if (type.endsWith("BigDecimal")) {
							ps.setBigDecimal(k,
									BigDecimal.valueOf(Double.parseDouble("0")));
						}
						if (type.endsWith("Long")) {
							ps.setLong(k, 0);
						}
						if (type.endsWith("Integer")) {
							ps.setInt(k, 0);
						}
						if (type.endsWith("Date")) {
							ps.setDate(k, null);
						}
					}
					k++;
				} catch (Exception e) {
					continue;
				}
			}
		}  
	 }
	
	/*@Override
	public void addBatchTempOrgInfo(final List<?> list, final T t, String tableName) {
		StringBuffer columnName = new StringBuffer();
		StringBuffer placeholder = new StringBuffer();
		Class<?> cls = t.getClass();
		Field[] fields = cls.getDeclaredFields();
		Method[] methods = cls.getDeclaredMethods();
		for (int j = 1; j <= fields.length; j++) {
			Field field = fields[j - 1];
			String[] temp = field.toString().split("\\.");

			if ("serialVersionUID".equals(temp[temp.length - 1])) {
				continue;
			}
			if ("batchno".equals(temp[temp.length - 1])) {
				continue;
			}
			if ("errinfo".equals(temp[temp.length - 1])) {
				continue;
			}
			String fieldGetName = BeanUtils.parGetName(field.getName());
			if (!BeanUtils.checkGetMet(methods, fieldGetName)) {
				continue;
			}
				columnName.append(trans(temp[temp.length - 1]) + ",");
				placeholder.append("?,");
		}
		String columnNameStr = columnName.toString();
		columnNameStr = columnNameStr.substring(0, columnNameStr.length() - 1);
		String placeholderStr = placeholder.toString();
		placeholderStr = placeholderStr.substring(0,
				placeholderStr.length() - 1);
		String sql = "insert into " + tableName + "(" + columnNameStr
				+ ") values (" + placeholderStr + ")";

		jdbcTemplate2.batchUpdate(sql, new OrgInfoBatchPreparedStatementSetter(list));
	}*/
	
	private class OrgInfoBatchPreparedStatementSetter implements BatchPreparedStatementSetter{  
		final List temList;  
		
		public OrgInfoBatchPreparedStatementSetter(List list){  
			temList = list;  
		}  
		public int getBatchSize() {  
			return temList.size();  
		}  
		
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			T t = (T) temList.get(i);
			
			Class<?> cls = t.getClass();
			Method[] methods = cls.getDeclaredMethods();
			Field[] fields = cls.getDeclaredFields();
			int k =1;
			for (int j = 1; j <= fields.length; j++) {
				Field field = fields[j - 1];
				String[] temp = field.toString().split("\\.");
				try {
					if ("serialVersionUID".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("batchno".equals(temp[temp.length - 1])) {
						continue;
					}
					if ("errinfo".equals(temp[temp.length - 1])) {
						continue;
					}
					String fieldGetName = BeanUtils.parGetName(field.getName());
					if (!BeanUtils.checkGetMet(methods, fieldGetName)) {
						continue;
					}
					Method fieldGetMet = cls.getMethod(fieldGetName,
							new Class[]{});
					Object fieldVal = fieldGetMet.invoke(t, new Object[]{});
					if (fieldVal != null) {
						String type = field.getType().toString();// 得到此属性的类型
						if (type.endsWith("String")) {
							ps.setString(k, fieldVal.toString());
						}
						if (type.endsWith("BigDecimal")) {
							ps.setBigDecimal(k, BigDecimal.valueOf(Double
									.parseDouble(fieldVal.toString())));
						}
						if (type.endsWith("Long")) {
							ps.setLong(k, Long.parseLong(fieldVal.toString()));
						}
						if (type.endsWith("Integer")) {
							ps.setInt(k, Integer.parseInt(fieldVal.toString()));
						}
						if (type.endsWith("Date")) {
							java.util.Date uDate = (java.util.Date)fieldVal;
							java.sql.Date sDate = new java.sql.Date(uDate.getTime());
							ps.setDate(k, sDate);
						}
					} else {
						String type = field.getType().toString();// 得到此属性的类型
						if (type.endsWith("String")) {
							ps.setString(k, "");
						}
						if (type.endsWith("BigDecimal")) {
							ps.setBigDecimal(k,
									BigDecimal.valueOf(Double.parseDouble("0")));
						}
						if (type.endsWith("Long")) {
							ps.setLong(k, 0);
						}
						if (type.endsWith("Integer")) {
							ps.setInt(k, 0);
						}
						if (type.endsWith("Date")) {
							ps.setDate(k, null);
						}
					}
					k++;
				} catch (Exception e) {
					continue;
				}
			}
		}  
	}
	
	
	
	
	@Override
	public int queryErrDataCount(ExportCondition tExportCondition) {
		int count =0;
		try{			
			count=getSqlSession().selectOne(this.buildStatement("findErrCount"), tExportCondition.getBatchno());  	
		}catch(Exception e){
			  logger.info("queryErrDataCount fail ,"+e.getMessage());		
		}
		
		return count;
	}
	
	
	
	
	@Override
	public boolean deleteErrData(ExportCondition tExportCondition) {
		try {
			int rs = getSqlSession().delete(
					this.buildStatement("delErr"), tExportCondition.getBatchno());
			return rs > 0 ? true : false;
		} catch (Exception e) {
			logger.info("deleteErrData fail ," + e.getMessage());
			return false;
		}
	}
	
	@Override
	public  <W> List<W> findErrAllDao(String fileId) {
		return getSqlSession().selectList(this.buildStatement("findErrAll"),fileId);
		
	}
	
	
	/*@Override
	public <W> List<W> listAllDao(@Param("example") final E example) {
		
		List<W> list = getSqlSession().selectList(buildStatement("listAll"),example);
		return list;
	}*/
	
	
	@Override
	public <W> List<W> listPageMap(Map<String, Object> condition) {
		
		List<W> list = getSqlSession().selectList(buildStatement("listPage"), condition);
		return list;
	}
	
	@Override
	public int countMap(Map<String, Object> condition) {
		return getSqlSession().selectOne(buildStatement("count"),condition);
	}


	@Override
	public int count(List<Long> orgIdList, T t) {
		int rs = 0;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("orgIdList", orgIdList);
			Map<String, Object> beanMap = BeanUtil.bean2Map(t);
			condition.putAll(beanMap);
			rs = getSqlSession().selectOne(this.buildStatement("count"),
					condition);
			return rs;
		} catch (Exception e) {
			logger.info("count fail ," + e.getMessage());
			return rs;
		}
	}

	@Override
	public <U> List<U> listPage(Integer page, Integer pageSize,
			List<Long> orgIdList, U t) {
		List<U> rsList = new ArrayList<U>();
		try{
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("pageSize", pageSize);
			condition.put("offset", (page - 1) * pageSize);
			condition.put("orgIdList", orgIdList);
			Map<String, Object> beanMap = BeanUtil.bean2Map(t);
			condition.putAll(beanMap);
			rsList = getSqlSession().selectList(this.buildStatement("listPage"), condition);
			return rsList;
		}
		catch(Exception e){
			logger.info("listPage fail ,"+e.getMessage());
			return rsList;
		}
	}

	@Override
	public List<T> listPageShortEx(Integer page, Integer pageSize,
			List<Long> orgIdList, T t) {
		List<T> rsList = new ArrayList<T>();
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("pageSize", pageSize);
			condition.put("offset", (page - 1) * pageSize);
			condition.put("orgIdList", orgIdList);
			Map<String, Object> beanMap = BeanUtil.bean2Map(t);
			condition.putAll(beanMap);
			rsList = getSqlSession().selectList(
					this.buildStatementShortEx("listPage"), condition);
			return rsList;
		} catch (Exception e) {
			logger.info("listPage fail ," + e.getMessage());
			return rsList;
		}
	}

	@Override
	public int countShortEx(List<Long> orgIdList, T t) {
		int rs = 0;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("orgIdList", orgIdList);
			Map<String, Object> beanMap = BeanUtil.bean2Map(t);
			condition.putAll(beanMap);
			rs = getSqlSession().selectOne(this.buildStatementShortEx("count"),
					condition);
			return rs;
		} catch (Exception e) {
			logger.info("count fail ," + e.getMessage());
			return rs;
		}
	}

	@Override
	public List<T> findByIdList(List<Long> idList) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("idList", idList);
		List<T> rsList = getSqlSession().selectList(
				this.buildStatement("findByIdList"), condition);
		return rsList;
	}
	
	@Override
	public List<T> findByIdListShortEx(List<Long> idList) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("idList", idList);
		List<T> rsList = getSqlSession().selectList(
				this.buildStatementShortEx("findByIdList"), condition);
		return rsList;
	}

	
	@Override
	public void batchUpdate(Map<String, Object> condition) {
		try{
			getSqlSession().update(this.buildStatement("batchUpdate"), condition);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void batchUpdateShortEx(Map<String, Object> condition) {
		try{
			getSqlSession().update(this.buildStatementShortEx("batchUpdate"), condition);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String trans(String str) {
		List record = new ArrayList();
		for (int i = 0; i < str.length(); i++) {
			char tmp = str.charAt(i);
			if ((tmp <= 'Z') && (tmp >= 'A')) {
				record.add(i);// 记录每个大写字母的位置
			}
		}
		str = str.toLowerCase();
		char[] charofstr = str.toCharArray();
		String[] t = new String[record.size()];
		for (int i = 0; i < record.size(); i++) {
			t[i] = "_" + charofstr[Integer.parseInt(record.get(i).toString())];// 加“_”
		}
		String result = "";
		int flag = 0;
		for (int i = 0; i < str.length(); i++) {
			if ((flag < record.size()) && (i == Integer.parseInt(record.get(flag).toString()) )) {
				result += t[flag];
				flag++;
			} else
				result += charofstr[i];
		}
		return result;
	}
     

	@Override
	public void deleteByIdList(List<Long> idList) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("idList", idList);
		getSqlSession().delete(this.buildStatement("deleteByIdList"), condition);
	}
	@Override
	public <W> List<W> findByIdLists(String idList) {
		Map<String, Object> condition = new HashMap<String, Object>();
		List<String> temp = StringUtil.StringToList(idList, ",");
		List<Long> idList2 = new ArrayList<Long>();
		for (String string : temp) {
			idList2.add(Long.parseLong(string));
		}
		condition.put("idList", idList2);
		List<W> rsList = getSqlSession().selectList(
				this.buildStatement("findByIdList"), condition);
		return rsList;
	}
	
	@Override
	public void execute(String mapperid,Map<String, Object> condition) {
		getSqlSession().update(this.buildStatement(mapperid), condition);
	}

	/*@Override
	public <W> Pagination<W> selectByPage(E example, SearchCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}*/

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

	/*@Override
	public <W> Pagination<W> selectByPage(E example, SearchCondition condition,
			boolean isEx) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public void addBatchTemp(List<?> orgInfoTemp, T t, String tableName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBatchTempOrgInfo(List<?> list, T t, String tableName) {
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
	
}
