package me.takahatashun.payviet.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.takahatashun.payviet.Listener.AsyncPlayerChat.*;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(isTriggerStep1(p)){
            unTriggerStep1(p);
        }
        if(isTriggerStep2(p)){
            unTriggerStep2(p);
        }
    }
}
