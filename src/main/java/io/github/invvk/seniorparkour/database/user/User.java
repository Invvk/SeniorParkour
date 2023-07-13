package io.github.invvk.seniorparkour.database.user;

import lombok.Data;

import java.util.*;

@Data
public class User {

    private final UUID uniqueId;
    private final String name;

    private final Map<String, UserParkourGames> parkours = new HashMap<>();

}
