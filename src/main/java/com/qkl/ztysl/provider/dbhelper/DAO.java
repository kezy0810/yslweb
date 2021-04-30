package com.qkl.ztysl.provider.dbhelper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.qkl.util.help.Pagination;
import com.qkl.ztysl.api.entity.ExportCondition;
import com.qkl.ztysl.api.entity.SearchCondition;;

/**
 * <pre>
 * 类说明
 * </pre>
 *
 * <b>功能描述：</b> 具体功能描述内容，可以为空。 注意事项： 具体注意事项，可以为空。
 */
public interface DAO<T> extends DaoHelper {
	int countByExample(T example);
	int deleteByExample(T example);
	int deleteByPrimaryKey(Long itemid);
	int insert(T record);
	int insertSelective(T record);
	/*List<T> selectByExample(E example);*/
	T selectByPrimaryKey(Long itemid);
/*	int updateByExampleSelective(@Param("record") T record,@Param("example") E example);
	int updateByExample(@Param("record") T record, @Param("example") E example);*/
	int updateByPrimaryKeySelective(T record);
	int updateByPrimaryKey(T record);
	<W> Pagination<W> selectByPage(String statement, Object param, int pageNo,			int pageSize);

	<W> Pagination<W> selectByPage(String statement, String countStatement,
			Object param, int pageNo, int pageSize);
	
	/*<W> Pagination<W> selectByPage(
			@Param("example") E example, SearchCondition condition);*/
	int count();
	<W> List<W> listPage(SearchCondition condition);
	
	public abstract <U> List<U> listPage(Integer page, Integer pageSize,List<Long> orgIdList,U t);

	public abstract  int count(List<Long> orgIdList, T t);
	
	public abstract  List<T> listPageShortEx(Integer page, Integer pageSize,List<Long> orgIdList,T t);

	public abstract  int countShortEx(List<Long> orgIdList, T t);
	
	public abstract int queryErrDataCount(ExportCondition tExportCondition);
	
	
	public abstract boolean deleteErrData(ExportCondition tExportCondition);
	
	public abstract <W> List<W> findErrAllDao(String fileId);

	public abstract int queryErrDataCountShortEx(ExportCondition tExportCondition);
	
	
	public abstract boolean deleteErrDataShortEx(ExportCondition tExportCondition);
	
	public abstract <W> List<W> findErrAllDaoShortEx(String fileId);

	/*public abstract <W> List<W> listAllDao(@Param("example") final E example);*/

	public abstract <W> List<W> listPageMap(Map<String, Object> condition);

	public abstract int countMap(Map<String, Object> condition);

/*	public abstract <W> Pagination<W> selectByPage(@Param("example") final E example, SearchCondition condition,
			boolean isEx);*/

	public abstract List<T> findByIdList(List<Long> idList);
	
	public abstract List<T> findByIdListShortEx(List<Long> idList);
	
	public abstract void batchUpdate(Map<String, Object> condition);
	
	public abstract void batchUpdateShortEx(Map<String, Object> condition);

	void addBatchTemp(List<?> orgInfoTemp, T t, String tableName);

	public abstract void deleteByIdList(List<Long> idList);
	
	public abstract <W> List<W> findByIdLists(String idList);

	public abstract void execute(String mapperid, Map<String, Object> condition);

	public abstract void addBatchTempOrgInfo(final List<?> list, final T t,
			String tableName);
}
