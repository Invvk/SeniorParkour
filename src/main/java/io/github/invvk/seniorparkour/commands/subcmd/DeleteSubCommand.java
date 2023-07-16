package io.github.invvk.seniorparkour.commands.subcmd;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.MessageProperties;
import io.github.invvk.seniorparkour.utils.Tuple;
import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.commands.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DeleteSubCommand extends AbstractCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        if (args.length < 2) {
            Utils.sendCnfMessage(player, MessageProperties.DELETE_CMD_ARGS);
            return;
        }

        String parkour = args[1].toLowerCase();

        var parkourMap = SeniorParkour.inst().getGameManager().getParkours();

        if (!parkourMap.containsKey(parkour)) {
            Utils.sendCnfMessage(player, MessageProperties.INVALID_PARKOUR,
                    Map.of("name", parkour));
            return;
        }
        var parkourData = parkourMap.get(parkour);

        if (args.length == 3) {
            String checkpointStr = args[2];
            int checkpoint = -1;
            try {
                checkpoint = Integer.parseInt(checkpointStr);
            } catch (NumberFormatException ignored) {
                Utils.sendCnfMessage(player, MessageProperties.NAN,
                        Map.of("input", parkour));
                return;
            }

            parkourData.getCheckpoints().forEach((x,y) -> System.out.println(x));
            if (!parkourData.getCheckpoints().containsKey(checkpoint)) {
                Utils.sendCnfMessage(player, MessageProperties.INVALID_CHECKPOINT);
                return;
            }

            List<Tuple<Integer, Location>> toModify = new ArrayList<>();

            for (var chkpoint2Modify: parkourData.getCheckpoints().entrySet()) {
                if (chkpoint2Modify.getKey() <= checkpoint)
                    continue;
                toModify.add(Tuple.of(chkpoint2Modify.getKey(), chkpoint2Modify.getValue()));
            }

            toModify.forEach(x -> {
                parkourData.getCheckpoints().remove(x.v1());
                x.setValue1(x.v1() - 1);
                parkourData.getCheckpoints().put(x.v1(), x.v2());
            });

            SeniorParkour.inst().getGameManager().save(parkourData);
            return;
        }
        Utils.removePlate(parkourData.getStart());
        Utils.removePlate(parkourData.getEnd());
        parkourData.getCheckpoints().values().forEach(Utils::removePlate);

        parkourMap.remove(parkour);

        SeniorParkour.inst().getGameManager().delete(parkourData);

        Utils.sendCnfMessage(player, MessageProperties.DELETE_CMD_REMOVED,
                Map.of("name", parkour));
    }

    @Override
    public List<String> onTab(CommandSender sender, String[] args) {
        if (args.length == 2) {
            final String parkourName = args[1];
            final Set<String> set = SeniorParkour.inst().getGameManager().getParkours().keySet();
            return set.stream().filter(s -> s.startsWith(parkourName))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            final String parkourName = args[1];
            final String indexStr = args[2];
            if (indexStr.isBlank()) return null;

            try {
                Integer.parseInt(indexStr);
                var parkour = SeniorParkour.inst().getGameManager().getParkours().get(parkourName);
                if (parkour == null) {
                    return null;
                }

                return parkour.getCheckpoints().keySet().stream().map(String::valueOf)
                        .filter(s -> s.startsWith(indexStr)).toList();
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }
}
