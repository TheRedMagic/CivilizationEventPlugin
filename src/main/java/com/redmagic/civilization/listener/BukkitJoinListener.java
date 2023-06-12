package com.redmagic.civilization.listener;

import com.redmagic.civilization.CivilizationPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitJoinListener implements Listener {

    private final CivilizationPlugin plugin;

    public BukkitJoinListener(CivilizationPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        e.getPlayer().setScoreboard(plugin.getScoreboard());

        if(plugin.getTeamManager().UUIDtoTeam(e.getPlayer().getUniqueId()) != null){
            plugin.getTeamManager().UUIDtoTeam(e.getPlayer().getUniqueId()).getTeam().addPlayer(e.getPlayer());
        }
    }

}
