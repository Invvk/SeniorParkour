package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.nms.HologramLineV1_19;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetTopSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        var a = new HologramLineV1_19(player.getLocation(), "Hello " + player.getName());
        a.spawn(player);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return null;
    }
}
