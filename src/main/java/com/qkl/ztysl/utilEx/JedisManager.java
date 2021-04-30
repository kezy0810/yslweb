package com.qkl.ztysl.utilEx;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

/**
 * 缓存操作
 * 
 * @author kezhiyi
 *
 */
@Component
public class JedisManager {
	private static Logger logger = Logger.getLogger(JedisManager.class);
	
	
	@Autowired
	private JedisClient jedisClient;

	
	private int defaultTimeOut = 60*60*24*30;//默认时间30天


	/**
	 * 获取对象
	 * 
	 * @param key
	 * @return Object
	 */
	public Object get(String key) {
		Object obj = null;
		Jedis jedis = jedisClient.get();
		try {
			obj = byte2Object(jedis.get(getKey(key)));
	

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);
		}
		return obj;
	}
	
	
	/**
	 * 获取对象
	 * 
	 * @param key
	 * @return Object
	 */
	public String getstrval(String key) {
		String val = null;
		Jedis jedis = jedisClient.get();
		try {
			val= jedis.get(key);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);
		}
		return val;
	}

	/**
	 * 判断对象是否存在
	 * 
	 * @param key
	 * @return true or false
	 */
	public boolean isExist(String key) {
		Jedis jedis = jedisClient.get();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return false;
		} finally {
			jedisClient.release(jedis);
//			jedis.disconnect();
		}
	}

	/**
	 * 保存对象
	 * 
	 * @param key
	 *            键
	 * @param obj
	 *            值
	 * @param outTime
	 *            超时时间
	 */
	public boolean set(String key, Object obj, int outTime) {
		Jedis jedis = jedisClient.get();
		boolean flag = true;
		try {
			jedis.set(getKey(key), object2Bytes(obj));
//			jedis.set(key, obj.toString());
			if (outTime != 0) {
				jedis.expire(getKey(key), outTime);
			}else{
			    jedis.expire(getKey(key), defaultTimeOut);
			}
		} catch (Exception e) {
			flag = false;
			logger.error("===save key===" + key + "==="
					+ e.getLocalizedMessage());
		} finally {
			jedisClient.release(jedis);
		}
		return flag;
	}
	
	public boolean setstrval(String key, String val, int outTime) {
		Jedis jedis = jedisClient.get();
		boolean flag = true;
		try {
			jedis.set(key, val);
			if (outTime != 0) {
				jedis.expire(key, outTime);
			}else{
			    jedis.expire(key, defaultTimeOut);
			}
		} catch (Exception e) {
			flag = false;
			logger.error("===save key===" + key + "==="
					+ e.getLocalizedMessage());
		} finally {
			jedisClient.release(jedis);
		}
		return flag;
	}

	/**
	 * 删除对象
	 * 
	 * @param key
	 * @return Long
	 */
	public Long del(String key) {
		Jedis jedis = jedisClient.get();
		try {
			return jedis.del(key);
			// return 1L;
		} catch (Exception e) {
			logger.error("===del key===" + key + "==="
					+ e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);

		}
	}

	
	
	/***
	 * 按照关键字批量获取key
	 * 
	 * 
	 * *****/
	public  Set<String> batchGetKey(String keyword) {
		Jedis jedis = jedisClient.get();
		try {
			Set<String> set = jedis.keys(keyword+"*");  			
			return set;
		} catch (Exception e) {
			logger.error("===batchGetKey===" + keyword + "==="
					+ e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);
		}
	}
	
	
	/***
	 * hget
	 * 
	 * 
	 * *****/
	public  String hget(String key,String field) {
		Jedis jedis = jedisClient.get();
		try {
			String value= jedis.hget(key, field); 				
			return value;
		} catch (Exception e) {
			logger.error("===hget===" + key + "==="+",===field"+field+"==="
					+ e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);
		}
	}
	
	//
	public  boolean hset(String key,String field,String value) {
		Jedis jedis = jedisClient.get();
		boolean flag = true;
		try {
			 jedis.hset(key, field,value); 						
		} catch (Exception e) {
			flag = false;
			logger.error("===hset===" + key + "==="+",===field"+field+"==="
					+ e.getLocalizedMessage());			
		} finally {
			jedisClient.release(jedis);
		}
		return flag;
	}
	
	
	/**
	 * 删除对象
	 * 
	 * @param key
	 * @return Long
	 */
	public Long hdel(String key,String field) {
		Jedis jedis = jedisClient.get();
		try {
			return jedis.hdel(key, field);
			// return 1L;
		} catch (Exception e) {
			logger.error("===hdel===" + key + "==="+",===field"+field+"==="
					+ e.getLocalizedMessage());
			return null;
		} finally {
			jedisClient.release(jedis);

		}
	}
	
	
	
	/**
	 * 字节转化为对象
	 * 
	 * @param bytes
	 * @return Object
	 */
	public static Object byte2Object(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			ObjectInputStream inputStream;
			inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			Object obj = inputStream.readObject();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 对象转化为字节
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] object2Bytes(Object value) {
		if (value == null) {
			return null;
		}

		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(arrayOutputStream);

			outputStream.writeObject(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				arrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return arrayOutputStream.toByteArray();
	}

	public static byte[] getKey(String key) {
		return key.getBytes();
	}
}
