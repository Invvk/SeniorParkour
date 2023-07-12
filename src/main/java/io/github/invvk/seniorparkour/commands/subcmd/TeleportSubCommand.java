package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeleportSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            Utils.sendCnfMessage(player, MessageProperties.TELEPORT_CMD_ARGS);
            return;
        }

        String parkour = args[1].toLowerCase();

        var parkourMap = Utils.getParkourConfigMap().getParkours();

        if (!parkourMap.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.INVALID_PARKOUR,
                    Map.of("name", parkour));
            return;
        }

        // Create
        var parkourData = parkourMap.get(parkour);
        int checkpoint = 0;

        if (args.length == 3) {
            try {
                checkpoint = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {
                Utils.sendCnfMessage(player, MessageProperties.NAN, Map.of("input", args[2]));
                return;
            }
        }

        Location checkpointLoc = parkourData.getCheckpoints().get(checkpoint);
        if (checkpointLoc == null) {
            Utils.sendCnfMessage(player, MessageProperties.TELEPORT_CMD_INVALID_CHECKPOINT);
            return;
        }

        player.teleport(checkpointLoc);
        player.setVelocity(new Vector(0, 0 , 0));

        Utils.sendCnfMessage(player, MessageProperties.TELEPORT_CMD_SUCCESS,
                Map.of("id", checkpoint == 0
                        ? "start" : String.valueOf(checkpoint)));
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 2) {
            final String parkourName = args[1];
            final Set<String> set = Utils.getParkourConfigMap().getParkours().keySet();
            return set.stream().filter(s -> s.startsWith(parkourName))
                    .toList();
        } else if (args.length == 3) {
            final String parkourName = args[1];
            final String indexStr = args[2];

            try {
                Integer.parseInt(indexStr);
                var parkour = Utils.getParkourConfigMap().getParkours().get(parkourName);
                if (parkour == null) {
                    return null;
                }

                return parkour.getCheckpoints().keySet().stream().map(String::valueOf)
                        .filter(s -> s.startsWith(indexStr)).toList();
            } catch (NumberFormatException ignored) {
                return List.of(Utils.hex("&cCheckpoint should be a number"));
            }
        }
        return null;
    }
}
