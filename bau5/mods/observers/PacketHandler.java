package bau5.mods.observers;

import bau5.mods.observers.inventory.ContainerObservingStation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{
	
	public static final String CHANNEL = Reference.CHANNEL;
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.data.length < 5){
			handleTinyPacket(packet, player);
			return;
		}
	}
	
	public void handleTinyPacket(Packet250CustomPayload packet, Player player){
		switch(packet.data[0]){
		case 0: switchTabs(packet, player);
			break;
		}
	}
	
	private void switchTabs(Packet250CustomPayload packet, Player player){
		EntityPlayerMP plyr = (EntityPlayerMP)player;
		ContainerObservingStation container = (ContainerObservingStation)plyr.openContainer;
		container.switchToTab(packet.data[1]);
	}

	public static void sendTinyPacket(int id, int i) {
		PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(CHANNEL, new byte[] { (byte)id, (byte)i }));
	}
}
