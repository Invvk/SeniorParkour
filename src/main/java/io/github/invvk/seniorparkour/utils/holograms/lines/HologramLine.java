package io.github.invvk.seniorparkour.utils.holograms.lines;

import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

public class HologramLine implements ILine {

    @Getter @Setter
    private String text;

    private ArmorStand stand;
    private final World world;
    private final Location loc;

    public HologramLine(World world, Location loc, String text) {
        this.text = text;
        this.world = world;
        this.loc = loc;
    }

    @Override
    public void spawn() {
        if (this.stand != null) {
            destroy();
        }

        this.stand = world.spawn(loc, ArmorStand.class);

        this.stand.setInvisible(true);
        this.stand.setSmall(true);
        this.stand.setCustomNameVisible(true);
        this.stand.setCustomName(Utils.hex(text));
        this.stand.setMetadata("sp", new FixedMetadataValue(SeniorParkour.getInstance(), true));
    }

    @Override
    public void destroy() {
        if (this.stand != null) {
            this.stand.remove();
            this.stand = null;
        }
    }


}
