package me.takahatashun.payviet.Configurations;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;

import static me.takahatashun.payviet.Main.*;

public class YAML {

    private static File configFile = new File(instance.getDataFolder(), "config.yml");
    private static FileConfiguration newConfig = null;
    private static String messageFileName =
            "Messages_{Lang}.yml".replaceAll("\\{+Lang+}",getConfig().getString("Settings.Locale"));
    private static File messageFile = new File(instance.getDataFolder()+"/Language",messageFileName);
    private static FileConfiguration newMessage = null;

    public static FileConfiguration getConfig(){
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }
    public static boolean checkVersion(){
        double conf_version = getConfig().getDouble("Version");
        double mess_version = getMessage().getDouble("Version");
        return conf_version == version && mess_version == version;
    }
    @SuppressWarnings("deprecation")
    public static void reloadConfig(){
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = instance.getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        final YamlConfiguration defConfig;
        if (isStrictlyUTF8()) {
            defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (final IOException e) {
                instance.getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml");
                return;
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                instance.getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
            }

            try {
                defConfig.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                instance.getLogger().log(Level.SEVERE, "Cannot load configuration from jar");
            }
        }
        newConfig.setDefaults(defConfig);
        try {
            newConfig.load(configFile);
        } catch (FileNotFoundException e){
            saveDefaultConfig();
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveDefaultConfig(){
        if(configFile == null || !(configFile.exists())){
            try {
                instance.saveResource("config.yml", false);
            } catch (NullPointerException nullex){
                return;
            }
        }
    }

    public static FileConfiguration getMessage(){
        if (newMessage == null) {
            reloadMessages();
        }
        return newMessage;
    }
    @SuppressWarnings("deprecation")
    public static void reloadMessages(){

        newMessage = YamlConfiguration.loadConfiguration(messageFile);
        final InputStream defMessagesStream = instance.getResource("Language/"+messageFileName);
        if (defMessagesStream == null) {
            return;
        }

        final YamlConfiguration defMessages;
        if (isStrictlyUTF8()) {
            defMessages = YamlConfiguration.loadConfiguration(new InputStreamReader(defMessagesStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            defMessages = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defMessagesStream);
            } catch (final IOException e) {
                instance.getLogger().log(Level.SEVERE, "Unexpected failure reading"+messageFileName);
                return;
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                instance.getLogger().warning("Default system encoding may have misread "+messageFileName+" from plugin jar");
            }

            try {
                defMessages.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                instance.getLogger().log(Level.SEVERE, "Cannot load configuration from jar");
            }
            newMessage.setDefaults(defMessages);
            try {
                newMessage.load(messageFile);
            } catch (FileNotFoundException e){
              saveDefaultMessage();
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void saveDefaultMessage(){
        if(messageFile == null || !(messageFile.exists())){
            instance.saveResource("Language/"+messageFileName,false);
        }
    }
    public static void saveMessage(){
        saveDefaultMessage();
        try {
            getMessage().save(messageFile);
        } catch (Exception e){
            saveDefaultMessage();
            reloadMessages();
        }
    }
    private static boolean isStrictlyUTF8() {
        return instance.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }
}
