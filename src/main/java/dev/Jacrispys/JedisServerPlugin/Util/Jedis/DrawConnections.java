package dev.Jacrispys.JedisServerPlugin.Util.Jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public interface DrawConnections {


    void establishJedisConnection(String host, int port);
    void terminateJedisConnection();

    /**
     * @Method getJedis is the primary method and will
     * @return an instance of getResource() from the JedisPool
     */
    Jedis getJedis();

    /**
     * @deprecated instance is not Auth and will throw error
     * @throws redis.clients.jedis.exceptions.JedisDataException because the Pool is not Authorized
     *
     */
    @Deprecated JedisPool getPool();


    JedisSubHelper getJedisSubHelper();
    JedisPubHelper getJedisPubHelper();
    JedisPoolConfig getJedisPoolConfig();
}
