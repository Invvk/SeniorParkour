package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramV1_17 implements IHologram {
    @Override
    public int spawn(Player player, String message) {
        var craftPlayer = (CraftPlayer) player;
        Level world = craftPlayer.getHandle().getLevel();

        Location loc = player.getLocation();

        ArmorStand armorStand = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        armorStand.setNoGravity(true);
        armorStand.setCustomName(new TextComponent("Hello There UwU " + player.getName()));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        ClientboundAddEntityPacket sp = new ClientboundAddEntityPacket(armorStand);
        craftPlayer.getHandle().networkManager.send(sp);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true);
        craftPlayer.getHandle().networkManager.send(metadata);
        return armorStand.getId();
    }

    @Override
    public void destroy(Player player, int... ids) {
        ClientboundRemoveEntitiesPacket re = new ClientboundRemoveEntitiesPacket(ids);
        var cPlayer = ((CraftPlayer)player);
        cPlayer.getHandle().networkManager.send(re);
    }
}
