package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckpointSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            Utils.sendCnfMessage(player, MessageProperties.CHECKPOINT_CMD_ARGS);
            return;
        }

        String parkour = args[1].toLowerCase();

        var parkourMap = SeniorParkour.inst().getGameManager().getParkours();

        if (!parkourMap.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.INVALID_PARKOUR,
                    Map.of("name", parkour));
            return;
        }

        // Create
        var parkourData = parkourMap.get(parkour);
        int index = parkourData.getCheckpoints().size() + 1;
        parkourData.getCheckpoints().put(index, player.getLocation());

        // Save & Update
        SeniorParkour.inst().getGameManager().save(parkourData);

        // Create pressure plate
        Utils.createPlate(player.getLocation(), MessageProperties.HOLOGRAM_PARKOUR_CHECKPOINT, index);
        SeniorParkour.inst().getHologramManager().spawnAll();

        Utils.sendCnfMessage(player, MessageProperties.CHECKPOINT_CMD_CREATED,
                Map.of("id", String.valueOf(index)));
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
