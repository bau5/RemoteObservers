package bau5.mods.observers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemObserver extends Item {
	private Icon[] icons;
	
	public ItemObserver(int id) {
		super(id);
		setMaxStackSize(16);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,
			EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		if(stack.stackTagCompound != null){
			NBTTagCompound tileTag = stack.stackTagCompound.getCompoundTag("LinkInfo");
			if(tileTag != null && !tileTag.hasNoTags()){
				int[] loc = { tileTag.getInteger("TileX"), tileTag.getInteger("TileY"), 
								tileTag.getInteger("TileZ") 
				};
				String name = tileTag.getString("TileName");
				list.add(name);
				list.add("X: " +loc[0]);
				list.add("Y: " +loc[1]);
				list.add("Z: " +loc[2]);
				
			}
		}
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		System.out.println(world.isRemote);
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null){
			if(stack.stackSize == 1){
				if(stack.stackTagCompound == null)
					stack.stackTagCompound = new NBTTagCompound("tag");
				NBTTagCompound tileLocTag = new NBTTagCompound();
				tileLocTag.setInteger("TileX", x);
				tileLocTag.setInteger("TileY", y);
				tileLocTag.setInteger("TileZ", z);
				tileLocTag.setString("TileName", Block.blocksList[world.getBlockId(x, y, z)].getLocalizedName());
				if(player.isSneaking() || !stack.stackTagCompound.hasKey("LinkInfo")){
					stack.stackTagCompound.setTag("LinkInfo", tileLocTag);
					stack.setItemDamage(1);
					if(player instanceof EntityPlayerSP){
						player.addChatMessage("Link established. Insert into Monitoring Station.");
					}
				}else{
					if(player instanceof EntityPlayerSP)
						player.addChatMessage("Link already active, sneak right click to overwrite.");
				}
			}else{
				if(player instanceof EntityPlayerSP){
					player.addChatMessage("Separate into a single item in the stack.");
				}
			}
		}
		return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World par2World,
			EntityPlayer player) {
		if(player.isSneaking() && itemstack.hasTagCompound()){
			itemstack.stackTagCompound = null;
			itemstack.setItemDamage(0);
		}
		return super.onItemRightClick(itemstack, par2World, player);
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		if(stack.hasTagCompound())
			return icons[1];
		else
			return icons[0];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister registrar) {
		icons = new Icon[2];
		icons[0] = registrar.registerIcon("monitors:link_i");
		icons[1] = registrar.registerIcon("monitors:link_a");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage) {
		if(damage <= 1){
			return icons[damage];
		}
		return super.getIconFromDamage(damage);
	}
}
