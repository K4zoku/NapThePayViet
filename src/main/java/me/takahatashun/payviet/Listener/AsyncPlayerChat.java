package me.takahatashun.payviet.Listener;

import com.google.gson.JsonObject;
import me.takahatashun.Library.Chat.ChatAPI;
import me.takahatashun.Library.Dispatch.Command;
import me.takahatashun.payviet.Configurations.YAML;
import me.takahatashun.payviet.Logger.Log;
import net.payviet.PayVietCard;
import net.payviet.PayVietStatusResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncPlayerChat implements Listener {
    private static List<Player> step_1 = new ArrayList<>();
    private static List<Player> step_2 = new ArrayList<>();
    private static HashMap<Player,String> card = new HashMap<>();
    private static HashMap<Player,String> seri = new HashMap<>();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();

        if(isTriggerStep1(p) && !isTriggerStep2(p)){
            String seri = e.getMessage();
            if(e.getMessage().matches("(?ium)(cancel|huy|hủy|exit)")){
                unTriggerStep1(p);
                p.sendMessage(ChatAPI.translateColorCode(
                        YAML.getMessage().getString("Messages.Cancelled")
                ));
                e.setCancelled(true);
            } else {
                AsyncPlayerChat.seri.put(p, seri);
                unTriggerStep1(p);
                triggerStep2(p);
                e.setCancelled(true);
            }
        } else
        if(isTriggerStep2(p) && !isTriggerStep1(p)){
            if(e.getMessage().matches("(?ium)(cancel|huy|hủy|exit)")){
                unTriggerStep2(p);
                p.sendMessage(ChatAPI.translateColorCode(
                        YAML.getMessage().getString("Messages.Cancelled")
                ));
                e.setCancelled(true);
            } else {
                String Pin = e.getMessage();
                String seri = AsyncPlayerChat.seri.get(p);
                String[] icard = card.get(p).split(":", 2);
                p.sendMessage(ChatAPI.translateColorCode(
                        YAML.getMessage().getString("Messages.Serial").replaceAll("(?ium)\\{Serial}", seri)
                ));
                p.sendMessage(ChatAPI.translateColorCode(
                        YAML.getMessage().getString("Messages.Pin").replaceAll("(?ium)\\{Pin}", Pin)
                ));
                p.sendMessage(ChatAPI.translateColorCode(
                        YAML.getMessage().getString("Messages.Handling")
                ));
                int type = Integer.parseInt(icard[0]);
                int amount = Integer.parseInt(icard[1]);
                unTriggerStep2(p);
                e.setCancelled(true);
                Log logger = new Log();
                try {
                    JsonObject jobj = PayVietCard.sendCard(type, seri, Pin, amount);
                    PayVietStatusResponse connInfo = PayVietStatusResponse.getStatusResponse(String.valueOf(jobj.get("status")));
                    if (connInfo.isConnected()) {
                        JsonObject result = jobj.get("result").getAsJsonObject();
                        PayVietStatusResponse resultInfo = PayVietStatusResponse.
                                getStatusResponse(String.valueOf(result.get("status")));
                        if (resultInfo.isSuccessful()) {
                            amount = Integer.parseInt(String.valueOf(result.get("amount")));
                            p.sendMessage(ChatAPI.translateColorCode(
                                    YAML.getMessage().getString("Messages.Success").
                                            replaceAll("(?ium)\\{Amount}", String.valueOf(amount))));
                            logger.writeLog(p, seri, Pin, PayVietCard.getCard(type), amount, true, resultInfo.getMessage());
                            List<String> cmds = YAML.getConfig().getStringList("Card." + amount);
                            for (String cmd : cmds) {
                                cmd = cmd.replaceAll("(?ium)(\\{Player})", p.getName());
                                cmd = cmd.replaceAll("(?ium)(\\{Amount})", String.valueOf(amount));

                                String cmdType = Command.getType(cmd);
                                switch (cmdType) {
                                    case "player":
                                        Command.runPlayerCmd(Command.getCmd(cmd), p);
                                        break;
                                    case "op":
                                        Command.runOpCmd(Command.getCmd(cmd), p);
                                        break;
                                    case "console":
                                        Command.runConsoleCmd(Command.getCmd(cmd));
                                        break;
                                }
                            }
                        } else {
                            p.sendMessage(ChatAPI.translateColorCode(
                                    YAML.getMessage().getString("Messages.Fail")
                            ));
                            p.sendMessage(ChatAPI.translateColorCode(
                                    resultInfo.getMessage()
                            ));
                            logger.writeLog(p, seri, Pin, PayVietCard.getCard(type), amount, false, resultInfo.getMessage());
                        }
                    } else {
                        p.sendMessage(ChatAPI.translateColorCode(
                                YAML.getMessage().getString("Messages.Fail")
                        ));

                        p.sendMessage(ChatAPI.translateColorCode(
                                connInfo.getMessage()
                        ));
                        logger.writeLog(p, seri, Pin, PayVietCard.getCard(type), amount, false, connInfo.getMessage());
                    }
                } catch (Exception ex) {
                    p.sendMessage(ChatAPI.translateColorCode(
                            YAML.getMessage().getString("Messages.SysErr")
                    ));
                    p.sendMessage(ex.getMessage());
                    try {
                        logger.writeLog(p, seri, Pin, PayVietCard.getCard(type), amount, false, YAML.getMessage().getString("Messages.SysErr")+" "+ex.getMessage());
                    } catch (Exception ioex) {
                        ioex.printStackTrace();
                    }
                    ex.printStackTrace();
                }
            }
        }

    }

    public static void triggerStep1(Player p,int type, int amount){
        p.sendMessage(ChatAPI.translateColorCode(YAML.getMessage().getString("Messages.InputSerial")));
        card.put(p,type+":"+amount);
        step_1.add(p);
    }
    public static boolean isTriggerStep1(Player p){
        return step_1.contains(p) && card.containsKey(p);
    }
    public static void unTriggerStep1(Player p){
        step_1.remove(p);
    }
    public static void triggerStep2(Player p){
        p.sendMessage(ChatAPI.translateColorCode(YAML.getMessage().getString("Messages.InputPin")));
        step_2.add(p);
    }
    public static boolean isTriggerStep2(Player p){
        return step_2.contains(p);
    }
    public static void unTriggerStep2(Player p){
        step_2.remove(p);
        seri.remove(p);
        card.remove(p);
    }

}
