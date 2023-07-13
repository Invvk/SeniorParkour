package io.github.invvk.seniorparkour.config;

import io.github.invvk.seniorparkour.utils.Utils;
import lombok.Data;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

@Data
public class ParkourGameData {

    private final String name;
    private Location start;
    private Location end;
    private final Map<Integer, Location> checkpoints = new HashMap<>();

    /**
     * determine whether the pressure plate belongs to a parkour game or not.
     * it will return a number: <br>
     *    - 1 = starting point
     *    - 2 = end point
     *    - 3 = checkpoint
     *    - otherwise -1
     * @param location location of the plate
     * @return a number determining the state of the plate
     */
    public int isParkourPlate(Location location) {
        if (isStartPlate(location)) return 1;
        else if (isEndPlate(location)) return 2;

        for (var loc: checkpoints.values())
            if (Utils.compareLoc(location, loc))
                return 3;

        return -1;
    }

    public boolean isStartPlate(Location location) {
        return (start != null && Utils.compareLoc(location, start));
    }

    public boolean isEndPlate(Location location) {
        return (end != null && Utils.compareLoc(location, end));
    }

}
