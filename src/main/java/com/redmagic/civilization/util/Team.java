package com.redmagic.civilization.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Getter
    private final org.bukkit.scoreboard.Team team;

    public Team(int ID, @NonNull List<UUID> inTeam, int alive, @NonNull Location location, @NonNull ChatColor chatColor, @NonNull org.bukkit.scoreboard.Team team) {
        this.Id = ID;
        this.inTeam = inTeam;
        this.alive = alive;
        this.teamSpawnLocation = location;
        this.teamColor = chatColor;
        this.team = team;

        team.setPrefix(chatColor + "");


    }
}
