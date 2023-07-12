package io.github.invvk.seniorparkour.utils.holograms.lines;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

public class ClientLine implements ILine {

    @Getter
    @Setter
    private String text;

    private ArmorStand stand;
    private final World world;
    private final Location loc;

    public ClientLine(World world, Location loc, String text) {
        this.text = text;
        this.world = world;
        this.loc = loc;
    }

    @Override
    public void spawn() {

    }

    @Override
    public void destroy() {

    }
}
