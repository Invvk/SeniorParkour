package io.github.invvk.seniorparkour.utils.holograms;

import io.github.invvk.seniorparkour.nms.*;
import io.github.invvk.seniorparkour.utils.ServerVersion;
import org.bukkit.Location;

public class HologramLineFactory {

    private static final ServerVersion ver = ServerVersion.get();

    public static AbstractHologramLine createPacketLine(Location loc, String text) {
        return switch (ver) {
            case V1_16 -> new HologramLineV1_16(loc, text);
            case V1_17 -> new HologramLineV1_17(loc, text);
            case V1_18 -> new HologramLineV1_18(loc, text);
            case V1_19 -> new HologramLineV1_19(loc, text);
            default -> null;
        };
    }

}
