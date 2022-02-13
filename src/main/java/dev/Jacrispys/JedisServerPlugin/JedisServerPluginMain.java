package dev.Jacrispys.JedisServerPlugin;

import dev.Jacrispys.JedisServerPlugin.Util.Jedis.JedisHelper;
import dev.Jacrispys.JedisServerPlugin.Util.Jedis.JedisPubHelper;
import dev.Jacrispys.JedisServerPlugin.Util.Jedis.JedisSubHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.Jacrispys.JedisServerPlugin.Util.Color.color;

public class JedisServerPluginMain extends JavaPlugin {
    private static Plugin mainInstance;
    private JedisHelper jedisHelper;


    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        startMessage();
        mainInstance = this;

        jedisHelper = JedisHelper.getInstance(true);
        JedisPubHelper jedisPubHelper = jedisHelper.getJedisPubHelper();
        JedisSubHelper jedisSubHelper = jedisHelper.getJedisSubHelper();

        jedisSubHelper.setListener(jedisHelper.getChannelListener());
        jedisSubHelper.setChannel(this.getName());
        jedisSubHelper.start();

        jedisPubHelper.publish(this.getName(), "SubToServerChannel");



    }



    protected void startMessage() {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        Bukkit.getConsoleSender().sendMessage(color(" "));
        Bukkit.getConsoleSender().sendMessage(color("  &5╭┳ &d╭━━━╮ &5┳━━━╮"));
        Bukkit.getConsoleSender().sendMessage(color("  &5┃┃ &d┃╭━╮┃ &5┃╭━╮┃"));
        Bukkit.getConsoleSender().sendMessage(color("  &5┃┃ &d┃╰━━╮ &5┃╰━╯┃   &5Jedis&dProxy&5Plugin &8v" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(color("&5╭╮┃┃ &d╰━━╮┃ &5┃╭━━╯   &8Running on: &7" + this.getServer().getName() + " - Bukkit v" + this.getServer().getBukkitVersion()));
        Bukkit.getConsoleSender().sendMessage(color("&5┃╰╯┃ &d┃╰━╯┃&5 ┃┃"));
        Bukkit.getConsoleSender().sendMessage(color("&5╰━━┻ &d╰━━━╯&5 ╰╯"));
        Bukkit.getConsoleSender().sendMessage(color(" "));


    }



    @Override
    public void onDisable() {
        jedisHelper.terminateJedisConnection();
    }




    public static Plugin getInstance() {
        return mainInstance;
    }
}
