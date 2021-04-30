package com.qkl.ztysl.utilEx;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClient {
    private Integer maxActive = 600;
    private Integer maxIdle = 300;
    private Integer maxWait = 10000;

    private final JedisPool jedisPool;

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public JedisClient(String address,Integer port,String pass){
        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(maxActive);
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
//        config.setMaxWait(maxWait);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, address, port,300000,pass);
    }

    //get a jedis client,but must return it.
    public Jedis get(){
        return jedisPool.getResource();
    }

    public void release(Jedis jedis){
        if(jedis != null && jedis.getDB() != 0){
            try {
                jedis.select(0);
            }catch(Exception ex){
            }
        }
        Jedis resource = jedisPool.getResource();
        if(resource!=null){
            resource.close();
        }
    }
}