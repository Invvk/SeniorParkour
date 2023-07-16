package io.github.invvk.seniorparkour.utils;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.game.ParkourPlayer;
import io.github.invvk.seniorparkour.utils.holograms.Hologram;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * <a href="https://www.spigotmc.org/threads/hex-color-code-translate.449748/">Credits</a>
     */
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendCnfMessage(Player player, String property) {
        player.sendMessage(hex(SeniorParkour.inst().getCnfManager().getMessages().getConfig().getString(property)));
    }

    public static void sendCnfMessage(Player player, String property, Map<String, String> keys) {
        player.sendMessage(StrSubstitutor.replace(hex(SeniorParkour.inst().getCnfManager().getMessages().getConfig().getString(property)), keys));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void createPlate(Location location, String prop) {
        var manager = SeniorParkour.inst().getHologramManager();

        Hologram.of(location.add(new Vector(0, 0.5, 0)))
                        .addLine(SeniorParkour.inst().getCnfManager()
                                .getMessages()
                                .getConfig().getString(prop))
                                .build(manager);

        Material mt = Material.getMaterial(SeniorParkour.inst().getConfig().getString(ConfigProperties.PARKOUR_PLATE_TYPE, "HEAVY_WEIGHTED_PRESSURE_PLATE"));
        location.getBlock().setType(mt);
    }

    public static void createPlate(Location location, String prop, int id) {
        var manager = SeniorParkour.inst().getHologramManager();

        Hologram.of(location.add(new Vector(0, 0.5, 0)))
                .addLine(SeniorParkour.inst()
                        .getCnfManager()
                        .getMessages().getConfig().getString(prop), Map.of("id", String.valueOf(id)))
                .build(manager);


        Material mt = Material.getMaterial(SeniorParkour.inst().getConfig().getString(ConfigProperties.PARKOUR_PLATE_TYPE, "HEAVY_WEIGHTED_PRESSURE_PLATE"));
        location.getBlock().setType(mt);
    }

    public static void removePlate(Location location) {
        if (location == null) return;
        SeniorParkour.inst().getHologramManager().removeHologram(location);
        location.getBlock().setType(Material.AIR);
    }

    public static boolean compareLoc(Location a, Location b) {
        if (a.getWorld() != b.getWorld()) return false;

        return a.distance(b) < 1.0;
    }

    public static void openTopInventory(Player player, ParkourGameData data) {
        var a = RyseInventory.builder()
                .title("Top 50 players")
                .rows(6)
                .disableUpdateTask()
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {
                        Pagination pagination = contents.pagination();
                        pagination.setItemsPerPage(10);
                        pagination.iterator(SlotIterator.builder().startPosition(0).build());
                        for (var topPlayer: data.getTopPlayers()) {
                            ItemStackBuilder item = new ItemStackBuilder(topPlayer.name());
                            item.withDisplayName("#" + topPlayer.position() + " - " + topPlayer.name());
                            item.withLore("time - " + topPlayer.time());
                            pagination.addItem(item.build());
                        }
                        int previousPage = pagination.page() - 1;

                        contents.set(5, 3, IntelligentItem
                                .of(new ItemStackBuilder(new ItemStack(Material.ARROW))
                                                .withDisplayName(pagination.isFirst()
                                                        ? "§c§oThis is the first page"
                                                        : "§ePage §8⇒ §9" + previousPage).build()
                                        , event -> {
                                            if (pagination.isFirst()) {
                                                player.sendMessage("§c§oYou are already on the first page.");
                                                return;
                                            }

                                            RyseInventory currentInventory = pagination.inventory();
                                            currentInventory.open(player, pagination.previous().page());
                                        }));


                        int page = pagination.page() + 1;
                        contents.set(5, 5, IntelligentItem.of(new ItemStackBuilder(new ItemStack(Material.ARROW))
                                .withDisplayName(!pagination.isLast()
                                        ? "§ePage §8⇒ §9" + page :
                                        "§c§oThis is the last page").build(), event -> {
                            if (pagination.isLast()) {
                                player.sendMessage("§c§oYou are already on the last page.");
                                return;
                            }

                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.next().page());
                        }));

                    }
                })
                .build(SeniorParkour.inst());
        a.open(player);
    }

    public static String formatTime(long end) {
        long minutes =  TimeUnit.MILLISECONDS.toMinutes(end) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(end) % 60;
        long milli = end % 1000;
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" + String.format("%02d", milli/10);
    }

    public static void createTopHologram(ParkourGameData data) {
        var h = Hologram.of(data.getTop());
        for (String lines: SeniorParkour.inst().getCnfManager().getMessages().getConfig().getStringList(MessageProperties.HOLOGRAM_PARKOUR_TOP)) {
            h.addLine(lines);
        }
        h.build(SeniorParkour.inst().getHologramManager()).spawnAll();
    }

    public static void updateTopHologram(Player player, ParkourGameData data) {
        Hologram h = SeniorParkour.inst().getHologramManager().getHologram(data.getTop());
        Map<String, String> map = new HashMap<>();
        var top = data.getTopPlayers();
        int i = 0;
        for (i = 0; i < Math.min(3, top.size()); i++) {
            var topPlayer = top.get(i);
            map.put("id_" + topPlayer.position(), String.valueOf(topPlayer.position()));
            map.put("name_" + topPlayer.position(), topPlayer.name());
            map.put("time_" + topPlayer.position(), Utils.formatTime(topPlayer.time()));
        }

        for (int j = i; j < 3; j++ ) {
            map.put("id_" + (j+1), String.valueOf(j+1));
            map.put("name_" + (j+1), "loading..");
            map.put("time_" + (j+1), "loading..");
        }

        map.put("parkourName", data.getName());

        var optional = SeniorParkour.inst().getUserManager().getUser(player.getUniqueId());
        if (optional.isPresent()) {
            var user = optional.get();
            var userParkour = user.getParkours().get(data.getName());
            map.put("userPosition", userParkour == null ? "unranked" : String.valueOf(userParkour.getPosition()));
            map.put("time", userParkour == null ? "99:99:99" : Utils.formatTime(userParkour.getTime()));
        }

        h.updateAll(player, map);
    }

    public static Map<String,String> constructPlaceholders(Player player, ParkourGameData data, ParkourPlayer parkourPlayer) {
        Map<String, String> map = new HashMap<>();
        map.put("parkourName", data.getName());
        var optional = SeniorParkour.inst().getUserManager().getUser(player.getUniqueId());
        if (optional.isPresent()) {
            var user = optional.get();
            var userParkour = user.getParkours().get(data.getName());
            map.put("position", userParkour == null ? "unranked" : String.valueOf(userParkour.getPosition()));
            map.put("time", userParkour == null ? "99:99:99" : Utils.formatTime(System.currentTimeMillis() - parkourPlayer.getStartTime()));
        }
        return map;
    }

    public static String replace(String text, Map<String, String> placeholders) {
        for (var entry: placeholders.entrySet()) {
            text = text.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return hex(text);
    }
}
