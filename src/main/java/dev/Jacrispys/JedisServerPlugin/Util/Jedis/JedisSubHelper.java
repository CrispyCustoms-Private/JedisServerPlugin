package dev.Jacrispys.JedisServerPlugin.Util.Jedis;

import dev.Jacrispys.JedisServerPlugin.JedisServerPluginMain;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

public class JedisSubHelper extends Thread {

    private JedisPubSub pubSub;
    private final Jedis jedis;
    private String[] channel;


    private final Plugin plugin = JedisServerPluginMain.getInstance();



    public JedisSubHelper(JedisHelper jedisHelper) {
        jedis = jedisHelper.getSubJedis();
    }

    public void setListener(JedisPubSub listener) {
        this.pubSub = listener;
        System.out.println("Listener Found!");
    }

    public void setChannel(String... channel) {
        this.channel = channel;
    }

    @SuppressWarnings("unused")
    public void unsubscribe(String channel) {
        try {
            pubSub.unsubscribe(channel);
        } catch (NullPointerException e) {
            System.out.println("Error unsubscribing from channel, must not exist, ignore this if all is working properly.");
            e.printStackTrace();
        }
    }



    @Override
    public void run() {

        System.out.println("Started new thread to handle jedis connections...");
        this.setName("JedisThread-0");

        try {
            if(channel == null && pubSub != null) {
                jedis.subscribe(pubSub, "default");
            }
            if (pubSub == null || channel == null) {
                plugin.getLogger().log(Level.SEVERE, "Either Listener or channel is null! Please report this!");
                return;
            }
            for (String channels : channel) {
                jedis.subscribe(pubSub, channels);
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println(jedis.quit());
        }
    }

}
