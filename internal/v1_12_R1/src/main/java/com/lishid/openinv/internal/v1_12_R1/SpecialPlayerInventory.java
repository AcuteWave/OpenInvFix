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

import com.google.common.collect.ImmutableList;
import com.lishid.openinv.internal.ISpecialPlayerInventory;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class SpecialPlayerInventory extends PlayerInventory implements ISpecialPlayerInventory {

    private final CraftInventory inventory = new CraftInventory(this);
    private boolean playerOnline;
    private NonNullList<ItemStack> items, armor, extraSlots;

    public SpecialPlayerInventory(final Player bukkitPlayer, final Boolean online) {
        super(PlayerDataManager.getHandle(bukkitPlayer));
        this.playerOnline = online;
        this.items = this.player.inventory.items;
        this.armor = this.player.inventory.armor;
        this.extraSlots = this.player.inventory.extraSlots;
        List<NonNullList<ItemStack>> f = ImmutableList.of(this.items, this.armor, this.extraSlots);
    }

    @Override
    public void setPlayerOnline(final Player player) {
        if (!this.playerOnline) {
            EntityPlayer entityPlayer = PlayerDataManager.getHandle(player);
            entityPlayer.inventory.transaction.addAll(this.transaction);
            this.player = entityPlayer;
            this.player.inventory.a(this);
            this.items = this.player.inventory.items;
            this.armor = this.player.inventory.armor;
            this.extraSlots = this.player.inventory.extraSlots;
            this.playerOnline = true;
        }
    }

    @Override
    public boolean a(final EntityHuman entityhuman) {
        return true;
    }

    @Override
    public Inventory getBukkitInventory() {
        return this.inventory;
    }

    @Override
    public ItemStack getItem(int i) {
        List<ItemStack> list = this.items;

        if (i >= list.size()) {
            i -= list.size();
            list = this.armor;
        } else {
            i = this.getReversedItemSlotNum(i);
        }

        if (i >= list.size()) {
            i -= list.size();
            list = this.extraSlots;
        } else if (list == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }

        if (i >= list.size()) {
            return ItemStack.a;
        }

        return list.get(i);
    }

    @Override
    public String getName() {
        if (this.player.getName().length() > 16) {
            return this.player.getName().substring(0, 16);
        }
        return this.player.getName();
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    private int getReversedArmorSlotNum(final int i) {
        if (i == 0) {
            return 3;
        }
        if (i == 1) {
            return 2;
        }
        if (i == 2) {
            return 1;
        }
        if (i == 3) {
            return 0;
        }
        return i;
    }

    private int getReversedItemSlotNum(final int i) {
        if (i >= 27) {
            return i - 27;
        }
        return i + 9;
    }

    @Override
    public int getSize() {
        return super.getSize() + 4;
    }

    @Override
    public boolean isInUse() {
        return !this.getViewers().isEmpty();
    }

    @Override
    public void setItem(int i, final ItemStack itemstack) {
        List<ItemStack> list = this.items;

        if (i >= list.size()) {
            i -= list.size();
            list = this.armor;
        } else {
            i = this.getReversedItemSlotNum(i);
        }

        if (i >= list.size()) {
            i -= list.size();
            list = this.extraSlots;
        } else if (list == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }

        if (i >= list.size()) {
            this.player.drop(itemstack, true);
            return;
        }

        list.set(i, itemstack);
    }


    @Override
    public void setPlayerOffline() {
        this.playerOnline = false;
    }


    @Override
    public ItemStack splitStack(int i, final int j) {
        NonNullList<ItemStack> list = this.items;

        if (i >= list.size()) {
            i -= list.size();
            list = this.armor;
        } else {
            i = this.getReversedItemSlotNum(i);
        }

        if (i >= list.size()) {
            i -= list.size();
            list = this.extraSlots;
        } else if (list == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }

        if (i >= list.size()) {
            return ItemStack.a;
        }

        return list.get(i).isEmpty() ? ItemStack.a : ContainerUtil.a(list, i, j);
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        NonNullList<ItemStack> list = this.items;

        if (i >= list.size()) {
            i -= list.size();
            list = this.armor;
        } else {
            i = this.getReversedItemSlotNum(i);
        }

        if (i >= list.size()) {
            i -= list.size();
            list = this.extraSlots;
        } else if (list == this.armor) {
            i = this.getReversedArmorSlotNum(i);
        }

        if (i >= list.size()) {
            return ItemStack.a;
        }

        if (!list.get(i).isEmpty()) {
            ItemStack itemstack = list.get(i);

            list.set(i, ItemStack.a);
            return itemstack;
        }

        return ItemStack.a;
    }

}
