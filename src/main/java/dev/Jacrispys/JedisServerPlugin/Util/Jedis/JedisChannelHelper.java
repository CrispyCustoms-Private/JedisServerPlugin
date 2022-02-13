package dev.Jacrispys.JedisServerPlugin.Util.Jedis;

import dev.Jacrispys.JedisServerPlugin.JedisServerPluginMain;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

public class JedisChannelHelper extends JedisPubSub {

    private final JedisHelper jedisHelper;
    private final JedisSubHelper subs;
    private final JedisPubHelper pubs;
    private final Plugin plugin = JedisServerPluginMain.getInstance();

    public JedisChannelHelper(JedisHelper jedisHelper) {
        pubs = jedisHelper.getJedisPubHelper();
        subs = jedisHelper.getJedisSubHelper();
        this.jedisHelper = jedisHelper;
    }


    @Override
    public void onMessage(String channel, String message) {

        if(message.equalsIgnoreCase("end")) {
            unsubscribe(channel);
            return;
        }

        if(channel.equalsIgnoreCase("startChannel")) {
            for(int i = 0; i  < 10; i++) {
                System.out.println("MESSAGE RECEIVED!!!");
            }
        } else {
            System.out.println("UNKNOWN MESSAGE!");
            return;
        }
        unsubscribe(channel);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        plugin.getLogger().log(Level.INFO, channel + " test # = " + subscribedChannels);

        if(channel.equalsIgnoreCase("default")) {
            System.out.println("Default channel used without publishing!");
            unsubscribe(channel);
        }

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        plugin.getLogger().log(Level.INFO, channel + " was unsubbed, remaining:  " + subscribedChannels);
    }
}
