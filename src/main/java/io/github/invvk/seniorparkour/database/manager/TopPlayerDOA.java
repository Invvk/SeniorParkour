package io.github.invvk.seniorparkour.database.manager;

public record TopPlayerDOA(String name, long time, int position) {

    public static TopPlayerDOA of(String name, long time, int position) {
        return new TopPlayerDOA(name, time, position);
    }

}
