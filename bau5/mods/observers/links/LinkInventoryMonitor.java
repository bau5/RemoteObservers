package bau5.mods.observers.links;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class LinkInventoryMonitor extends LinkTileMonitor {
	public IInventory inventory;
	public LinkInventoryMonitor(int x, int y, int z, TileEntity tile, IInventory inv) {
		super(x, y, z, tile);
		inventory = inv;
	}
	
	@Override
	public void tickLink() {
		super.tickLink();
		if(!isInvalid()){
			
		}
	}
	
	@Override
	public MonitorLinks makeNew(NBTTagCompound linkTag) {
		// TODO Auto-generated method stub
		return super.makeNew(linkTag);
	}
	@Override
	public NBTTagCompound writeLinkToTag(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		return super.writeLinkToTag(tag);
	}
	
	@Override
	public String toString() {
		return String.format("InventoryLink(%d,%d,%d)->%s->%s", linkXCoord, linkYCoord,
								linkZCoord, linkedTile.getClass().getSimpleName(), 
								inventory.getClass().getSimpleName());
	}
}
