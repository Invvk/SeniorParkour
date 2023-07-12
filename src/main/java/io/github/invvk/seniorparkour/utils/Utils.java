package io.github.invvk.seniorparkour.utils;

import ch.jalu.configme.properties.Property;
import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ParkourProperties;
import io.github.invvk.seniorparkour.config.holder.bean.ParkourBeanMap;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
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

    public static void sendCnfMessage(Player player, Property<String> property) {
        player.sendMessage(hex(SeniorParkour.getInstance().getCnfManager().getMessage().getProperty(property)));
    }

    public static void sendCnfMessage(Player player, Property<String> property, Map<String, String> keys) {
        player.sendMessage(StrSubstitutor.replace(hex(SeniorParkour.getInstance().getCnfManager().getMessage().getProperty(property)), keys));
    }

    public static ParkourBeanMap getParkourConfigMap() {
        return SeniorParkour.getInstance().getCnfManager().getParkour().getProperty(ParkourProperties.PARKOURS);
    }

    public static void createPlate(Location location) {
        // TODO: add holograms
        location.getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
    }

    public static void removePlate(Location location) {
        if (location == null) return;
        // TODO: remove holograms
        location.getBlock().setType(Material.AIR);
    }

    public static boolean compareLoc(Location a, Location b) {
        if (a.getWorld() != b.getWorld()) return false;

        return a.distance(b) < 1.0;
    }

}
