package io.github.invvk.seniorparkour.utils.holograms;

import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private final List<Hologram> holograms = new ArrayList<>();

    public void createHologram(Hologram hologram) {
        holograms.add(hologram);
    }

    public Hologram getHologram(Location loc) {
        for (Hologram hologram : holograms) {
            if (Utils.compareLoc(hologram.getBase(), loc)) {
                return hologram;
            }
        }
        return null;
    }

    public void removeHologram(Location loc) {
        for (Hologram hologram : holograms) {
            if (Utils.compareLoc(hologram.getBase(), loc)) {
                hologram.destroyAllPlayers();
                this.holograms.remove(hologram);
                break;
            }
        }
    }

    public void spawn(Player player) {
        for (Hologram hologram : holograms) {
            hologram.spawn(player);
        }
    }

    public void spawnAll() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            spawn(player);
        }
    }

    public void destroy(Player pla) {
        for (Hologram hologram : holograms) {
            hologram.destroy(pla);
        }
    }

    public void destroyAll() {
        for (Hologram hologram : holograms) {
            hologram.destroyAllPlayers();
        }
    }

}
