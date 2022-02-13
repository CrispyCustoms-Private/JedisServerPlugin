package dev.Jacrispys.JedisServerPlugin.Util.Jedis;

import dev.Jacrispys.JedisServerPlugin.JedisServerPluginMain;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.time.Duration;
import java.util.Scanner;

import static dev.Jacrispys.JedisServerPlugin.Util.Color.color;


public class JedisHelper implements DrawConnections {

    private Jedis subJedis;
    private JedisPool subPool;
    private Jedis pubJedis;
    private JedisPool pubPool;
    private JedisPool mainPool;
    private Jedis mainJedis;

    private JedisPubHelper jedisPubHelper;
    private JedisSubHelper jedisSubHelper;
    private JedisPoolConfig jedisPoolConfig;

    private Plugin plugin = JedisServerPluginMain.getInstance();


    private JedisChannelHelper channelListener;

    private boolean isEnabled = false;
    private String authInput = (String) plugin.getConfig().get("authkey");

    public JedisHelper(boolean newConnection) {
        if (newConnection) {
            establishJedisConnection("10.0.0.109", 25564);
            subJedis.auth(authInput);
            pubJedis.auth(authInput);
            mainJedis.auth(authInput);
            jedisPubHelper = new JedisPubHelper(this);
            jedisSubHelper = new JedisSubHelper(this);
            channelListener = new JedisChannelHelper(this);
        }

    }

    protected JedisPoolConfig createPoolConfig() {
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWait(Duration.ofMillis(5000));

        return jedisPoolConfig;
    }


    @Override
    public void establishJedisConnection(String host, int port) {
        pubPool = new JedisPool(createPoolConfig(), host, port);
        subPool = new JedisPool(createPoolConfig(), host, port);
        mainPool = new JedisPool(createPoolConfig(), host, port);
        subJedis = subPool.getResource();
        pubJedis = pubPool.getResource();
        mainJedis = mainPool.getResource();
        try {
            mainJedis.get("test");
            System.out.println("Jedis Connection Established!");

        } catch (JedisDataException ex) {
            if (ex.getMessage().contains("NOAUTH")) {


                try {
                    mainJedis.auth(authInput);
                    mainJedis.get("test");


                    System.out.println("Auth Key Accepted! Starting Jedis Service...");

                    isEnabled = true;

                    return;
                } catch (JedisDataException ex1) {
                    if (ex1.getMessage().contains("NOAUTH") || ex1.getMessage().contains("ERR invalid password")) {
                        System.out.println("Invalid Auth Key! Try Again.");
                    }

                }
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public void terminateJedisConnection() {
        jedisPubHelper.publish("startChannel", "end");
        try {
            mainJedis.flushAll();
            pubJedis.flushAll();
            subJedis.flushAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainJedis.close();
            mainPool.close();

            pubJedis.close();
            pubPool.close();

            subJedis.close();
            subPool.close();
        }
        System.out.println("Jedis Connection Safely Terminated!");
    }

    @Override
    public JedisPool getPool() {
        if (isEnabled) {
            mainPool.getResource().auth(authInput);
            return mainPool;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    @Override
    public Jedis getJedis() {
        if (isEnabled) {
            mainJedis.auth(authInput);
            return mainJedis;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }


    @SuppressWarnings("unused")
    public JedisPool getSubPool() {
        if (isEnabled) {
            subPool.getResource().auth(authInput);
            return subPool;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }


    public Jedis getSubJedis() {
        if (isEnabled) {
            subJedis.auth(authInput);
            return subJedis;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    @SuppressWarnings("unused")
    public JedisPool getPubPool() {
        if (isEnabled) {
            pubPool.getResource().auth(authInput);
            return pubPool;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }


    public Jedis getPubJedis() {
        if (isEnabled) {
            pubJedis.auth(authInput);
            return pubJedis;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    @Override
    public JedisSubHelper getJedisSubHelper() {
        if (isEnabled) {
            return jedisSubHelper;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    @Override
    public JedisPubHelper getJedisPubHelper() {
        if (isEnabled) {
            return jedisPubHelper;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    @Override
    public JedisPoolConfig getJedisPoolConfig() {
        if (isEnabled) {
            return jedisPoolConfig;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }


    public JedisChannelHelper getChannelListener() {
        if (isEnabled) {
            return channelListener;
        } else throw new JedisConnectionException("Connection not established before obtaining instance!");
    }

    public static JedisHelper getInstance(boolean newConnection) {
        return new JedisHelper(newConnection);
    }


}
