package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.config.holder.ParkourProperties;
import io.github.invvk.seniorparkour.config.holder.bean.ParkourCnfData;
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

        var parkours = Utils.getParkourConfigMap().getParkours();

        if (parkours.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.CREATE_CMD_EXISTS,
                    Map.of("name", parkour));
            return;
        }

        // Create
        ParkourCnfData parkourData = new ParkourCnfData(parkour);
        parkourData.setStart(player.getLocation());

        // Save & Update
        parkours.put(parkour, parkourData);
        SeniorParkour.getInstance().getCnfManager().getParkour()
                .setProperty(ParkourProperties.PARKOURS,
                        Utils.getParkourConfigMap());


        Utils.createPlate(player.getLocation());

        Utils.sendCnfMessage(player, MessageProperties.CREATE_CMD_CREATED,
                Map.of("name", parkour));
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        return null;
    }
}
