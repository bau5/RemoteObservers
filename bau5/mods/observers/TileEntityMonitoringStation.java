package bau5.mods.observers;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import bau5.mods.observers.links.LinkInventoryMonitor;
import bau5.mods.observers.links.MonitorLinks;

public class TileEntityMonitoringStation extends TileEntity implements IInventory{
	
	public int updateFrequency = RemoteObservers.UPDATE_FREQUENCY;
	private long ticks = 0L;
	
	public ItemStack[] inv = new ItemStack[8];
	private ItemStack[] lastInv = new ItemStack[inv.length];
	public HashMap<ItemStack, MonitorLinks> linkMap = new HashMap();
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		if(hasInventoryChanged()){
			updateLinks();
		}
	}
	
	private boolean hasInventoryChanged(){
		for(int i = 0; i < inv.length; i++){
			if(!ItemStack.areItemStacksEqual(inv[i], lastInv[i])){
				return true;
			}
		}
		return false;
	}
	
	private void updateLinks() {
		HashMap<ItemStack, MonitorLinks> newMap = new HashMap();
		int nonnull = 0;
		for(ItemStack stack : inv)
			if(stack != null) nonnull++;
		if(nonnull > linkMap.size()){
			for(ItemStack stack : inv){
				if(stack == null)
					continue;
				if(linkMap.containsKey(stack)){
					newMap.put(stack, linkMap.get(stack));
				}else{
					MonitorLinks newlink = makeNewLink(stack);
					if(newlink != null){
						newMap.put(stack, newlink);
					}else{
						System.out.println("Skipping stack, no link: " +stack);
					}
				}
			}
			linkMap = newMap;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		ticks++;
		if(ticks >= updateFrequency){
			ticks = 0;
			for(MonitorLinks link : linkMap.values())
				link.tickLink();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tileTag) {
		super.readFromNBT(tileTag);
		NBTTagList inventorylist = tileTag.getTagList("Inventory");
		if(inventorylist != null && inventorylist.tagCount() > 0){
			for(int i = 0; i < inventorylist.tagCount(); i++){
				NBTTagCompound tag = (NBTTagCompound) inventorylist.tagAt(i);
				ItemStack theStack = ItemStack.loadItemStackFromNBT(tag);
				inv[tag.getByte("Slot")] = theStack;
			}
		}
	}
	
	 @Override
	public void writeToNBT(NBTTagCompound tileTag) {
		 super.writeToNBT(tileTag);
		 NBTTagList inventoryList = new NBTTagList();
		 for(int i = 0; i < inv.length; i++){
			 ItemStack stack = inv[i];
			 if(stack == null)
				 continue;
			 NBTTagCompound tagForStack = new NBTTagCompound();
			 tagForStack.setByte("Slot", (byte)i);
			 stack.writeToNBT(tagForStack);
			 inventoryList.appendTag(tagForStack);
		 }
		 tileTag.setTag("Inventory", inventoryList);
	}
	 
	public MonitorLinks makeNewLink(ItemStack stack){
		NBTTagCompound stackTag = stack.stackTagCompound;
		if(stackTag != null && stackTag.hasKey("LinkInfo")){
			NBTTagCompound linkTag = stackTag.getCompoundTag("LinkInfo");
			int x = linkTag.getInteger("TileX");
			int y = linkTag.getInteger("TileY");
			int z = linkTag.getInteger("TileZ");
			return handleNewLink(x, y, z);
		}
		return null;
	}
	
	public MonitorLinks handleNewLink(int i, int j, int k) {
		Block blck = Block.blocksList[worldObj.getBlockId(i, j, k)];
		TileEntity tile = worldObj.getBlockTileEntity(i, j, k);
		if(tile != null){
			if(tile instanceof IInventory){
				LinkInventoryMonitor inventorylink = new LinkInventoryMonitor(i, j, k, tile, (IInventory)tile);
				return inventorylink;
			}
			System.out.println("Skipping this link, not IInventory: " +tile.getClass().getSimpleName());
		}
		
		return null;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inv[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			if(stack.stackSize <= amount)
			{
				setInventorySlotContents(slot, null);
			} else
			{
				stack = stack.splitStack(amount);
				if(stack.stackSize == 0) 
				{
					setInventorySlotContents(slot, null);
				}else
					onInventoryChanged();
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		onInventoryChanged();
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		inv[slot] = itemstack;
	}

	@Override
	public String getInvName() {
		return "Observing Station";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
}
