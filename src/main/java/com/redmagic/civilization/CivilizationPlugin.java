package com.redmagic.civilization;

import com.redmagic.civilization.commands.TeamCommand;
import com.redmagic.civilization.util.Event.PlayerJoinTeamEvent;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class CivilizationPlugin extends JavaPlugin {

    @Getter
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        // Plugin startup logic



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
