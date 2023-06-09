package com.redmagic.civilization;

import com.redmagic.civilization.util.Event.PlayerJoinTeamEvent;
import com.redmagic.civilization.util.HexColor;
import com.redmagic.civilization.util.Team;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamManager {
    @Getter
    private final Map<Integer, Team> TeamMap = new HashMap<>();

    private final CivilizationPlugin plugin;

    public TeamManager(@NonNull CivilizationPlugin plugin){
        this.plugin = plugin;


        getAllDataFromFile();

    }

    private void getAllDataFromFile(){

        for (String s : Objects.requireNonNull(plugin.getTeamsFile().getConfigurationSection("teams")).getKeys(false)){
            int id = Integer.parseInt(s);
            List<UUID> inTeam = new ArrayList<>();

            if (plugin.getTeamsFile().getString("teams." + s + ".UUIDS") == null){
                Bukkit.getLogger().warning("Team " + s + " UUID Data not found!");
                plugin.getServer().shutdown();
                return;
            }

            for (String stringUUID : Objects.requireNonNull(plugin.getTeamsFile().getString("teams." + s + ".UUIDS")).split(",")){
                try {
                   UUID uuid = UUID.fromString(stringUUID);
                   inTeam.add(uuid);
                }   catch (IllegalArgumentException ignored) {

                }


            }

            if (plugin.getTeamsFile().get("teams." + s + ".Alive") == null){
                Bukkit.getLogger().warning("Team " + s + " Alive Data not found!");
                plugin.getServer().shutdown();
                return;
            }

            int alive = plugin.getTeamsFile().getInt("teams." + s + ".Alive");

            String[] locationString = Objects.requireNonNull(plugin.getTeamsFile().getString("teams." + s + ".Location")).split(",");

            if (locationString.length != 4){
                Bukkit.getLogger().warning("Team " + s + " location Data not found!");
                plugin.getServer().shutdown();
                return;
            }

            Location location = new Location(Bukkit.getWorld(locationString[0]),
                    Double.parseDouble(locationString[1]),
                    Double.parseDouble(locationString[2]),
                    Double.parseDouble(locationString[3]));

            if (plugin.getTeamsFile().getString("teams." + s + ".Color") == null){
                Bukkit.getLogger().warning("Team " + s + " Color Data not found!");
                plugin.getServer().shutdown();
                return;
            }

            ChatColor color;
            if (HexColor.isValidHexaCode(Objects.requireNonNull(plugin.getTeamsFile().getString("teams." + s + ".Color")))){
                color = net.md_5.bungee.api.ChatColor.of(Objects.requireNonNull(plugin.getTeamsFile().getString("teams." + s + ".Color")));
            }else {
                try {

                    String[] stringArray = Objects.requireNonNull(plugin.getTeamsFile().getString("teams." + s + ".Color")).split(",");

                    color = net.md_5.bungee.api.ChatColor.of(new Color(Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]), Integer.parseInt(stringArray[0])));
                }catch (IllegalArgumentException e){

                    System.out.println(plugin.getTeamsFile().getString("teams." + s + ".Color"));


                    Bukkit.getLogger().warning("Team " + s + " Color Data not found!");
                    plugin.getServer().shutdown();
                    return;
                }
            }


            TeamMap.put(id, new Team(id, inTeam, alive, location, color));
        }

    }
    public void saveAllDataToFile(){

        FileConfiguration fileConfiguration = plugin.getTeamsFile();

        for (int id : TeamMap.keySet()){


            Team team = TeamMap.get(id);
            fileConfiguration.set("teams." + id + ".Alive", team.getAlive());


            StringBuilder UUIDS = new StringBuilder();
            for (UUID uuid : team.getInTeam()){
                UUIDS.append(uuid).append(",");
            }

            fileConfiguration.set("teams." + id + ".UUIDS", UUIDS.toString());

            fileConfiguration.set("teams." + id + ".Location", Objects.requireNonNull(team.getTeamSpawnLocation().getWorld()).getName() + "," +
                    team.getTeamSpawnLocation().getX() + "," +
                    team.getTeamSpawnLocation().getY() + "," +
                    team.getTeamSpawnLocation().getZ());

            fileConfiguration.set("teams." + id + ".Color", team.getTeamColor().getColor().getBlue() + "," + team.getTeamColor().getColor().getRed() + "," + team.getTeamColor().getColor().getGreen());

        }

        File file = new File(plugin.getDataFolder(), "teams.yml");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void removeTeam(int Id){
        if (TeamMap.containsKey(Id)){
            TeamMap.remove(Id);

            FileConfiguration fileConfiguration = plugin.getTeamsFile();

            fileConfiguration.set("teams." + Id, null);

            File file = new File(plugin.getDataFolder(), "teams.yml");

            try {
                fileConfiguration.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int findId(){

        List<Team> allteams = new ArrayList<>();

        for (int id : TeamMap.keySet()){
            allteams.add(TeamMap.get(id));
        }


        Set<Integer> ids = allteams.stream()
                .map(Team::getId)
                .collect(Collectors.toSet());
        return IntStream.iterate(1, n -> n + 1)
                .filter(n -> ! ids.contains(n))
                .findFirst().getAsInt();
    }

    public void addTeam(@NonNull Team team){
        TeamMap.put(team.getId(), team);
    }

    public boolean addAllPlayersToRandomTeams(){
        Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if(TeamMap.keySet().isEmpty()){
            return false;
        }

        int maxIndexSize = TeamMap.keySet().size()-1;
        final int[] currenltIndexSize = {0};

        int teamId = (int) TeamMap.keySet().toArray()[currenltIndexSize[0]];

        online.forEach(player -> {

            Team team = TeamMap.get(teamId);

            PlayerJoinTeamEvent event = new PlayerJoinTeamEvent(player, team, false);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (player.hasPermission("civilization.team.sort")) {

                    List<UUID> inTeam = team.getInTeam();
                    inTeam.add(player.getUniqueId());
                    team.setInTeam(inTeam);
                    team.setAlive(team.getAlive()+1);
                    player.teleport(team.getTeamSpawnLocation());


                    if (currenltIndexSize[0] >= maxIndexSize) {
                        currenltIndexSize[0] = 0;
                    } else {
                        currenltIndexSize[0]++;
                    }
                }
            }

        });


        return true;
    }
}
