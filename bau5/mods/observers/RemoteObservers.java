package bau5.mods.observers;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(name=Reference.MOD_NAME, version=Reference.MOD_VERSION, modid=Reference.MOD_ID)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, 
			packetHandler=PacketHandler.class,
			channels={Reference.CHANNEL})
public class RemoteObservers {
	@Instance(Reference.MOD_ID)
	public static RemoteObservers instance;
	@SidedProxy(serverSide="bau5.mods.observers.CommonProxy",
				clientSide="bau5.mods.observers.client.ClientProxy")
	public static CommonProxy proxy;
	
	public static Item monitorItem;
	public static Block monitor;

	public static int UPDATE_FREQUENCY;
	
	private int[] itemIDs;
	private int	  monitorID;
	
	@EventHandler
	public void preLoad(FMLPreInitializationEvent ev){
		Configuration config = new Configuration(ev.getSuggestedConfigurationFile());
		itemIDs = new int[1];
		try{
			config.load();
			itemIDs[0] = config.getItem("Remote Observer", 18975).getInt(18975);
			monitorID  = config.getBlock("RO: Block 1", 4065, "Block ID for Observing Station, Link Modifier.").getInt(4065);
			UPDATE_FREQUENCY = config.get(Configuration.CATEGORY_GENERAL, "Update Frequency", 20, "How many ticks inbetween each link update. Lower the number, the more CPU intensive it becomes.").getInt(20);
		}catch(Exception ex){
			FMLLog.log(Level.SEVERE, ex, null, "Remote Monitors failed loading config.");
		}finally{
			config.save();
		}
		monitorItem = new ItemObserver(itemIDs[0]).setUnlocalizedName("bau5_remonitor");
		monitor = new BlockObserver(monitorID);
		GameRegistry.registerItem(monitorItem, "monitor", null);
		GameRegistry.registerBlock(monitor, ItemBlockObserver.class, "bau5_observer", null);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent ev){
		GameRegistry.registerTileEntity(TileEntityMonitoringStation.class, "bau5_monitorer");
		GameRegistry.registerTileEntity(TileEntityLinkModifier.class, "bau5_linkmodifier");
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent ev){
		registerRecipes();
	}
	
	private void registerRecipes(){
		
	}
}
