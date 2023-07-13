package io.github.invvk.seniorparkour.database;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

    private final UUID uniqueId;
    private final String name;


}
