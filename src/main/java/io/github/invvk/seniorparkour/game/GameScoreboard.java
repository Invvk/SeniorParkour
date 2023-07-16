package io.github.invvk.seniorparkour.game;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.scoreboard.Entry;
import io.github.invvk.seniorparkour.utils.scoreboard.IScoreboardHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class GameScoreboard implements IScoreboardHandler {

    private final ParkourGameData data;
    private final ParkourPlayer parkourPlayer;
    private final YamlConfiguration cnf = SeniorParkour.inst().getCnfManager().getMessages().getConfig();

    @Override
    public String getTitle(Player player) {
        var placeholders = Utils.constructPlaceholders(player, data, parkourPlayer);

        String title = cnf.getString(MessageProperties.SCOREBOARD_TITLE, "");

        for (var placeholder: placeholders.entrySet()) {
            title = title.replace("%" + placeholder.getKey() + "%", placeholder.getValue());
        }

        return Utils.hex(title);
    }

    @Override
    public List<Entry> getEntries(Player player) {
        var entries = Utils.constructPlaceholders(player, data, parkourPlayer).entrySet();
        var builder = Entry.builder();
        var lines = cnf.getStringList(MessageProperties.SCOREBOARD_LINES);
        for (var line: lines) {
            for (var entry: entries) {
                line = line.replace("%" + entry.getKey() + "%", entry.getValue());
            }
            builder.next(line);
        }
        return builder.build();
    }

}
