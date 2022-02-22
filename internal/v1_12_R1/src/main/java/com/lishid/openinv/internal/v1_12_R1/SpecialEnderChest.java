/*
 * Copyright (C) 2011-2018 lishid. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.lishid.openinv.internal.v1_12_R1;

import com.lishid.openinv.internal.ISpecialEnderChest;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SpecialEnderChest extends InventorySubcontainer
        implements IInventory, ISpecialEnderChest {

    private final InventoryEnderChest enderChest;
    private final CraftInventory inventory = new CraftInventory(this);
    private final EntityPlayer owner;
    private boolean playerOnline;
    private NonNullList<ItemStack> items;

    public SpecialEnderChest(final Player player, final Boolean online) {
        super(PlayerDataManager.getHandle(player).getEnderChest().getName(),
                PlayerDataManager.getHandle(player).getEnderChest().hasCustomName(),
                PlayerDataManager.getHandle(player).getEnderChest().getSize());
        this.owner = PlayerDataManager.getHandle(player);
        this.playerOnline = online;
        this.enderChest = owner.getEnderChest();
        this.bukkitOwner = owner.getBukkitEntity();
        this.items = owner.getEnderChest().items;
        this.setItemLists(this);
    }

    @Override
    public Inventory getBukkitInventory() {
        return this.inventory;
    }

    @Override
    public boolean isInUse() {
        return !this.getViewers().isEmpty();
    }

    private void setItemLists(final InventorySubcontainer subcontainer) {
        InventoryEnderChest enderChest = owner.getEnderChest();
        for (int i = 0; i < enderChest.getSize(); ++i) {
            subcontainer.setItem(i, this.items.get(i));
        }
        this.items = subcontainer.items;
    }

    @Override
    public void setPlayerOffline() {
        this.playerOnline = false;
    }

    @Override
    public void setPlayerOnline(final Player player) {
        if (!this.playerOnline) {
            try {
                EntityPlayer nmsPlayer = PlayerDataManager.getHandle(player);
                this.bukkitOwner = nmsPlayer.getBukkitEntity();
                this.setItemLists(nmsPlayer.getEnderChest());
            } catch (Exception ignored) {
            }
            this.playerOnline = true;
        }
    }

    @Override
    public void update() {
        super.update();
        this.enderChest.update();
    }

}
