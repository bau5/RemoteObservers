package bau5.mods.observers.inventory;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import bau5.mods.observers.RemoteObservers;
import bau5.mods.observers.TileEntityMonitoringStation;

public class ContainerObservingStation extends Container {

	public TileEntityMonitoringStation tileEntity;
	private EntityPlayer player;
	
	public Slot[] inventoryslots;
	
	private int currentTab = 0;
	
	public ContainerObservingStation(EntityPlayer plyr, TileEntityMonitoringStation te){
		tileEntity = te;
		player = plyr;
		inventoryslots = new Slot[tileEntity.inv.length];
		makeInventorySlots();
	}
	
	public void switchToTab(int index){
		System.out.println("Switching to tab " +index);
		if(currentTab == 1 && index != currentTab){
			removeInventorySlots();
		}
		currentTab = index;
		if(index == 1){
			putSlotsForInventoryTab();
			bindPlayerInventory(player.inventory);
		}
	}
	
	public void putSlotsForInventoryTab(){
		for(Slot inventoryslot : inventoryslots){
			addSlotToContainer(inventoryslot);
		}
	}
	
	public void removeInventorySlots(){
//		List temp = Lists.newArrayList(inventoryslots);
//		inventorySlots.removeAll(temp);
		inventorySlots = new ArrayList();
	}
	
	public void makeInventorySlots(){
		int k = 0;
		for(int i = 0; i < inventoryslots.length; i++){
			if(i < 4){
				inventoryslots[i] = new SlotObservingStation(tileEntity, i, 52 + (i * 18), 26);
			}else{
				inventoryslots[i] = new SlotObservingStation(tileEntity, i, 52 + ((i % 4) * 18), 45);
			}
		}
	}
	
	public int getCurrentTab(){
		return currentTab;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	private void bindPlayerInventory(InventoryPlayer invPlayer) {
		int i;
		int j;
		for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 >= 0 && par2 < 8)
            {
                if (!this.mergeItemStack(itemstack1, 8, 43, false))
                {
                    return null;
                }
            }
            else if (par2 >= 8 && par2 <= 43)
            {
                if (!(itemstack1.itemID == RemoteObservers.monitorItem.itemID && itemstack1.stackSize == 1 && this.mergeItemStack(itemstack1, 0, 7, false)))
                {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
	}
}
