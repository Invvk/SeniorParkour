/*
 * Copyright (C) DeSpam, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Malek <itzghostx3@gmail.com>, December 2020
 */

package io.github.invvk.seniorparkour.utils.scoreboard;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Forked into 1.16.5+
 *
 * @author Chinwe, Invvk
 */
public class ScoreboardImpl implements IScoreboard {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<FakePlayer, Integer> entryCache = new ConcurrentHashMap<>();
    private final Table<String, Integer, FakePlayer> playerCache = HashBasedTable.create();
    private final Table<Team, String, String> teamCache = HashBasedTable.create();
    protected Player holder;
    protected long updateInterval = 10L;
    private boolean activated;
    private IScoreboardHandler handler;
    private BukkitRunnable updateTask;

    public ScoreboardImpl(Player holder) {
        this.holder = holder;
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        scoreboard.registerNewObjective("board", "dummy", "dummy2").setDisplaySlot(DisplaySlot.SIDEBAR);
        objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    @Override
    public void activate(JavaPlugin plugin) {
        if (activated)
            return;

        if (handler == null)
            throw new IllegalArgumentException("Scoreboard handler not set");

        activated = true;
        holder.setScoreboard(scoreboard);
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        };
        updateTask.runTaskTimer(plugin, 0, updateInterval);
    }

    @Override
    public void deactivate() {
        if (!activated) return;
        activated = false;
        if (holder.isOnline())
            synchronized (this) {
                holder.setScoreboard((Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard()));
            }

        teamCache.rowKeySet().forEach(Team::unregister);

        updateTask.cancel();

        this.entryCache.clear();
        this.playerCache.clear();
        this.teamCache.clear();
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public IScoreboardHandler getHandler() {
        return handler;
    }

    @Override
    public IScoreboard setHandler(IScoreboardHandler handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public long getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public ScoreboardImpl setUpdateInterval(long updateInterval) {
        if (activated) {
            Logger.getLogger(this.getClass().getSimpleName())
                    .severe("Prevented updateInterval alteration while update task is active.");
            return this;
        }
        this.updateInterval = updateInterval;
        return this;
    }

    @Override
    public Player getHolder() {
        return holder;
    }

    @SuppressWarnings("deprecation")
    private void update() {

        if (!holder.isOnline()) {
            deactivate();
            return;
        }
        String handlerTitle = handler.getTitle(holder);
        String finalTitle = ChatColor.translateAlternateColorCodes('&',
                handlerTitle != null ? handlerTitle : ChatColor.BOLD.toString());
        ;

        if (!objective.getDisplayName().equals(finalTitle))
            objective.setDisplayName(finalTitle);

        final List<Entry> passed = handler.getEntries(holder);
        final Map<String, Integer> appeared = new HashMap<>();
        final Map<FakePlayer, Integer> current = new HashMap<>();

        if (passed == null)
            return;

        for (Entry entry : passed) {
            String key = entry.getName();
            Integer score = entry.getPosition();
            if (key.length() > 64)
                key = key.substring(0, 63);

            String appearance;
            if (key.length() > 16) {
                appearance = key.substring(16);
            } else {
                appearance = key;
            }
            if (!appeared.containsKey(appearance)) appeared.put(appearance, -1);
            appeared.put(appearance, appeared.get(appearance) + 1);
            final FakePlayer faker = getFakePlayer(key, appeared.get(appearance));
            objective.getScore(faker).setScore(score);
            entryCache.put(faker, score);
            current.put(faker, score);
        }
        appeared.clear();

        entryCache.forEach((fakePlayer, score) -> {
            if (!current.containsKey(fakePlayer)) {
                entryCache.remove(fakePlayer);
                scoreboard.resetScores(fakePlayer.getName());
            }
        });
    }

    @SuppressWarnings("deprecation")
    private FakePlayer getFakePlayer(String text, int offset) {
        final String name = text + StringUtils.repeat(" ", offset);
        FakePlayer fake;
        if (!playerCache.contains(name, offset)) {
            fake = new FakePlayer(name, null, offset);
            playerCache.put(name, offset, fake);
        } else {
            fake = playerCache.get(name, offset);
            fake.setTeam(null);
        }

        if (fake.getTeam() != null)
            fake.getTeam().addPlayer(fake);

        return fake;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
