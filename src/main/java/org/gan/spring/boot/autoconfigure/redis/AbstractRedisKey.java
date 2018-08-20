package org.gan.spring.boot.autoconfigure.redis;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public abstract class AbstractRedisKey implements RedisKey{
	
	private int expire;
	
	private String prefix;
	
	@Override
	public int expire() {
		return expire;
	}
	
	@Override
	public String prefix() {
		return this.getClass().getSimpleName()+"_"+prefix;
	}

}
