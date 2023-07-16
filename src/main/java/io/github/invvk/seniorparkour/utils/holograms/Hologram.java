package io.github.invvk.seniorparkour.utils.holograms;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.nms.AbstractHologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hologram {

    private final Location base;
    private final List<AbstractHologramLine> lines = new ArrayList<>();
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> spawned = new ArrayList<>();
    public Hologram(Location base) {
        this.base = base;
    }

    public void addLine(String text) {
        int count = Math.max(1, lines.size());
        double height = SeniorParkour.inst().getConfig().getDouble(ConfigProperties.HOLOGRAM_LINE_HEIGHT);

        double y = count * height;
        Location spawnLoc = base.clone();
        spawnLoc.subtract(0, y, 0);

        var line = HologramLineFactory.createPacketLine(spawnLoc, text);
        this.lines.add(line);
    }

    public void spawn(Player player) {
        if (players.contains(player.getUniqueId()))
            return;

        for (var line : lines) {
            if (spawned.contains(line.getUUID()))
                continue;

            line.spawn(player);
            if (!line.isPacket())
                spawned.add(line.getUUID());
        }
        players.add(player.getUniqueId());
    }

    public void destroy(Player player) {
        if (!players.contains(player.getUniqueId()))
            return;
        for (var line: lines) {
            line.destroy(player);
            if (!line.isPacket())
                spawned.remove(line.getUUID());
        }
        players.remove(player.getUniqueId());
    }

}
