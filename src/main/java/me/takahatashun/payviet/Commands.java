package me.takahatashun.payviet;

import me.takahatashun.Library.Chat.ChatAPI;
import me.takahatashun.payviet.Configurations.YAML;
import me.takahatashun.payviet.Handlers.CalculateTop;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

import static me.takahatashun.Library.Chat.ChatAPI.translateColorCode;
import static me.takahatashun.payviet.Listener.AsyncPlayerChat.*;
import static me.takahatashun.payviet.Main.instance;
import static me.takahatashun.payviet.Main.version;
import static net.payviet.PayVietCard.*;
public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender,Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("choose"))) {
                for(String Card : cardList) {
                    String cardType = Card.split(":")[0];
                    String text = translateColorCode(YAML.getMessage().getString("ChatGui.CardType.Text").replaceAll("(?ium)\\{Card_Type\\}",cardType));
                    List<String> hovers = YAML.getMessage().getStringList("ChatGui.CardType.Hover");
                    StringBuilder hover = new StringBuilder();
                    for (int i=0; i<hovers.size();i++) {
                        String hoverln = hovers.get(i);
                        hoverln = hoverln.replaceAll("(?ium)\\{Card_Type}",cardType);
                        hoverln = translateColorCode(hoverln);
                        if(i < (hovers.size()-1)){
                            hover.append(hoverln).append("\n");
                        } else {
                            hover.append(hoverln);
                        }
                    }
                    TextComponent chat = ChatAPI.getTextComponent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(hover.toString()).create(),
                                    ClickEvent.Action.RUN_COMMAND, "/napthe choose "+cardType,
                                    text);
                    p.spigot().sendMessage(chat);
                }

            }
            if(args.length == 1){
                if (args[0].equalsIgnoreCase("about")) {
                    p.sendMessage("§6======= §2[§9Pay§cViet§2] §6=======");
                    p.sendMessage("§e§lAuthor: §fTakahataShun");
                    p.sendMessage("§e§lVersion: §f"+version);
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if(p.hasPermission("napthe.reload")) {
                        instance.reloadConfig();
                        YAML.reloadConfig();
                        YAML.reloadMessages();
                        sender.sendMessage(translateColorCode(
                                YAML.getMessage().getString("Messages.Reloaded")
                        ));
                    } else {
                        p.sendMessage(translateColorCode(
                                YAML.getMessage().getString("Messages.NoPerm")
                        ));
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    p.sendMessage("/napthe about - Show info plugin");
                    p.sendMessage("/napthe help - Display this page");
                    p.sendMessage("/napthe top [type] - Show top donator");
                    p.sendMessage("/napthe choose [card_type] [card_amount] - Donate");
                    p.sendMessage("/napthe reload - Reload config, messages");
                }
                if(args[0].equalsIgnoreCase("top")){
                    try {
                        Map<String,Double> top = CalculateTop.execute("total");
                        printTop(p, top);
                    } catch (Exception exc){
                        return false;
                    }
                }
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("choose")) {
                    if(cardNameList.contains(args[1])) {
                        for (String Card : cardList) {
                            String cardType = Card.split(":", 2)[0];
                            if (args[1].equalsIgnoreCase(cardType)) {
                                for (int cardAmount : amountList) {
                                    String text = translateColorCode(
                                            YAML.getMessage().getString("ChatGui.CardAmount.Text")
                                                    .replaceAll("(?ium)\\{Card_Amount}",
                                                            String.valueOf(cardAmount)));
                                    List<String> hovers = YAML.getMessage().getStringList("ChatGui.CardAmount.Hover");
                                    StringBuilder hover = new StringBuilder();

                                    for (int i = 0; i < hovers.size(); i++) {
                                        String hoverln = hovers.get(i);
                                        hoverln = hoverln.replaceAll("(?ium)\\{Card_Amount\\}", String.valueOf(cardAmount));
                                        hoverln = translateColorCode(hoverln);
                                        if (i < (hovers.size() - 1)) {
                                            hover.append(hoverln).append("\n");
                                        } else {
                                            hover.append(hoverln);
                                        }
                                    }
                                    TextComponent chat = ChatAPI.getTextComponent(
                                            HoverEvent.Action.SHOW_TEXT,
                                            new ComponentBuilder(hover.toString()).create(),
                                            ClickEvent.Action.RUN_COMMAND,
                                            "/napthe choose " + cardType + " " + cardAmount,
                                            text);
                                    p.spigot().sendMessage(chat);
                                }
                            }
                        }
                    } else {
                        p.sendMessage(translateColorCode(YAML.getMessage().getString("Messages.InvalidType")));
                    }
                }
                if(args[0].equalsIgnoreCase("top")){
                    try {
                        if (args[1].equalsIgnoreCase("day")) {
                            Map<String, Double> top = CalculateTop.execute("day");
                            printTop(p, top);
                        }
                        if (args[1].equalsIgnoreCase("month")) {
                            Map<String, Double> top = CalculateTop.execute("month");
                            printTop(p, top);
                        }
                        if (args[1].equalsIgnoreCase("year")) {
                            Map<String, Double> top = CalculateTop.execute("year");
                            printTop(p, top);
                        }
                    } catch(Exception exc){
                        return false;
                    }
                }
            }
            if (args.length == 3){
                if(args[0].equalsIgnoreCase("choose")) {
                    if(cardNameList.contains(args[1])) {
                        for (String Card : cardList) {
                            String cardType = Card.split(":", 2)[0];
                            if (args[1].equalsIgnoreCase(cardType)) {
                                if (amountList.contains(Integer.parseInt(args[2]))) {
                                    for (int Amount : amountList) {
                                        if (args[2].equalsIgnoreCase(String.valueOf(Amount))) {
                                            if (!(isTriggerStep1(p))) {
                                                if (!(isTriggerStep2(p))) {
                                                    int type = Integer.parseInt(Card.split(":", 2)[1]);
                                                    triggerStep1(p, type, Amount);
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    p.sendMessage(translateColorCode(YAML.getMessage().getString("Messages.InvalidAmount")));
                                }
                            }
                        }
                    } else {
                        p.sendMessage(translateColorCode(YAML.getMessage().getString("Messages.InvalidType")));
                    }
                }
            }
        } else {
            if(args.length == 0 || args[0].equalsIgnoreCase("choose")){
                sender.sendMessage(translateColorCode(
                        YAML.getMessage().getString("Messages.OnlyPlayer")
                ));
            }
            if(args.length == 1){
                if (args[0].equalsIgnoreCase("about")) {
                    sender.sendMessage("§6======= §2[§9Pay§cViet§2] §6=======");
                    sender.sendMessage("§e§lAuthor: §fTakahataShun");
                    sender.sendMessage("§e§lVersion: §f"+version);
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    instance.reloadConfig();
                    YAML.reloadConfig();
                    YAML.reloadMessages();
                    sender.sendMessage(translateColorCode(
                            YAML.getMessage().getString("Messages.Reloaded")
                    ));
                }
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage("/napthe about - Show info plugin");
                    sender.sendMessage("/napthe help - Display this page");
                    sender.sendMessage("/napthe top [type] - Show top donator");
                    sender.sendMessage("/napthe reload - Reload config, messages");
                }
                if(args[0].equalsIgnoreCase("top")){
                    try {
                        Map<String,Double> top = CalculateTop.execute("total");
                        if(!top.isEmpty()) {
                            printTop(sender,top);
                        } else {
                            sender.sendMessage(translateColorCode(
                                    YAML.getMessage().getString("Messages.NoTop")
                            ));
                        }
                    } catch (Exception exc){
                        return false;
                    }
                }
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("top")){
                    if(args[1].equalsIgnoreCase("day")){
                        try {
                            Map<String,Double> top = CalculateTop.execute("day");
                            printTop(sender, top);
                        } catch (Exception exc){
                            return false;
                        }
                    }
                    if(args[1].equalsIgnoreCase("month")){
                        try {
                            Map<String,Double> top = CalculateTop.execute("month");
                            printTop(sender, top);
                        } catch (Exception exc){
                            return false;
                        }
                    }
                    if(args[1].equalsIgnoreCase("year")){
                        try {
                            Map<String,Double> top = CalculateTop.execute("year");
                            printTop(sender, top);
                        } catch (Exception exc){
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
    private static void printTop(CommandSender sender, Map<String,Double> top){
        int i = 0;
        String topFormat = YAML.getMessage().getString("Messages.TopFormat");
        sender.sendMessage(translateColorCode(
                YAML.getMessage().getString("Messages.Calculating")
        ));
        if(top.isEmpty()){
            sender.sendMessage(ChatAPI.translateColorCode(
                    YAML.getMessage().getString("Messages.NoTop")
            ));
        } else {
            for (Map.Entry<String, Double> entry : top.entrySet()) {
                i++;
                String name = entry.getKey();
                String amount = String.valueOf(entry.getValue());
                if (i < 10) {
                    sender.sendMessage(ChatAPI.translateColorCode(topFormat.
                            replaceAll("(?ium)\\{OrdinalNumber\\}", String.valueOf(i)).
                            replaceAll("(?ium)\\{PlayerName\\}", name).
                            replaceAll("(?ium)\\{TotalAmount\\}", amount)
                    ));
                }
            }
        }
    }
    private static void printTop(Player player, Map<String,Double> top){
        player.sendMessage(translateColorCode(
                YAML.getMessage().getString("Messages.Calculating")
        ));
        int i = 0;
        String playerName = player.getName();
        String topFormat = YAML.getMessage().getString("Messages.TopFormat");
        String format = YAML.getMessage().getString("Messages.YourTop");
        if(top.isEmpty()){
            player.sendMessage(ChatAPI.translateColorCode(
                    YAML.getMessage().getString("Messages.NoTop")
            ));
        } else {
            for (Map.Entry<String, Double> entry : top.entrySet()) {
                i++;
                String name = entry.getKey();
                String amount = String.valueOf(entry.getValue());
                if (i < 10) {
                    player.sendMessage(ChatAPI.translateColorCode(topFormat.
                            replaceAll("(?ium)\\{OrdinalNumber\\}", String.valueOf(i)).
                            replaceAll("(?ium)\\{PlayerName\\}", name).
                            replaceAll("(?ium)\\{TotalAmount\\}", amount)
                    ));
                }
                if (name.contains(playerName)) {
                    player.sendMessage(ChatAPI.translateColorCode(format.
                            replaceAll("(?ium)\\{OrdinalNumber\\}", String.valueOf(i)).
                            replaceAll("(?ium)\\{PlayerName\\}", name).
                            replaceAll("(?ium)\\{TotalAmount\\}", amount)
                    ));
                }
            }
        }

    }
}