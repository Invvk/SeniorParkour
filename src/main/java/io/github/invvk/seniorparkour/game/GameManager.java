package io.github.invvk.seniorparkour.game;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private final Map<UUID, ParkourPlayer> players = new HashMap<>();
    @Getter
    private final Map<String, ParkourGameData> parkours = new HashMap<>();

    public GameManager() {
        this.reload();
    }

    public void start(Player player, ParkourGameData data) {
        var uuid = player.getUniqueId();
        if (getParkourPlayer(uuid).isPresent())
            return;

        players.put(uuid, new ParkourPlayer(uuid, data.getName()));
        // TODO: setup soreboard

        Utils.sendCnfMessage(player, MessageProperties.PLAYER_GAME_STARTED);
    }

    public void end(Player player) {

        var uuid = player.getUniqueId();
        if (getParkourPlayer(uuid).isEmpty()) return;

        var data = players.get(uuid);
        long time = data.getTime();

        long end = System.currentTimeMillis() - time;
        long minutes =  TimeUnit.MILLISECONDS.toMinutes(end);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(end) % 60;
        long milli = end % 1000;

        Utils.sendCnfMessage(player, MessageProperties.PLAYER_GAME_ENDED, Map.
                of("m", String.format("%02d", minutes), "s", String.format("%02d", seconds), "ms", String.format("%02d", milli/10)));

        players.remove(uuid);
    }

    public Optional<ParkourPlayer> getParkourPlayer(UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    public void reload() {
        YamlConfiguration config = SeniorParkour.inst().getCnfManager().getParkour().getConfig();
        if (config.get("parkours") == null) return;
        var parkoursSection = config.getConfigurationSection("parkours");
        for (var parkourKey: config.getConfigurationSection("parkours").getKeys(false)) {
            String name = parkoursSection.getString(parkourKey + ".name");
            Location start = parkoursSection.getLocation(parkourKey + ".start");
            Location end = parkoursSection.getLocation(parkourKey + ".end");
            Map<Integer, Location> checkpointsMap = new HashMap<>();
            if (parkoursSection.get(parkourKey + ".checkpoints") != null) {
                var checkpoints = parkoursSection.getConfigurationSection(parkourKey + ".checkpoints");
                for (var checkpointKey : checkpoints.getKeys(false)) {
                    try {
                        checkpointsMap.put(Integer.parseInt(checkpointKey), checkpoints.getLocation(checkpointKey));
                    } catch (NumberFormatException ignored) {
                        SeniorParkour.inst().getLogger().warning("skipping key " + checkpointKey + " checkpoint for " + parkourKey);
                    }
                }
            }

            ParkourGameData data = new ParkourGameData(name);
            data.setStart(start);
            data.setEnd(end);
            data.getCheckpoints().putAll(checkpointsMap);

            parkours.put(parkourKey, data);
        }
    }
    public void save(ParkourGameData data) {
        YamlConfiguration config = SeniorParkour.inst().getCnfManager().getParkour().getConfig();

        String key = "parkours." + data.getName();

        config.set(key + ".name", data.getName());
        config.set(key + ".start", data.getStart());
        config.set(key + ".end", data.getEnd());

        for (var entry: data.getCheckpoints().entrySet()) {
            String chkPointKey = key + ".checkpoints." + entry.getKey();
            config.set(chkPointKey, entry.getValue());
        }
        SeniorParkour.inst().getCnfManager().getParkour().save();
    }


}
