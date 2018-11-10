package me.takahatashun.payviet.Logger;

import me.takahatashun.payviet.Main;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static me.takahatashun.Library.Chat.ChatAPI.removeColorCode;

public class Log {
    private static final File log = new File(Main.instance.getDataFolder()+"/logs/","napthe.log");
    public boolean writeLog(Player p, String seri, String pin, String cardType, int amount, boolean success, String reason) throws Exception{
        if(!(log.exists())){
            log.getParentFile().mkdir();
            log.createNewFile();
        }
        FileWriter fw = new FileWriter(log,true);
        BufferedWriter bw = new BufferedWriter(fw);
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String name = p.getName();
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        bw.append(
                df.format(dt)).
                append(" | NAME ").append(name).
                append(" | SERI ").append(seri).
                append(" | PIN ").append(pin).
                append(" | TYPE ").append(cardType).
                append(" | AMOUNT ").append(String.valueOf(amount)).
                append(" | SUCCESS ").append(String.valueOf(success)).
                append(" | REASON ").append(removeColorCode(reason));
        bw.newLine();
        bw.close();
        fw.close();
        return true;
    }
    public static File getLog(){
        return log;
    }
}
