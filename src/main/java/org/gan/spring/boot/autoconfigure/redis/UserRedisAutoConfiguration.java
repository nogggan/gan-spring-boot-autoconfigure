package org.gan.spring.boot.autoconfigure.redis;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class UserRedisAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(value=JedisPool.class)
	public JedisPool jedisPool(RedisProperties redisProperties) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
		config.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
		config.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().getSeconds());
		config.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
		JedisPool jedisPool = new JedisPool(config,redisProperties.getHost(),redisProperties.getPort(),5000,redisProperties.getPassword(),0);
		return jedisPool;
	}
	
	@Bean
	@ConditionalOnMissingBean(value=RedisService.class)
	public RedisService redisService(JedisPool jedisPool) {
		return new RedisService(jedisPool);
	}
	
}
