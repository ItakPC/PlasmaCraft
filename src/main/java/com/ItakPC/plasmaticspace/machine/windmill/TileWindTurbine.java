package com.ItakPC.plasmaticspace.machine.windmill;

import com.ItakPC.plasmaticspace.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileWindTurbine extends TileEntity implements ISidedInventory {

    private ItemStack[] slots = new ItemStack[1];

    /** Used To Render The Rotor */
    public float rotation = 0;

    /** Maximum Power The Machine Can Store */
    public int maxPower = 128000;

    /** Power The Machine Store Now */
    public float power = 0;

    /** Power Generated Per Tick*/
    public float powerPerTick = 0;

    /** Updates Entity Each Tick */

    public void updateEntity() {
        if (this.getWorldObj().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) > 6) {

            if(this.worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord) == true) {
                powerPerTick = this.yCoord*(255/128);
                rotation++;
            }else{
                powerPerTick = 0;
                rotation = 0;
            }

            power += powerPerTick;
            if (power > maxPower) power = maxPower;
        }
    }

    /** Used To Display The Power Stored */
    public int getPowerScaled(int scaled) {
        return  (int) this.power * scaled / this.maxPower;
    }

    /** Used To Read From Named Binary Tag - power, items in inv */
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagList list = nbt.getTagList("slots", 10);
        this.slots = new ItemStack[getSizeInventory()];

        for(int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound item = list.getCompoundTagAt(i);
            byte b = item.getByte("item");

            if(b >= 0 && b < this.slots.length) {
                this.slots[b] = ItemStack.loadItemStackFromNBT(item);
            }
        }

        this.power = nbt.getInteger("power");

        if(nbt.hasKey("customName")) {
            this.setInventoryName(nbt.getString("customName"));
        }
    }

    /** Used To Write To Named Binary Tag - power, items in inv */
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setFloat("power", this.power);

        NBTTagList list = new NBTTagList();

        for(int i = 0; i < this.slots.length; i++) {
            if(this.slots[i] != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("item", (byte)i);
                this.slots[i].writeToNBT(item);
                list.appendTag(item);
            }
        }

        nbt.setTag("slots", list);

        if(this.hasCustomInventoryName()) {
            nbt.setString("customName", this.getInventoryName());
        }

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        return new int[0];
    }

    /** WIP Used To Insert Item By hopper, pipe */
    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3) {
        return false;
    }

    /** WIP Used To Pull Item By hopper, pipe */
    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.slots[i];
    }

    /** Used To Pull Items Out Of The Inv By Player */
    @Override
    public ItemStack decrStackSize(int i, int j) {
        if(this.slots[i] != null) {
            ItemStack itemStack;

            if(this.slots[i].stackSize <= j) {
                itemStack = this.slots[i];
                this.slots[i] = null;
            }else{
                itemStack = this.slots[i].splitStack(j);

                if(this.slots[i].stackSize == 0) {
                    this.slots[i] = null;
                }
            }

            return itemStack;

        }

        return null;

    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if(this.slots[i] != null) {
            ItemStack itemStack = this.slots[i];
            this.slots[i] = null;
            return itemStack;
        }

        return null;

    }

    /** Used To Get Each ItemStack In Inv */
    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.slots[i] = itemstack;

        if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    /** Each ItemStack In Inv Can Store Only Up To 64 Items */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }

    public void setInventoryName(String string) {

    }
}
