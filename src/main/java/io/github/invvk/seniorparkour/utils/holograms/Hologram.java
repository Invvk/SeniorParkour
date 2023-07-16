package io.github.invvk.seniorparkour.utils.holograms;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.nms.AbstractHologramLine;
import io.github.invvk.seniorparkour.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Hologram {

    @Getter private final Location base;
    private final List<AbstractHologramLine> lines = new ArrayList<>();
    @Getter private final List<UUID> players = new ArrayList<>();
    public Hologram(Location base) {
        this.base = base;
    }

    public static HologramBuilder of(Location base) {
        return new HologramBuilder(base);
    }

    public void addLine(String text) {
        int count = lines.size() + 1;
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
            line.spawn(player);
        }

        players.add(player.getUniqueId());
    }

    public void spawnAll() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            spawn(player);
        }
    }

    public void updateAll(Player player, Map<String, String> placeholders) {
        if (!players.contains(player.getUniqueId())) return;
        for (var line: lines) {
                String txt = line.getText();
                for (var entry: placeholders.entrySet()) {
                    txt = txt.replace("%" + entry.getKey() + "%", entry.getValue());
                }
                line.update(player, txt);
            }
    }

    public void destroy(Player player) {
        if (!players.contains(player.getUniqueId()))
            return;

        for (var line: lines) {
            line.destroy(player);
        }
        players.remove(player.getUniqueId());
    }

    public void destroyAllPlayers() {
        for (UUID uuid : players) {
            var player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;

            for (var line: lines) {
                line.destroy(player);
            }
        }
        players.clear();
    }

    public static class HologramBuilder {
        private final Hologram hologram;
        public HologramBuilder(Location base) {
            this.hologram = new Hologram(base);
        }

        public HologramBuilder addLine(String text) {
            this.hologram.addLine(Utils.hex(text));
            return this;
        }

        public HologramBuilder addLine(String text, Map<String, String> placeholders) {
            for (var entry: placeholders.entrySet()) {
                text = text.replace("%" + entry.getKey() + "%", entry.getValue());
            }
            return addLine(text);
        }

        public Hologram build(HologramManager manager) {
            manager.createHologram(hologram);
            return hologram;
        }

    }

}
