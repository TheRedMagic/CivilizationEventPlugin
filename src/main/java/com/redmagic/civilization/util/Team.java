package com.redmagic.civilization.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    @Getter
    private int Id;
    @Getter
    @Setter
    private List<UUID> inTeam;
    @Getter
    @Setter
    private int alive;
    @Getter
    @Setter
    private Location teamSpawnLocation;

    @Getter
    @Setter
    private ChatColor teamColor;


    public Team(@NonNull int ID, @NonNull List<UUID> inTeam, @NonNull int alive, @NonNull Location location, @NonNull ChatColor chatColor){
        this.Id = ID;
        this.inTeam = inTeam;
        this.alive = alive;
        this.teamSpawnLocation = location;
        this.teamColor = chatColor;
    }
}
