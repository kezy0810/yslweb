package com.qkl.ztysl.provider.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLHelp
{
    private static Logger logger = LoggerFactory.getLogger(SQLHelp.class);

    /**
	 * 查询总纪录数
	 * 
	 * @param mappedStatement
	 *            mapped
	 * @param parameterObject
	 *            参数
	 * @return 总记录数
	 * @throws java.sql.SQLException
	 *             sql查询错误
	 */
    public static int getCount(final MappedStatement mappedStatement, final Object parameterObject) throws SQLException
    {
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        final String count_sql = " select count(1) from (" + boundSql.getSql() + ")  _counttarget";
        if (logger.isDebugEnabled())
        {
            logger.debug("Total count SQL [{}] ", count_sql);
            logger.debug("Total count Parameters: {} ", parameterObject);
        }

        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try
        {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            countStmt = connection.prepareStatement(count_sql);
            DefaultParameterHandler handler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
            handler.setParameters(countStmt);

            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next())
            {
                count = rs.getInt(1);
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("Total count: {}", count);
            }
            return count;
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            finally
            {
                try
                {
                    if (countStmt != null)
                    {
                        countStmt.close();
                    }
                }
                finally
                {
                    if ((connection != null) && !connection.isClosed())
                    {
                        connection.close();
                    }
                }
            }
        }
    }

    public static String rebuildSql(final String sql, final Map<String, Object> mapValues)
    {
        StrSubstitutor sub = new StrSubstitutor(new StrLookup<String>()
        {
            @Override
            public String lookup(final String s)
            {

                for (String key : mapValues.keySet())
                {
					// 1.语句S中必须含有key
					// 2.mapValues对应的key值不为空
                    if ((s.indexOf(":" + key) > -1) && (null != mapValues.get(key))
                            && !"".equals(mapValues.get(key).toString().trim()))
                    {

						// 3.排除部分匹配，如:pichId会匹配到pichIds
                        int maxIndex = s.indexOf(":" + key) + key.length() + 1;
                        if (maxIndex == s.length())
                        {
                            return s.replaceAll(":" + key, mapValues.get(key).toString());
                        }
                        else if (!s.substring(maxIndex, maxIndex + 1).matches("^[A-Za-z0-9]+$"))
                        {
                            if ("@".equals(s.substring(maxIndex, maxIndex + 1)))
                            {
                                String values = mapValues.get(key).toString();
                                String[] valueArray = values.split(",");
                                StringBuffer rtn = new StringBuffer();
                                int orderNum = 1;
                                for (String value : valueArray)
                                {
									if (!StringUtils.isEmpty(StringUtils
											.trimToEmpty((value))))
                                    {
                                        rtn.append("select '");
                                        rtn.append(value);
                                        rtn.append("' as COLUMN_VALUE,");
                                        rtn.append(orderNum++);
                                        rtn.append(" as ORDERNUM from dual union all ");
                                    }
                                }

                                return s.replaceAll(":" + key + "@", rtn.substring(0, rtn.length() - 10));
                            }
                            else
                            {
                                return s.replaceAll(":" + key, mapValues.get(key).toString());
                            }
                        }
                    }
                }

                return "";
            }
        });

        return sub.replace(sql);
    }
}