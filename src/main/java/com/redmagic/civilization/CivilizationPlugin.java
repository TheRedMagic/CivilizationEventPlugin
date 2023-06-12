package com.redmagic.civilization;

import com.redmagic.civilization.commands.TeamCommand;
import com.redmagic.civilization.managers.TeamManager;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.util.Objects;

public final class CivilizationPlugin extends JavaPlugin {

    @Getter
    private TeamManager teamManager;

    @Getter
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        // Plugin startup logic


        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = Objects.requireNonNull(manager).getNewScoreboard();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        teamManager = new TeamManager(this);



        new TeamCommand("civilization", new String[]{"c"}, "Civilization Event Command", "civilization.command.use");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getTeamManager().saveAllDataToFile();

    }

    public @NonNull FileConfiguration getTeamsFile(){
        File file = new File(getDataFolder() + "/teams.yml");
        if (!file.exists()){

            System.out.println("Creating File");

            saveResource("teams.yml", false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
