package org.gan.spring.boot.autoconfigure.redis;

public interface RedisKey {
	
	default int expire() {
		return 0;
	}
	
	default String prefix() {
		return "";
	}

}
