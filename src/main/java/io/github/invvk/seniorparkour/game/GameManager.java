package io.github.invvk.seniorparkour.game;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.database.UserManager;
import io.github.invvk.seniorparkour.database.user.UserParkourGames;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.holograms.Hologram;
import io.github.invvk.seniorparkour.utils.scoreboard.ScoreboardImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class GameManager {

    private final Map<UUID, ParkourPlayer> players = new HashMap<>();
    @Getter
    private final Map<String, ParkourGameData> parkours = new HashMap<>();
    private final UserManager userManager;

    public GameManager() {

        this.reload();
        this.createHolograms();
        userManager = SeniorParkour.inst().getUserManager();

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                SeniorParkour.inst(),
                () -> {
                    var dataManager = SeniorParkour.inst().getStorageManager().getDataManager();
                    for (var data : parkours.values()) {
                        data.getTopPlayers().clear();
                        var fetchTopPlayer = dataManager.getTopPlayers(data.getName(), 50);
                        data.getTopPlayers().addAll(fetchTopPlayer);
                    }
                }, 0,
                SeniorParkour.inst().getConfig().getLong(ConfigProperties.STORAGE_DB_UPDATE_TIMER) * 60 * 20);

        Bukkit.getScheduler().runTaskTimerAsynchronously(SeniorParkour.inst(),
                () -> {
                    for (var data : parkours.values()) {
                        if (data.getTop() == null)
                            continue;
                        for (Player player: Bukkit.getOnlinePlayers()) {
                            Utils.updateTopHologram(player, data);
                        }
                    }
                }, 0, SeniorParkour.inst().getConfig().getLong(ConfigProperties.HOLOGRAM_INTERVAL_SECONDS) * 20);
    }

    public void start(Player player, ParkourGameData data) {
        var uuid = player.getUniqueId();
        if (getParkourPlayer(uuid).isPresent())
            return;

        ParkourPlayer parkourPlayer = new ParkourPlayer(uuid, data.getName());
        var scoreboard = new ScoreboardImpl(player).setHandler(new GameScoreboard(data, parkourPlayer));
        scoreboard.setUpdateInterval(SeniorParkour.inst().getConfig().getInt(ConfigProperties.SCOREBOARD_INTERVAL_TICKS));
        parkourPlayer.setScoreboard(scoreboard);

        scoreboard.activate(SeniorParkour.inst());
        players.put(uuid, parkourPlayer);
        Utils.sendCnfMessage(player, MessageProperties.PLAYER_GAME_STARTED);
    }

    public void end(Player player) {

        var uuid = player.getUniqueId();
        if (getParkourPlayer(uuid).isEmpty()) return;

        var data = players.get(uuid);

        var score = data.getScoreboard();
        if (score != null) score.deactivate();

        long time = data.getStartTime();

        long end = System.currentTimeMillis() - time;

        Utils.sendCnfMessage(player, MessageProperties.PLAYER_GAME_ENDED, Map.
                of("time", Utils.formatTime(end)));

        var userOptional = userManager.getUser(uuid);

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            var parkourData = user.getParkours().getOrDefault(data.getGameName(), UserParkourGames.of(data.getGameName(), end, -1, false, true));
            if (!parkourData.isNewlyAdded()) {
                if (end < parkourData.getTime()) {
                    parkourData.setModified(true);
                    parkourData.setTime(end);
                }
            } else {
                user.getParkours().put(data.getGameName(), parkourData);
            }
        }

        players.remove(uuid);
    }

    public void terminate(Player player) {
        var uuid = player.getUniqueId();
        if (getParkourPlayer(uuid).isEmpty()) return;

        var data = players.get(uuid);
        var score = data.getScoreboard();
        if (score != null)
            score.deactivate();
        Utils.sendCnfMessage(player,
                MessageProperties.PLAYER_GAME_TERMINATED);
        players.remove(uuid);
    }

    public Optional<ParkourPlayer> getParkourPlayer(UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    public void reload() {
        YamlConfiguration config = SeniorParkour.inst().getCnfManager().getParkour().getConfig();
        if (config.get("parkours") == null) return;
        var parkoursSection = config.getConfigurationSection("parkours");
        for (var parkourKey : parkoursSection.getKeys(false)) {
            String name = parkoursSection.getString(parkourKey + ".name");
            Location start = parkoursSection.getLocation(parkourKey + ".start");
            Location end = parkoursSection.getLocation(parkourKey + ".end");
            Location top = parkoursSection.getLocation(parkourKey + ".top");
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
            System.out.println(Objects.requireNonNull(top));

            ParkourGameData data = new ParkourGameData(name);
            data.setStart(start);
            data.setEnd(end);
            data.setTop(top);
            data.getCheckpoints().putAll(checkpointsMap);

            parkours.put(parkourKey, data);
        }
    }

    public void createHolograms() {
        var cnf = SeniorParkour.inst().getCnfManager().getMessages().getConfig();
        var hologramMngr = SeniorParkour.inst().getHologramManager();
        if (!parkours.isEmpty()) {
            for (var parkour : parkours.values()) {
                Hologram.of(parkour.getStart().clone().add(new Vector(0, 0.5, 0)))
                        .addLine(cnf.getString(MessageProperties.HOLOGRAM_PARKOUR_START))
                        .build(hologramMngr);

                Hologram.of(parkour.getEnd().clone().add(new Vector(0, 0.5, 0)))
                        .addLine(cnf.getString(MessageProperties.HOLOGRAM_PARKOUR_END))
                        .build(hologramMngr);

                Utils.createTopHologram(parkour);

                for (var checkpointEntry : parkour.getCheckpoints().entrySet()) {
                    var checkpoint = checkpointEntry.getValue();
                    int id = checkpointEntry.getKey();
                    Hologram.of(checkpoint.clone().add(new Vector(0, 0.5, 0)))
                            .addLine(cnf.getString(MessageProperties.HOLOGRAM_PARKOUR_CHECKPOINT), Map.of("id", String.valueOf(id)))
                            .build(hologramMngr);
                }
            }
        }

    }

    public void save(ParkourGameData data) {
        YamlConfiguration config = SeniorParkour.inst().getCnfManager().getParkour().getConfig();

        String key = "parkours." + data.getName();

        config.set(key + ".name", data.getName());
        config.set(key + ".start", data.getStart());
        config.set(key + ".end", data.getEnd());
        config.set(key + ".top", data.getTop());

        config.set(key + ".checkpoints", null);
        for (var entry : data.getCheckpoints().entrySet()) {
            String chkPointKey = key + ".checkpoints." + entry.getKey();
            config.set(chkPointKey, entry.getValue());
        }
        SeniorParkour.inst().getCnfManager().getParkour().save();
    }

    public void delete(ParkourGameData data) {
        YamlConfiguration config = SeniorParkour.inst().getCnfManager().getParkour().getConfig();
        config.set("parkours." + data.getName(), null);
        SeniorParkour.inst().getCnfManager().getParkour().save();
    }


}
