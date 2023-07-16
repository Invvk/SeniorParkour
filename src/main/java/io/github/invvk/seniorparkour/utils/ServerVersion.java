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
        return find(Integer.valueOf(version.trim()));
    }

    private static ServerVersion find(int version) {
        for (var ver: values()) {
            if (ver.getVersion() == version) return ver;
        }
        return UNKNOWN;
    }

}
