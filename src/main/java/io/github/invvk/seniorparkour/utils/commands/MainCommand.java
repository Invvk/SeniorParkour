package io.github.invvk.seniorparkour.utils.commands;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.github.invvk.seniorparkour.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MainCommand extends AbstractCommand {

    private final ImmutableMap<String, AbstractCommand> subCommands;

    public MainCommand(Map<String, AbstractCommand> subCmds) {
        this.subCommands = ImmutableMap.<String, AbstractCommand>builder()
                .putAll(subCmds)
                .build();
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length != 0) {
            var subCmd = subCommands.get(args[0]);
            if (subCmd != null) {
                subCmd.onCommand(sender, args);
                return;
            }
        }
        help(sender).forEach(line -> sender.sendMessage(Utils.hex(line)));
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 1) {
            final String command = args[0];
            if (command.isEmpty())
                return Collections.emptyList();

            final ImmutableSet<String> set = this.subCommands.keySet();

            return set.stream().filter(s -> s.startsWith(command))
                    .collect(Collectors.toList());
        }
        var match = this.subCommands.get(args[0]);
        return match != null ? match.onTab(sender, args) : null;
    }

    public abstract List<String> help(CommandSender sender);
}
