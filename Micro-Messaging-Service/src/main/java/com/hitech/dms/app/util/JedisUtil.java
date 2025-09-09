package com.hitech.dms.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Slf4j
public class JedisUtil {

	@Autowired(required = false)
	private JedisPool jedisPool;

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	/**
	 * set value
	 *
	 * aramparam key
	 * 
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.set(key, value);
		} catch (Exception e) {
			log.error("set key: {} value: {} error", key, value, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * set value
	 *
	 * aramparam key
	 * 
	 * @param value
	 * @param expireTime expiration time, unit: s
	 * @return
	 */
	public String set(String key, String value, int expireTime) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.setex(key, expireTime, value);
		} catch (Exception e) {
			log.error("set key:{} value:{} expireTime:{} error", key, value, expireTime, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * set value
	 *
	 * aramparam key
	 * 
	 * @param value
	 * @return
	 */
	public Long setnx(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.setnx(key, value);
		} catch (Exception e) {
			log.error("set key:{} value:{} error", key, value, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * value
	 *
	 * aramparam key
	 * 
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(key);
		} catch (Exception e) {
			log.error("get key:{} error", key, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * delete key
	 *
	 * aramparam key
	 * 
	 * @return
	 */
	public Long del(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.del(key.getBytes());
		} catch (Exception e) {
			log.error("del key:{} error", key, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * Determine if the key exists
	 *
	 * aramparam key
	 * 
	 * @return
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.exists(key.getBytes());
		} catch (Exception e) {
			log.error("exists key:{} error", key, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * Set the key expiration time
	 *
	 * aramparam key
	 * 
	 * @param expireTime expiration time, unit: s
	 * @return
	 */
	public Long expire(String key, int expireTime) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.expire(key.getBytes(), expireTime);
		} catch (Exception e) {
			log.error("expire key:{} error", key, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	/**
	 * Get the remaining time
	 *
	 * aramparam key
	 * 
	 * @return
	 */
	public Long ttl(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.ttl(key);
		} catch (Exception e) {
			log.error("ttl key:{} error", key, e);
			return null;
		} finally {
			close(jedis);
		}
	}

	private void close(Jedis jedis) {
		if (null != jedis) {
			jedis.close();
		}
	}

}
