package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class StatsSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            Utils.sendCnfMessage(player, MessageProperties.STATS_USAGE);
            return;
        }

        String name = args[1];
        Player target = Bukkit.getPlayer(name);
        if (target == null || !target.isOnline()) {
            Utils.sendCnfMessage(player, MessageProperties.STATS_OFFLINE_PLAYER, Map.of("name", name));
            return;
        }

        SeniorParkour.inst().getGuiConfigManager().openPlayerData(player, target);

    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return null;
    }
}
