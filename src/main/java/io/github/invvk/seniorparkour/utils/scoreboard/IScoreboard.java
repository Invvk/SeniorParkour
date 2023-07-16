/*
 * Copyright (C) DeSpam, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Malek <itzghostx3@gmail.com>, December 2020
 */

package io.github.invvk.seniorparkour.utils.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface IScoreboard {

    void activate(JavaPlugin plugin);

    void deactivate();

    boolean isActivated();

    IScoreboardHandler getHandler();

    IScoreboard setHandler(IScoreboardHandler handler);

    long getUpdateInterval();

    IScoreboard setUpdateInterval(long updateInterval);

    Player getHolder();


}
