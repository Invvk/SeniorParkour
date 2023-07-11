package io.github.invvk.seniorparkour.utils;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta mt;

    public ItemStackBuilder(@NonNull ItemStack stack) {
        this.itemStack = stack.clone();
        this.mt = stack.getItemMeta();
    }

    @SuppressWarnings("deprecation")
    public ItemStackBuilder(String skullOwner) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mt = (SkullMeta) this.itemStack.getItemMeta();
        mt.setOwner(skullOwner);
        this.itemStack.setItemMeta(mt);

        this.mt = mt;
    }

    public ItemStackBuilder withDisplayName(String displayName) {
        mt.setDisplayName(Utils.hex(displayName));
        return this;
    }

    public ItemStackBuilder withLore(String... lore) {
        mt.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(mt);
        return this.itemStack;
    }

}
