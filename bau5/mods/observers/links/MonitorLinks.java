package bau5.mods.observers.links;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.FMLLog;

public abstract class MonitorLinks{
	
	public static HashMap<Byte, Class<? extends MonitorLinks>> linkTypes = new HashMap();
	
	public final int linkXCoord;
	public final int linkYCoord;
	public final int linkZCoord;
	
	public boolean invalid = false;
	
	public MonitorLinks(int x, int y, int z){
		linkXCoord = x;
		linkYCoord = y;
		linkZCoord = z;
	}
	
	public MonitorLinks() {
		linkXCoord = -1;
		linkYCoord = -1;
		linkZCoord = -1;
	}
	
	public abstract void tickLink();
	public abstract MonitorLinks makeNew(NBTTagCompound linkTag);
	
	public boolean isInvalid(){
		return invalid;
	}
	
	public void invalidate(){
		invalid = true;
	}
	
	@Override
	public String toString() {
		return String.format("BasicLink(%d,%d,%d)", linkXCoord, linkYCoord, linkZCoord);
	}
	
	public byte getByteType(Class<? extends MonitorLinks> clz){
		if(linkTypes.containsValue(clz)){
			for(Entry<Byte, Class<? extends MonitorLinks>> entry : linkTypes.entrySet()){
				if(entry.getValue().equals(clz)){
					return entry.getKey();
				}
			}
		}
		return -1;
	}
	
	static{
		linkTypes.put((byte)0, LinkTileMonitor.class);
		linkTypes.put((byte)1, LinkInventoryMonitor.class);
		linkTypes.put((byte)2, LinkRedstoneMonitor.class);
	}

	public static MonitorLinks newLink(byte type, NBTTagCompound linkTag) {
		try{
			Class clz = linkTypes.get(type);
			if(clz != null){
				MonitorLinks instance = (MonitorLinks) clz.newInstance();
				instance = instance.makeNew(linkTag);
				return instance;
			}
		}catch(Exception ex){
			FMLLog.log(Level.SEVERE, ex, null, "Remote Monitor: Failed making a new link.");
		}
		
		return null;
	}

	public NBTTagCompound writeLinkToTag(NBTTagCompound tag){
		tag.setByte("Type", getByteType(this.getClass()));
		return tag;
	}
}
