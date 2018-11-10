package me.takahatashun.Library.Dispatch;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.takahatashun.payviet.Placeholders.Placeholders.Bonus;

public class Command {
    public static void runConsoleCmd(String input){
        ConsoleCommandSender ccs = Bukkit.getConsoleSender();
        Bukkit.dispatchCommand(ccs,Bonus(input));
    }
    public static void runOpCmd(String input, Player p){
        if(p.isOp()){
            p.performCommand(Bonus(input));
        } else {
            p.setOp(true);
            p.performCommand(Bonus(input));
            p.setOp(false);
        }
    }
    public static void runPlayerCmd(String input, Player p){
        p.performCommand(Bonus(input));
    }
    public static String getType(String input){
        if(input.matches("(?iu)^(player|op|console):(.*)$"))
            return input.split(":", 2)[0];
        else return "player";
    }
    public static String getCmd(String input){
        if(input.matches("(?iu)^(player|op|console):(.*)$"))
            return input.split(":",2)[1];
        else return input;
    }
}
