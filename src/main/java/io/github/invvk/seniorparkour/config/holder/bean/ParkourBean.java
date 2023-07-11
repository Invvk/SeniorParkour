package io.github.invvk.seniorparkour.config.holder.bean;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class ParkourBean {

    private String name;
    private Location start;
    private Location end;
    private final Map<Integer, Location> checkpoints = new HashMap<>();


}
