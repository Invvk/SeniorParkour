package io.github.invvk.seniorparkour.database.manager;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public record TopPlayerDOA(String name, long time, int position) {}
