package com.redmagic.civilization.util.Event;

import com.redmagic.civilization.util.Team;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinTeamEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Player player;
    @Getter
    private final Team team;
    private boolean cancelled;

    public PlayerJoinTeamEvent(Player player, Team team, boolean cancelled){
        this.cancelled = false;
        this.player = player;
        this.team = team;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlersList(){
        return HANDLERS;
    }
    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
