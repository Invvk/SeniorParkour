package io.github.invvk.seniorparkour.config.holder.bean;

import io.github.invvk.seniorparkour.utils.Utils;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

@Data
public class ParkourCnfData {

    private final String name;
    private Location start;
    private Location end;
    private final Map<Integer, Location> checkpoints = new HashMap<>();

    public boolean isParkourPlate(Location location) {
        if ((start != null && Utils.compareLoc(location, start))
                || (end != null && Utils.compareLoc(location, end))) return true;

        for (var loc: checkpoints.values())
            if (Utils.compareLoc(location, loc))
                return true;

        return false;
    }

}
