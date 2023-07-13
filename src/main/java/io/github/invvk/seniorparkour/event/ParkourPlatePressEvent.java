package io.github.invvk.seniorparkour.event;

import io.github.invvk.seniorparkour.config.ParkourGameData;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class ParkourPlatePressEvent extends PlayerEvent {

    private final Location loc;
    private final PlateType plateType;
    private final ParkourGameData gameData;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ParkourPlatePressEvent(Player who, Location loc, PlateType type, ParkourGameData gameData) {
        super(who);
        this.loc = loc;
        this.plateType = type;
        this.gameData = gameData;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
