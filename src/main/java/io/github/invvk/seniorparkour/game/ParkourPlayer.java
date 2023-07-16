package io.github.invvk.seniorparkour.game;

import io.github.invvk.seniorparkour.utils.Tuple;
import io.github.invvk.seniorparkour.utils.scoreboard.IScoreboard;
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
    @Setter private long startTime = System.currentTimeMillis();
    private final Tuple<Integer, Location> checkpoint = new Tuple<>(0, null);
    @Getter @Setter private IScoreboard scoreboard;

    public boolean isValidCheckpoint(int next) {
        return next - checkpoint.v1() == 1;
    }

    public void setCheckpoint(Location checkpointLoc) {
        checkpoint.setValue1(checkpoint.v1() + 1);
        checkpoint.setValue2(checkpointLoc);
    }

}
