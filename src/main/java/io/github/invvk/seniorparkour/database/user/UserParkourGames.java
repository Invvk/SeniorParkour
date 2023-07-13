package io.github.invvk.seniorparkour.database.user;

import lombok.*;

@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class UserParkourGames {

    private final String name;
    private long time;
    private boolean modified;

}
