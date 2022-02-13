package dev.Jacrispys.JedisServerPlugin.Util.Jedis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JedisPubHelper {

    private final Jedis jedis;

    private String channel;
    private String message;


    public JedisPubHelper(JedisHelper jedisHelper) {
        jedis = jedisHelper.getPubJedis();

        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
        newFixedThreadPool.submit(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    jedis.publish(channel, message);
                } catch (IllegalArgumentException | NullPointerException ex) {
                    ex.printStackTrace();
                } finally {
                    jedisHelper.getPool().returnResource(jedis);
                }
            }
        });
    }

    public void publish(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }



}
