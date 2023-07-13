package io.github.invvk.seniorparkour.commands;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.commands.subcmd.*;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.commands.MainCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class ParkourMainCommand extends MainCommand {

    public ParkourMainCommand() {
        super(Map.of("create", new CreateSubCommand(),
                "checkpoint", new CheckpointSubCommand(),
                "end", new EndSubCommand(),
                "delete", new DeleteSubCommand(),
                "teleport", new TeleportSubCommand(),
                "top", new TopSubCommand(),
                "info", new InfoSubCommand(),
                "stats", new StatsSubCommand(),
                "settop", new SetTopSubCommand(),
                "deltop", new DeleteTopSubCommand()));
    }

    @Override
    public List<String> help(CommandSender sender) {
        return SeniorParkour.inst().getCnfManager()
                .getMessages().getConfig()
                .getStringList(MessageProperties.COMMAND_HELP);
    }
}
