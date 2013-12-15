package bau5.mods.observers;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockObserver extends ItemBlock{
	private final String[] names = {
		"observer", "modifier"	
	};
	public ItemBlockObserver(int id) {
		super(id);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabDecorations);
		setUnlocalizedName("obsitemblock");
	}
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		// TODO Auto-generated method stub
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ, metadata);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack,
			EntityPlayer player, List list, boolean par4) {
		super.addInformation(itemstack, player, list, par4);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack);
	}
}
