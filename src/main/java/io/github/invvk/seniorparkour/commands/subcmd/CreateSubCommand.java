package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.config.ParkourGameData;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CreateSubCommand extends AbstractCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            Utils.sendCnfMessage(player, MessageProperties.CREATE_CMD_ARGS);
            return;
        }

        String parkour = args[1].toLowerCase();
        
        var parkours = SeniorParkour.inst().getGameManager().getParkours();

        if (parkours.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.CREATE_CMD_EXISTS,
                    Map.of("name", parkour));
            return;
        }

        // Create
        ParkourGameData parkourData = new ParkourGameData(parkour);
        parkourData.setStart(player.getLocation());

        // Save & Update
        parkours.put(parkour, parkourData);
        SeniorParkour.inst().getGameManager().save(parkourData);

        Utils.createPlate(player.getLocation(), MessageProperties.HOLOGRAM_PARKOUR_START);
        SeniorParkour.inst().getHologramManager().spawnAll();

        Utils.sendCnfMessage(player, MessageProperties.CREATE_CMD_CREATED,
                Map.of("name", parkour));
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return null;
    }
}
