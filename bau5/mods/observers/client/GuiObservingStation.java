package bau5.mods.observers.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import bau5.mods.observers.PacketHandler;
import bau5.mods.observers.TileEntityMonitoringStation;
import bau5.mods.observers.inventory.ContainerObservingStation;

public class GuiObservingStation extends GuiContainer{
	private TileEntityMonitoringStation tileEntity;
	private ContainerObservingStation container;
	private int selectedLink = 0;
	private boolean initialized = false;
	
	private ResourceLocation resource = new ResourceLocation("monitors", "textures/gui/observer.png");
	public GuiObservingStation(EntityPlayer player, TileEntityMonitoringStation te) {
		super(new ContainerObservingStation(player, te));
		tileEntity = te;
		container = (ContainerObservingStation)inventorySlots;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		mc.getTextureManager().bindTexture(resource);
		int x = (width-xSize)/2;
		int y = (height-ySize)/2;
		int tab = container.getCurrentTab();
		if(!initialized){
			buttonList.add(new ScrollButton(0, x +56, y +22));
			buttonList.add(new ScrollButton(1, x +105, y +22));
			initialized = true;
		}
		for(int a = 0; a < 2; a++){
			int b = (a * 31);
			int c = 0;
			if(tab == 1){
				if(a == 0)
					c = 31;
				else
					c = -31;
			}
			this.drawTexturedModalRect(x +b +c, y-20, b, 0, 31 +b, 31);
		}
		if(tab == 1){
			this.drawTexturedModalRect(x, y +76, 0, 31, 176, 90);
			this.drawTexturedModalRect(x+44, y +18, 0, 121, 86, 51);
		}else{
			this.drawTexturedModalRect(x+78, y +20, 30, 172, 20, 20);
			if(!tileEntity.linkMap.isEmpty()){
	    		if(selectedLink >= tileEntity.linkMap.size() && selectedLink >= 0)
	    			return;
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_LIGHTING);
	    		int xLoc = (x + 80);
	    		int yLoc = (y + 22);
	    		ItemStack stack = tileEntity.linkMap.keySet().toArray(new ItemStack[0])[selectedLink];
		        FontRenderer font = null;
		        if (stack != null) font = stack.getItem().getFontRenderer(stack);
		        if (font == null) font = fontRenderer;
		        itemRenderer.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), stack, xLoc, yLoc);
		        itemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, xLoc, yLoc, "");
		        GL11.glPopMatrix();
			}
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int type) {
		super.mouseClicked(x, y, type);
		int xLoc = (width-xSize)/2;
		int yLoc = (height-ySize)/2;
		if(x <= xLoc +30 && x >= xLoc && y >= yLoc -20 && y <= yLoc +9){
			System.out.println("A");
			if(container.getCurrentTab() == 0)
				return;
			container.switchToTab(0);
			PacketHandler.sendTinyPacket(0, 0);
		}
		if(x <= xLoc +62 && x >= xLoc +32 && y >= yLoc -20 && y <= yLoc +9){
			if(container.getCurrentTab() == 1)
				return;
			container.switchToTab(1);
			PacketHandler.sendTinyPacket(0, 1);
		}
	}
	
	public class ScrollButton extends GuiButton{
		private int buttonID;
		public ScrollButton(int id, int x, int y) {
			super(id, x, y, 15, 15, "");
			buttonID = id;
		}
		
		@Override
		public void drawButton(Minecraft par1Minecraft, int i, int j) {
			if (this.drawButton && container.getCurrentTab() == 0)
	        {
	            FontRenderer fontrenderer = mc.fontRenderer;
	            mc.getTextureManager().bindTexture(resource);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            this.field_82253_i = i >= this.xPosition && j >= this.yPosition && i < this.xPosition + this.width && j < this.yPosition + this.height;
	            int k = this.getHoverState(this.field_82253_i);
	            if(k == 2 && !isEnabled())
	            	k -= 1;
	            //Top left, top right, bottom left, bottom right, icon
	            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0 + (buttonID *15), 172 + (k-1)*15, width, height); 
	            this.mouseDragged(mc, i, j);
	            int l = 14737632;

	            if (!this.enabled)
	            {
	                l = -6250336;
	            }
	            else if (this.field_82253_i)
	            {
	                l = 16777120;
	            }

	            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
	        }
		}
		
		@Override
		public boolean mousePressed(Minecraft mc, int par2, int par3) {
			boolean fireButton = super.mousePressed(mc, par2, par3);
			if(fireButton && container.getCurrentTab() == 0 && isEnabled()){
	            mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	            if(buttonID == 0){
	            	selectedLink -= 1;
	            	if(selectedLink < 0)
	            		selectedLink = 0;
	            }else{
	            	selectedLink += 1;
	            	if(selectedLink >= tileEntity.linkMap.size())
	            		selectedLink = tileEntity.linkMap.size();
	            }
			}
			return fireButton;
		}
		
		public boolean isEnabled(){
			if(buttonID == 0){
				return selectedLink - 1 > -1;
			}else{
				return selectedLink + 1 < tileEntity.linkMap.size();
			}
		}
	}
}
