package io.github.invvk.seniorparkour.utils.commands;

import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    @Override
    public final boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, String @NonNull [] args) {
        onCommand(commandSender, args);
        return true;
    }

    @Override
    public final List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String s, String @NonNull [] args) {
        return onTab(sender, args);
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    public abstract List<String> onTab(CommandSender sender, String[] args);
}

