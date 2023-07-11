package io.github.invvk.seniorparkour.game;

import lombok.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GamePlayer {

    private final UUID uuid;

    private ParkourGame game;

    @Getter private long time;

    protected void setGame(@NonNull ParkourGame game) {
        this.game = game;
    }

    public Optional<ParkourGame> getGame() {
        return Optional.ofNullable(game);
    }

}
