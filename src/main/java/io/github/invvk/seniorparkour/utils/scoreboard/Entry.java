/*
 * Copyright (C) DeSpam, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Malek <itzghostx3@gmail.com>, December 2020
 */

package io.github.invvk.seniorparkour.utils.scoreboard;

import io.github.invvk.seniorparkour.utils.Utils;
import net.md_5.bungee.api.ChatColor;

import java.util.LinkedList;
import java.util.List;

public class Entry {

    private String name;
    private int position;

    public Entry(String name, int position) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.position = position;
    }

    public static EntryBuilder builder() {
        return new EntryBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class EntryBuilder {

        private final LinkedList<Entry> entries = new LinkedList<>();

        public EntryBuilder blank() {
            return next("");
        }

        public EntryBuilder next(String string) {
            entries.add(new Entry(adapt(string), entries.size()));
            return this;
        }

        public List<Entry> build() {
            for (Entry entry : entries) {
                entry.setPosition(entries.size() - entry.getPosition());
            }
            return entries;
        }

        private String adapt(String entry) {
            if (entry.length() > 64) entry = entry.substring(0, 63);
            return Utils.hex(entry);
        }

    }


}
