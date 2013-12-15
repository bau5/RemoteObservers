package bau5.mods.observers.links;

import net.minecraft.nbt.NBTTagCompound;

public class LinkRedstoneMonitor extends MonitorLinks{

	public LinkRedstoneMonitor(int x, int y, int z) {
		super(x, y, z);
	}
	
	@Override
	public void tickLink() {
		if(!isInvalid()){
			
		}
	}
	
	@Override
	public String toString() {
		return String.format("RedstoneLink(%d,%d,%d)", linkXCoord,
								linkYCoord, linkZCoord);
	}

	@Override
	public MonitorLinks makeNew(NBTTagCompound linkTag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public NBTTagCompound writeLinkToTag(NBTTagCompound tag) {
		return super.writeLinkToTag(tag);
	}
}
