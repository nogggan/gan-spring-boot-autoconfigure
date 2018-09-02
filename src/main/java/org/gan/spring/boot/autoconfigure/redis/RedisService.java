package org.gan.spring.boot.autoconfigure.redis;

import java.util.List;

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
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
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
			if(jedis!=null)
				jedis.close();
		}
	}
	
	public String getSet(RedisKey key,String name,Object value) {
		if(StringUtils.isEmpty(name)) {
			log.debug(String.format("RedisService--> getSet()方法 参数key不能为空，传入的key为%s", name));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
		}
		if(StringUtils.isEmpty(value)) {
			log.debug(String.format("RedisService--> getSet()方法 参数value不能为空，传入的value为%s", value));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数value不能为空，传入的value为%s", value));
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realName = key.prefix()+name;
			return jedis.getSet(realName, JSONObject.toJSONString(value));
		} catch (Exception e) {
			log.error(String.format("RedisService--> getSet()方法 name:%s,value:%s==>发生了异常:%s", name,value,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return null;
	}
	
	public <T> T get(RedisKey key,String name,Class<T> clazz) {
		if(StringUtils.isEmpty(name) || clazz==null)
			return null;
		T t = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			String value = jedis.get(realKey);
			if(!StringUtils.isEmpty(value))
				t = JSONObject.parseObject(value, clazz);
		} catch (Exception e) {
			log.error(String.format("RedisService--> get()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return t;
	}
	
	public <T> List<T> getList(RedisKey key,String name,Class<T> clazz) {
		if(StringUtils.isEmpty(name) || clazz==null)
			return null;
		List<T> t = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			String value = jedis.get(realKey);
			if(!StringUtils.isEmpty(value))
				t = JSONObject.parseArray(value, clazz);
		} catch (Exception e) {
			log.error(String.format("RedisService--> getList()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return t;
	}
	
	public long incr(RedisKey key,String name) {
		if(StringUtils.isEmpty(name))
			return 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			return jedis.incr(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> incr()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return 0;
	}
	
	public long decr(RedisKey key,String name) {
		if(StringUtils.isEmpty(name))
			return 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			return jedis.decr(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> decr()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return 0;
	}
	
	public Long expire(RedisKey key,String name,Integer value) {
		if(StringUtils.isEmpty(name))
			return null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			return jedis.expire(realKey, value);
		} catch (Exception e) {
			log.error(String.format("RedisService--> decr()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return null;
	}
	
	public Long setnx(RedisKey key,String name,Object value) {
		if(StringUtils.isEmpty(name)) {
			log.debug(String.format("RedisService--> setnx()方法 参数key不能为空，传入的key为%s", name));
			throw new NullPointerException(String.format("RedisService--> setnx()方法 参数key不能为空，传入的key为%s", name));
		}
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			result = jedis.setnx(realKey, JSONObject.toJSONString(value));
		} catch (Exception e) {
			log.error(String.format("RedisService--> del()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
		return result;
	}
	
	public void del(RedisKey key,String name) {
		if(StringUtils.isEmpty(name)) {
			log.debug(String.format("RedisService--> del()方法 参数key不能为空，传入的key为%s", name));
			throw new NullPointerException(String.format("RedisService--> set()方法 参数key不能为空，传入的key为%s", name));
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = key.prefix()+name;
			jedis.del(realKey);
		} catch (Exception e) {
			log.error(String.format("RedisService--> del()方法 name:%s==>发生了异常:%s", name,e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
	}
	
	public void clear() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushDB();
		} catch (Exception e) {
			log.error(String.format("RedisService--> clear()方法 发生了异常:%s",e));
		}finally {
			if(jedis!=null)
				jedis.close();
		}
	}

}
