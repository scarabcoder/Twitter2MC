package com.scarabcoder.ereijan.events;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.gui.AuthGUI;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;

public class Events {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	public static long tick;
	public static boolean check = false;
	public static String text;
	public static int width;
	public static int height;
	
	@SubscribeEvent
	public void loadWorld(EntityJoinWorldEvent e){
		
		
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent e){
		if(check){
			if(tick < mc.theWorld.getTotalWorldTime() - 3){
				Main.screenshotPost(text, width, height);
				
				this.check = false;
			}
		}
	}
	
	public static void onAuth() throws Exception{
    	
    	ResponseList<DirectMessage> msgs = Main.twitter.directMessages().getDirectMessages(new Paging(1, 150));
	        for(DirectMessage msg : msgs){
	        	if(Main.dms.size() != 3){
	        		if(!Main.dms.contains(msg.getSender().getScreenName())){
	        			Main.dms.add(msg.getSender().getScreenName());
	        		}
	        	}
	        }
    }
	
	@SubscribeEvent
	public void playerAttackEntity(AttackEntityEvent e){
		
	}
	
	@SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if(KeyBindings.auth.isPressed())
    		mc.displayGuiScreen(new AuthGUI());
    }
	
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent e){
		/*if(e.type == RenderGameOverlayEvent.ElementType.TEXT){
			NBTTagCompound tag = new NBTTagCompound();
			tag = mc.thePlayer.getEntityData();
			mc.thePlayer.writeToNBT(tag);
			int maxEssence;
			System.out.println(tag.hasKey("maxEssence"));
			if(tag.hasKey("maxEssence")){
				maxEssence = tag.getInteger("maxEssence");
			}else{
				maxEssence = -1;
			}
			mc.fontRendererObj.drawString(String.valueOf(tag.getInteger("maxEssence")), 50, 50,0xFFFFFF);
		}*/
	}
	
}
