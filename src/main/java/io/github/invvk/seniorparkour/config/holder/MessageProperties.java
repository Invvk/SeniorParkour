package io.github.invvk.seniorparkour.config.holder;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.StringListProperty;
import ch.jalu.configme.properties.StringProperty;

import java.util.List;

public class MessageProperties implements SettingsHolder {

    public static final Property<List<String>> COMMAND_HELP
            = new StringListProperty("global.main-help");

    public static final Property<String> INVALID_PARKOUR = new
            StringProperty("global.invalid-parkour-name", "&4(!) &cParkour with name {name} doesn't exists");

    public static final Property<String> NAN = new
            StringProperty("global.not-a-number", "&4(!) &c{input} &fis not a number!");

    public static final Property<String> CREATE_CMD_ARGS = new
            StringProperty("create.args", "&4(!) &cInvalid args: #ffcccb/parkour create <name>");


    public static final Property<String> CREATE_CMD_EXISTS = new
            StringProperty("create.already-exists", "&4(!) &cParkour with name {name} already exists");

    public static final Property<String> CREATE_CMD_CREATED = new
            StringProperty("create.add-new-parkour",
            "&a(!) &fYou've created a new parkour with name &e{name}");

    public static final Property<String> DELETE_CMD_ARGS = new
            StringProperty("delete.args", "&4(!) &cInvalid args: #ffcccb/parkour delete <name>");

    public static final Property<String> DELETE_CMD_REMOVED = new
            StringProperty("delete.delete-succes",
            "&a(!) &fYou've deleted parkour with name &e{name}");

    public static final Property<String> CHECKPOINT_CMD_ARGS = new
            StringProperty("checkpoint.args", "&4(!) &cInvalid args: #ffcccb/parkour checkpoint <name>");

    public static final Property<String> CHECKPOINT_CMD_CREATED = new
            StringProperty("checkpoint.added-checkpoint",
            "&a(!) &fYou've added checkpoint #&e{id}");

    public static final Property<String> END_CMD_ARGS = new
            StringProperty("end.args", "&4(!) &cInvalid args: #ffcccb/parkour end <name>");

    public static final Property<String> END_CMD_CREATED = new
            StringProperty("end.added-end",
            "&a(!) &fYou've added the end checkpoint for &e{name}");

    public static final Property<String> TELEPORT_CMD_ARGS = new
            StringProperty("teleport.args", "&4(!) &cInvalid args: #ffcccb/parkour teleport <name> [checkpoint]");

    public static final Property<String> TELEPORT_CMD_INVALID_CHECKPOINT = new
            StringProperty("teleport.invalid-checkpoint", "&4(!) &cSelected checkpoint doesn't exists!");

    public static final Property<String> TELEPORT_CMD_SUCCESS = new
            StringProperty("teleport.success",
            "&a(!) &fYou've been teleported to &e{id}");
}
