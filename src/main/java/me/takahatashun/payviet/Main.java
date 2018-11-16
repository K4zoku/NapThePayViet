package me.takahatashun.payviet;

import me.takahatashun.payviet.Configurations.YAML;
import me.takahatashun.payviet.Listener.AsyncPlayerChat;
import me.takahatashun.payviet.Listener.PlayerQuit;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static net.payviet.CardLists.getCard;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.Bukkit.getScheduler;

public class Main extends JavaPlugin {
    public static final double version = 1.3;
    public static final String prefix = "§2[§9Pay§cViet§2] §r";
    public static Main instance;
    public static String token;
    public static String merchantID;
    public static String secretKey;
    public static List<String> card;
    static ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getPluginManager();

        ccs.sendMessage(prefix+"§eRegistering event...");
        pm.registerEvents(new AsyncPlayerChat(),this);
        pm.registerEvents(new PlayerQuit(),this);
        ccs.sendMessage(prefix+"§aRegistered event");

        ccs.sendMessage(prefix+"§eSetting up command...");
        getCommand("napthe").setExecutor(new Commands());
        ccs.sendMessage(prefix+"§aCommand was setup!");

        ccs.sendMessage(prefix+"§eChecking config...");
        YAML.saveDefaultConfig();
        YAML.saveDefaultMessage();
        token = YAML.getConfig().getString("PayViet.Token");
        merchantID = YAML.getConfig().getString("PayViet.Merchant_ID");
        secretKey = YAML.getConfig().getString("PayViet.Secret_Key");
        if(!YAML.checkVersion()){
            ccs.sendMessage(prefix+"§cInvalid configuration version, disabling...");
            pm.disablePlugin(this);
        } else {
            if (((token.isEmpty()) && (merchantID.isEmpty()) && (secretKey.isEmpty()))) {
                ccs.sendMessage(prefix + "§cPayViet info is not config, disabling...");
                pm.disablePlugin(this);
            } else {
                ccs.sendMessage(prefix + "§aConfig ok!");
                ccs.sendMessage(prefix + "§aNapThePayViet has been enabled!");
            }
        }
        card = getCard();
        getScheduler().scheduleSyncRepeatingTask(this,new BukkitRunnable() {
            @Override
            public void run() {
                card = getCard();
            }
        }, 0, 20 * 60 * 5); // 5 minutes
    }

    @Override
    public void onDisable() {
        ccs.sendMessage(prefix+"§cGoodbye world :(");
    }
}
