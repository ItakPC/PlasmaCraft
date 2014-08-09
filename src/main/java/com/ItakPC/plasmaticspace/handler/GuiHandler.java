package com.ItakPC.plasmaticspace.handler;

import com.ItakPC.plasmaticspace.PlasmaticSpace;
import com.ItakPC.plasmaticspace.machine.windmill.ContainerWindmill;
import com.ItakPC.plasmaticspace.machine.windmill.GuiWindmill;
import com.ItakPC.plasmaticspace.machine.windmill.TileEntityWindmill;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        switch (ID) {
            case PlasmaticSpace.guiIDWindmillBasic:
                while (tileEntity instanceof TileEntityWindmill && world.getBlockMetadata(x, y, z) < 8) {
                    y++;
                }

                return new ContainerWindmill(player.inventory, (TileEntityWindmill) world.getTileEntity(x, y, z));

        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        switch (ID) {
            case PlasmaticSpace.guiIDWindmillBasic:
                while (tileEntity instanceof TileEntityWindmill && world.getBlockMetadata(x, y, z) < 8) {
                    y++;
                }

                return new GuiWindmill(player.inventory, (TileEntityWindmill) world.getTileEntity(x, y, z));

        }

        return null;
    }
}

