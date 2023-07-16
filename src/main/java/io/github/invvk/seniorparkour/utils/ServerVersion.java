package io.github.invvk.seniorparkour.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public enum ServerVersion {

    V1_19(20),
    V1_18(17),
    V1_17(15),
    V1_16(14),
    UNKNOWN(-1);

    @Getter private final int version;
    public static ServerVersion get() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        return find(version.trim());
    }

    private static ServerVersion find(String version) {
        return switch (version) {
            case "v1_19_R3" -> V1_19;
            case "v1_18_R2" -> V1_18;
            case "v1_17_R1" -> V1_17;
            case "v1_16_R3" -> V1_16;
            default -> UNKNOWN;
        };
    }

}
