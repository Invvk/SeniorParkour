package io.github.invvk.seniorparkour.config.holder;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.StringProperty;

public class ConfigProperties implements SettingsHolder {

    public static Property<String> START_HOLOGRAM =
            new StringProperty("holograms.messages.start-point", "&e&lStart");

    public static Property<String> END_HOLOGRAM =
            new StringProperty("holograms.messages.end-point", "&a&lEnd");

    public static Property<String> CHECKPOINT_HOLOGRAM =
            new StringProperty("holograms.messages.checkpoint", "&e&lCheckpoint &f#{id}");

}
