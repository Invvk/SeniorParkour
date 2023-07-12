package io.github.invvk.seniorparkour.utils.holograms;

import io.github.invvk.seniorparkour.utils.Utils;
import io.github.invvk.seniorparkour.utils.holograms.lines.ClientLine;
import io.github.invvk.seniorparkour.utils.holograms.lines.HologramLine;
import io.github.invvk.seniorparkour.utils.holograms.lines.ILine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Hologram {

    private final List<ILine> lines = new ArrayList<>();
    private final List<ClientLine> clientLines = new ArrayList<>();

    @Getter private final String id;
    private final World world;
    private final Location location;

    public void addLine(String line) {
        lines.add(new HologramLine(world, location, Utils.hex(line)));
    }

    public void addClientSideLine(String line) {
        var clientLine = new ClientLine(world, location, Utils.hex(line));
        lines.add(clientLine);
    }

    public void removeLine(int id) {
        lines.remove(id);
    }

    public void spawn() {
        for (var line: lines) {

        }
    }

    public void destroy() {

    }

}
