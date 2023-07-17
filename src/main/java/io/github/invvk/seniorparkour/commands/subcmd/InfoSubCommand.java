package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.nms.HologramLineV1_19;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InfoSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            Utils.sendCnfMessage(player, MessageProperties.INFO_ARGS);
            return;
        }

        String parkour = args[1].toLowerCase();

        var parkourMap = SeniorParkour.inst().getGameManager().getParkours();

        if (!parkourMap.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.INVALID_PARKOUR,
                    Map.of("name", parkour));
            return;
        }

        // Update
        var parkourData = parkourMap.get(parkour);
        SeniorParkour.inst().getGuiConfigManager().openParkourInfo(player, parkourData);
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 2) {
            final String parkourName = args[1];
            final Set<String> set = SeniorParkour.inst().getGameManager().getParkours().keySet();
            return set.stream().filter(s -> s.startsWith(parkourName))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
