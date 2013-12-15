package bau5.mods.observers.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import bau5.mods.observers.RemoteObservers;
import bau5.mods.observers.TileEntityLinkModifier;

public class ContainerLinkModifier extends Container {

	public TileEntityLinkModifier tileEntity;
	
	private IInventory inventory = new InventoryBasic("linkmodifier", false, 1);
	
	public ContainerLinkModifier(EntityPlayer player, TileEntityLinkModifier tile){
		tileEntity = tile;
		addSlotToContainer(new SlotObservingStation(inventory, 0, 10, 10));
		bindPlayerInventory(player.inventory);
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
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		if (!tileEntity.worldObj.isRemote){
			if(inventory.getStackInSlot(0) != null)
				par1EntityPlayer.dropPlayerItem(inventory.getStackInSlot(0));
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

            if (par2 == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 36, false))
                {
                    return null;
                }
            }
            else if (par2 > 0 && par2 < 37)
            {
                if (!(itemstack1.itemID == RemoteObservers.monitorItem.itemID && itemstack1.stackSize == 1 && this.mergeItemStack(itemstack1, 0, 1, false)))
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
