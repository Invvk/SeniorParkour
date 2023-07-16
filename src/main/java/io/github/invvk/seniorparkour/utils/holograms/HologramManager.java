package io.github.invvk.seniorparkour.utils.holograms;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private final List<Hologram> holograms = new ArrayList<>();

    public Hologram createHologram(Location loc) {
        Hologram hologram = new Hologram(loc);
        holograms.add(hologram);
        return hologram;
    }

}
