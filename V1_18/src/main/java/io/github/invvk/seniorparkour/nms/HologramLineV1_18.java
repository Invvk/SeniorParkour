package io.github.invvk.seniorparkour.nms;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class HologramLineV1_18 extends AbstractHologramLine {

    public HologramLineV1_18(Location loc, String text) {
        super(loc, text);
    }

    @Override
    public void spawn(Player player) {
        if (id != -1)
            destroy(player);

        var craftPlayer = (CraftPlayer) player;
        Level world = craftPlayer.getHandle().getLevel();

        ArmorStand armorStand = new ArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
        armorStand.setNoGravity(true);
        armorStand.setCustomName(new TextComponent(text));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        ClientboundAddEntityPacket sp = new ClientboundAddEntityPacket(armorStand);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true);
        craftPlayer.getHandle().networkManager.send(sp);
        craftPlayer.getHandle().networkManager.send(metadata);
        this.id = armorStand.getId();

    }

    @Override
    public void update(Player player, String text) {
        var craftPlayer = (CraftPlayer) player;
        Entity e = craftPlayer.getHandle().getLevel().getEntity(id);
        if (e == null) return;
        if (e.getType() != EntityType.ARMOR_STAND) return;

        ArmorStand ar = (ArmorStand) e;
        ar.setCustomName(new TextComponent(text));

        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(ar.getId(), ar.getEntityData(), true);
        craftPlayer.getHandle().networkManager.send(metadata);
        this.text = text;
    }

    @Override
    public void destroy(Player player) {
        if (id == -1) return;
        ClientboundRemoveEntitiesPacket re = new ClientboundRemoveEntitiesPacket(id);
        var cPlayer = ((CraftPlayer)player);
        cPlayer.getHandle().networkManager.send(re);
        this.id = -1;
    }

}
