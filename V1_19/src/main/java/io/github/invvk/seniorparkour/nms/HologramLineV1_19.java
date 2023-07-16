package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramLineV1_19 extends AbstractHologramLine {

    private ArmorStand ar;

    public HologramLineV1_19(Location loc, String text) {
        super(loc, text);
    }

    @Override
    public void spawn(Player player) {
        if (id != -1)
            destroy(player);

        var craftPlayer = (CraftPlayer) player;
        Level world = craftPlayer.getHandle().getLevel();

        this.ar = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        ar.setNoGravity(true);
        ar.setCustomName(Component.literal(text));
        ar.setCustomNameVisible(true);
        ar.setInvisible(true);
        ar.setSmall(true);
        ClientboundAddEntityPacket sp = new ClientboundAddEntityPacket(ar);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(ar.getId(), ar.getEntityData().getNonDefaultValues());
        craftPlayer.getHandle().connection.send(sp);
        craftPlayer.getHandle().connection.send(metadata);
        this.id = ar.getId();

    }

    @Override
    public void update(Player player, String text) {
        if (ar == null) return;
        var craftPlayer = (CraftPlayer) player;
        ar.setCustomName(Component.literal(text));

        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(ar.getId(), ar.getEntityData().getNonDefaultValues());
        craftPlayer.getHandle().connection.send(metadata);
        this.text = text;
    }


    @Override
    public void destroy(Player player) {
        if (ar == null) return;
        ClientboundRemoveEntitiesPacket re = new ClientboundRemoveEntitiesPacket(ar.getId());
        var cPlayer = ((CraftPlayer)player);
        cPlayer.getHandle().connection.send(re);
        this.ar = null;
    }

}
