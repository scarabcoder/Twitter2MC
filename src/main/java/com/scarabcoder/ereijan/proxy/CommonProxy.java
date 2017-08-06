package com.scarabcoder.ereijan.proxy;

import com.scarabcoder.ereijan.gui.AuthGUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy  implements IGuiHandler{
	public void registerRenders(){
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == AuthGUI.GUI_ID){
            return new AuthGUI();
        }
		return null;
	}
}
