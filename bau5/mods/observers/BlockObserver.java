package bau5.mods.observers;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockObserver extends BlockContainer {
	
	private Icon[] icons;
	
	public BlockObserver(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister registrar) {
		icons = new Icon[2];
		for(int i = 0; i<icons.length; i++){
			icons[i] = registrar.registerIcon("modjam:monitor_" +i);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if(world.getBlockTileEntity(x, y, z) != null){
			player.openGui(RemoteObservers.instance, world.getBlockMetadata(x, y, z), world, x, y, z);
			return true;
		}
		return super.onBlockActivated(world, x, y, z, player,
				par6, par7, par8, par9);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		switch(metadata){
		case 0: return new TileEntityMonitoringStation();
		case 1: return new TileEntityLinkModifier();
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
			List list) {
		list.add(new ItemStack(blockID, 1, 0));
		list.add(new ItemStack(blockID, 1, 1));
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}
