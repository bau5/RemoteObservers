package bau5.mods.observers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import bau5.mods.observers.client.GuiLinkModifier;
import bau5.mods.observers.client.GuiObservingStation;
import bau5.mods.observers.inventory.ContainerLinkModifier;
import bau5.mods.observers.inventory.ContainerObservingStation;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null){
			switch(ID){
			case 0: return new ContainerObservingStation(player, (TileEntityMonitoringStation)tile);
			case 1: return new ContainerLinkModifier(player, (TileEntityLinkModifier)tile);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null){
			switch(ID){
			case 0: return new GuiObservingStation(player, (TileEntityMonitoringStation)tile);
			case 1: return new GuiLinkModifier(player, (TileEntityLinkModifier)tile);
			}
		}
		return null;
	}
	
	public void registerRenderingInformation() {}

}
