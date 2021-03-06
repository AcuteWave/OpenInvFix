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
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

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
        this.setItemLists(this, items);
    }

    @Override
    public Inventory getBukkitInventory() {
        return this.inventory;
    }

    @Override
    public boolean isInUse() {
        return !this.getViewers().isEmpty();
    }

    private void setItemLists(final InventorySubcontainer subcontainer, final List<ItemStack> list) {
        for (int i = 0; i < enderChest.getSize(); i++) {
            subcontainer.items.set(i, list.get(i));
        }
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
                this.setItemLists(nmsPlayer.getEnderChest(), items);
            } catch (Exception ignored) {
            }
            this.playerOnline = true;
        }
    }

    @Override
    public void update() {
        super.update();
    }

    public List<ItemStack> getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        this.owner.getEnderChest().onOpen(who);
    }

    public void onClose(CraftHumanEntity who) {
        this.owner.getEnderChest().onClose(who);
    }

    public List<HumanEntity> getViewers() {
        return this.owner.getEnderChest().getViewers();
    }

    public InventoryHolder getOwner() {
        return this.owner.getEnderChest().getOwner();
    }

    public Location getLocation() {
        return null;
    }

    public void a(IInventoryListener iinventorylistener) {
        this.owner.getEnderChest().a(iinventorylistener);
    }

    public void b(IInventoryListener iinventorylistener) {
        this.owner.getEnderChest().b(iinventorylistener);
    }

    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.a;
    }

    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = ContainerUtil.a(this.items, i, j);
        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    public ItemStack a(ItemStack itemstack) {
        ItemStack itemstack1 = itemstack.cloneItemStack();

        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack2 = this.getItem(i);
            if (itemstack2.isEmpty()) {
                this.setItem(i, itemstack1);
                this.update();
                return ItemStack.a;
            }

            if (ItemStack.c(itemstack2, itemstack1)) {
                int j = Math.min(this.getMaxStackSize(), itemstack2.getMaxStackSize());
                int k = Math.min(itemstack1.getCount(), j - itemstack2.getCount());
                if (k > 0) {
                    itemstack2.add(k);
                    itemstack1.subtract(k);
                    if (itemstack1.isEmpty()) {
                        this.update();
                        return ItemStack.a;
                    }
                }
            }
        }

        if (itemstack1.getCount() != itemstack.getCount()) {
            this.update();
        }

        return itemstack1;
    }

    public ItemStack splitWithoutUpdate(int i) {
        ItemStack itemstack = this.items.get(i);
        if (itemstack.isEmpty()) {
            return ItemStack.a;
        } else {
            this.items.set(i, ItemStack.a);
            return itemstack;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        this.update();
    }

    public int getSize() {
        return this.owner.getEnderChest().getSize();
    }

    public boolean hasCustomName() {
        return false;
    }

    public int getMaxStackSize() {
        return 64;
    }

    public void setMaxStackSize(int i) {
        this.owner.getEnderChest().setMaxStackSize(i);
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public void startOpen(EntityHuman entityhuman) {
    }

    public void closeContainer(EntityHuman entityhuman) {
    }

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public int getProperty(int i) {
        return 0;
    }

    public void setProperty(int i, int j) {
    }

    public int h() {
        return 0;
    }

    public void clear() {
        this.items.clear();
    }

}
