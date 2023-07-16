/*
 * Copyright (C) DeSpam, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Malek <itzghostx3@gmail.com>, December 2020
 */

package io.github.invvk.seniorparkour.utils.scoreboard;


import org.bukkit.entity.Player;

import java.util.List;

public interface IScoreboardHandler {

    String getTitle(Player player);

    List<Entry> getEntries(Player player);

}
