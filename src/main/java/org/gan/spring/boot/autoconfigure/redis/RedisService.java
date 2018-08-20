package org.gan.spring.boot.autoconfigure.redis;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
public class RedisService {
	
	private JedisPool jedisPool;

	public RedisService(JedisPool jedisPool) {
		super();
		this.jedisPool = jedisPool;
	}
	
	public void set(RedisKey key,String name,Object value) {
		if(StringUtils.isEmpty(name)) {
			log.debug(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
		}
		if(StringUtils.isEmpty(value)) {
			log.debug(String.format("RedisService--> set()方法 参数value不能为空，传入的value为%s", value));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数value不能为空，传入的value为%s", value));
		}
		Jedis jedis = jedisPool.getResource();
		try {
			int expire = key.expire();
			String realName = key.prefix()+name;
			if(expire > 0) {
				jedis.setex(realName, expire,JSONObject.toJSONString(value));
			}else {
				jedis.set(realName, JSONObject.toJSONString(value));
			}
		} catch (Exception e) {
			log.error(String.format("RedisService--> set()方法 name:%s,value:%s==>发生了异常:%s", name,value,e));
		}finally {
			jedis.close();
		}
	}
	
	public <T> T get(RedisKey key,String name,Class<T> clazz) {
		if(StringUtils.isEmpty(name) || clazz==null)
			return null;
		Jedis jedis = jedisPool.getResource();
		T t = null;
		try {
			String realKey = key.prefix()+name;
			String value = jedis.get(realKey);
			t = JSONObject.parseObject(value, clazz);
		} catch (Exception e) {
			log.error(String.format("RedisService--> set()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			jedis.close();
		}
		return t;
	}
	
	public long incr(RedisKey key,String name) {
		if(StringUtils.isEmpty(name))
			return 0;
		Jedis jedis = jedisPool.getResource();
		try {
			String realKey = key.prefix()+name;
			return jedis.incr(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> set()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			jedis.close();
		}
		return 0;
	}
	
	public long decr(RedisKey key,String name) {
		if(StringUtils.isEmpty(name))
			return 0;
		Jedis jedis = jedisPool.getResource();
		try {
			String realKey = key.prefix()+name;
			return jedis.decr(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> set()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			jedis.close();
		}
		return 0;
	}
	
	public void del(RedisKey key,String name) {
		if(StringUtils.isEmpty(name)) {
			log.debug(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
		}
		Jedis jedis = jedisPool.getResource();
		try {
			String realKey = key.prefix()+name;
			jedis.del(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> set()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			jedis.close();
		}
	}

}
