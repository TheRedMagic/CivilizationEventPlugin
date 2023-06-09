package com.redmagic.civilization.commands;

import com.redmagic.civilization.CivilizationPlugin;
import com.redmagic.civilization.util.Command;
import com.redmagic.civilization.util.HexColor;
import com.redmagic.civilization.util.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamCommand extends Command {

    private final CivilizationPlugin plugin;

    public TeamCommand(String command, String[] aliases, String description, String permission) {
        super(command, aliases, description, permission);

        plugin = JavaPlugin.getPlugin(CivilizationPlugin.class);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 3){
            switch (args[0]){
                case "team":
                    try {
                        int id = Integer.parseInt(args[1]);

                        if (!plugin.getTeamManager().getTeamMap().containsKey(id)){
                            if (sender instanceof Player){
                                sender.sendMessage(ChatColor.RED + "Invalid Team ID");
                            }else {
                                plugin.getLogger().info("Invalid Team ID");
                            }
                            return;
                        }

                        Team team = plugin.getTeamManager().getTeamMap().get(id);

                        switch (args[2]){
                            case "setSpawnLocation":
                                if (sender instanceof Player){

                                    Location location = ((Player) sender).getLocation();

                                    team.setTeamSpawnLocation(location);

                                    sender.sendMessage(ChatColor.GREEN + "Team Spawn Location set at : " + location.getX() + " " + location.getY() + " " +  location.getZ());

                                }else {
                                    plugin.getLogger().info("Invalid Console Command.");
                                }
                                break;
                            case "delete":



                                plugin.getTeamManager().removeTeam(team.getId());



                                if (sender instanceof Player){
                                    sender.sendMessage(ChatColor.GREEN + "Team with id : " + team.getId() + " was deleted");
                                }else {
                                    Bukkit.getLogger().info("Team with id : " + team.getId() + " was deleted");
                                }

                                break;
                            case "setColor":

                                if (HexColor.isValidHexaCode(args[3])){
                                    team.setTeamColor(net.md_5.bungee.api.ChatColor.of(args[3]));
                                    if (sender instanceof Player){
                                        sender.sendMessage(ChatColor.GREEN + "Team Color has been update!");
                                    }else {
                                        Bukkit.getLogger().info("Team Color has been update!");
                                    }
                                }else {
                                    try {
                                        net.md_5.bungee.api.ChatColor chatColor = net.md_5.bungee.api.ChatColor.valueOf(args[3]);
                                        team.setTeamColor(chatColor);

                                        if (sender instanceof Player){
                                            sender.sendMessage(ChatColor.GREEN + "Team Color has been update!");
                                        }else {
                                            Bukkit.getLogger().info("Team Color has been update!");
                                        }
                                    }catch (IllegalArgumentException e){
                                        if (sender instanceof Player){
                                            sender.sendMessage(ChatColor.RED + "Invalid Color!");
                                        }else {
                                            Bukkit.getLogger().info("Invalid Color!");
                                        }
                                    }
                                }

                                break;

                        }

                    }catch (NumberFormatException e){
                        if (sender instanceof Player){
                            sender.sendMessage(ChatColor.RED + "Invalid Team ID");
                        }else {
                            plugin.getLogger().info("Invalid Team ID");
                        }
                    }
                    break;
                case "createTeam":

                    if (sender instanceof Player){

                        Random obj = new Random();
                        int rand_num = obj.nextInt(0xffffff + 1);
                        String colorCode = String.format("#%06x", rand_num);

                        Team team = new Team(plugin.getTeamManager().findId(), new ArrayList<>(), 0, ((Player) sender).getLocation(), net.md_5.bungee.api.ChatColor.of(colorCode));

                        plugin.getTeamManager().addTeam(team);

                        sender.sendMessage(ChatColor.GREEN + "Team Created ID : " + team.getId());
                    }else {
                        Bukkit.getLogger().info("Invalid Console Command.");
                    }

                    break;
                case "start":
                    boolean done = plugin.getTeamManager().addAllPlayersToRandomTeams();

                    if (done){

                        if (sender instanceof Player){
                            sender.sendMessage(ChatColor.GREEN + "Civilization event started");
                        }else {
                            Bukkit.getLogger().info("Civilization event started");
                        }

                    }else {
                        if (sender instanceof Player){
                            sender.sendMessage(ChatColor.RED + "There is no valid team");
                        }else {
                            Bukkit.getLogger().info("There is no valid team");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {

        List<String> TabCompleteList = new ArrayList<>();

        if (args.length == 1){
            TabCompleteList.add("team");
            TabCompleteList.add("createTeam");
            TabCompleteList.add("start");
        }
        if (args.length == 2){
            for (int id : plugin.getTeamManager().getTeamMap().keySet()){
                TabCompleteList.add(String.valueOf(id));
            }
        }
        if (args.length == 3){
            if (sender instanceof Player) {
                TabCompleteList.add("setSpawnLocation");
            }

            TabCompleteList.add("delete");
            TabCompleteList.add("setColor");
        }

        return TabCompleteList;
    }
}
