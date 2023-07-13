package io.github.invvk.seniorparkour.game;

import io.github.invvk.seniorparkour.utils.Tuple;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ParkourPlayer {

    private final UUID uuid;
    private final String gameName;
    @Setter private long time = System.currentTimeMillis();
    private final Tuple<Integer, Location> checkpoint = new Tuple<>(0, null);

    public boolean isValidCheckpoint(int next) {
        return next - checkpoint.getValue1() == 1;
    }

    public void setCheckpoint(Location checkpointLoc) {
        checkpoint.setValue1(checkpoint.getValue1() + 1);
        checkpoint.setValue2(checkpointLoc);
    }

}
