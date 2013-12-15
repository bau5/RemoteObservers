package bau5.mods.observers.links;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class LinkTileMonitor extends MonitorLinks {
	public TileEntity linkedTile;
	public LinkTileMonitor(int x, int y, int z, TileEntity tile){
		super(x, y, z);
		if(tile.worldObj.getBlockTileEntity(x, y, z) == tile){
			linkedTile = tile;
		}
	}
	
	@Override
	public void tickLink() {
		if(linkedTile == null || linkedTile.worldObj.getBlockTileEntity(linkXCoord, linkYCoord, linkZCoord) != linkedTile){
			invalidate();
		}
		if(!isInvalid()){
			
		}
	}
	
	@Override
	public String toString() {
		return String.format("TileEntityLink(%d,%d,%d)->%s", linkXCoord, linkYCoord,
				linkZCoord, linkedTile.getClass().getSimpleName());
	}

	@Override
	public MonitorLinks makeNew(NBTTagCompound linkTag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NBTTagCompound writeLinkToTag(NBTTagCompound tag) {
		super.writeLinkToTag(tag);
		NBTTagCompound tilelinktag = new NBTTagCompound();
		tilelinktag.setInteger("LinkX", linkXCoord);
		tilelinktag.setInteger("LinkY", linkYCoord);
		tilelinktag.setInteger("LinkZ", linkZCoord);
		tilelinktag.setString("TileType", linkedTile.getClass().getName());
		return tag;
	}
}