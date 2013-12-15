package bau5.mods.observers.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import bau5.mods.observers.RemoteObservers;

public class SlotObservingStation extends Slot{

	public SlotObservingStation(IInventory inventory, int id, int x,
			int y) {
		super(inventory, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return itemstack.itemID == RemoteObservers.monitorItem.itemID;
	}
	
	@Override
	public int getSlotStackLimit() {
		return inventory.getInventoryStackLimit();
	}
}
