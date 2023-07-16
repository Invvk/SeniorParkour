package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.nms.HologramLineV1_19;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return null;
    }
}
