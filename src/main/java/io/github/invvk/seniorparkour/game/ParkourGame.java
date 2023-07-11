package io.github.invvk.seniorparkour.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParkourGame {

    @Getter
    private final String name;
    private int checkpoint;
    private long startTime;
    private long endTime;

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void endTimer() {
        this.endTime = System.currentTimeMillis();
    }

    public long calcCurrentTimer() {
        return System.currentTimeMillis() - startTime;
    }

    public long calcFinalTime() {
        return this.endTime - this.startTime;
    }

}
