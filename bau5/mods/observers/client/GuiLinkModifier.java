package bau5.mods.observers.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import bau5.mods.observers.TileEntityLinkModifier;
import bau5.mods.observers.inventory.ContainerLinkModifier;

public class GuiLinkModifier extends GuiContainer {
	
	private ContainerLinkModifier container;
	private TileEntityLinkModifier tileEntity;
	
	public GuiLinkModifier(EntityPlayer player, TileEntityLinkModifier tile) {
		super(new ContainerLinkModifier(player, tile));
		container = (ContainerLinkModifier)inventorySlots;
		tileEntity = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ItemStack stack = container.getSlot(0).getStack();
		if(stack != null){
			if(stack.hasTagCompound()){
				NBTTagCompound linktag = stack.stackTagCompound.getCompoundTag("LinkInfo");
				int x = linktag.getInteger("TileX");
				int y = linktag.getInteger("TileY");
				int z = linktag.getInteger("TileZ");
				TileEntity tile = mc.theWorld.getBlockTileEntity(x, y, z);
				if(tile != null && tile instanceof IInventory){
					IInventory inv = (IInventory)tile;
					for(int a = 0; a < inv.getSizeInventory(); a++){
			    		ItemStack inventorystack = inv.getStackInSlot(a);
			    		if(inventorystack == null)
			    			continue;
						GL11.glPushMatrix();
						GL11.glDisable(GL11.GL_LIGHTING);
			    		int xLoc = (x + 80) + (a * 18);
			    		int yLoc = (y + 22);
				        FontRenderer font = null;
				        if (stack != null) font = stack.getItem().getFontRenderer(stack);
				        if (font == null) font = fontRenderer;
				        itemRenderer.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), inventorystack, xLoc, yLoc);
				        itemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), inventorystack, xLoc, yLoc, "");
			        	GL11.glPopMatrix();
					}
				}
			}
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int type) {
		// TODO Auto-generated method stub
		super.mouseClicked(x, y, type);
	}

}
