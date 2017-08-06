package com.scarabcoder.ereijan.gui;

import java.io.IOException;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.events.Events;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import twitter4j.StatusUpdate;

public class TweetGUI extends GuiScreen{

	private GuiTextField text;
	
	private boolean image = false;
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
		super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawDefaultBackground();
        this.text.drawTextBox();
        String str = "";
        if(this.image){
	    	str = "116";
	    }else{
	    	str = "140";
	    }
        this.fontRendererObj.drawString(this.text.getText().length() + "/" + str, this.width - 40,this.height/2-38, 0xFFFFFF);
        this.buttonList.clear();
        String name;
	     try{
	    name = " as " + Main.twitter.getScreenName();
	     }catch(Exception e){
	    	 name = "";
	     }
	    this.buttonList.add(new GuiButton(1, this.width / 2 - 80, this.height / 5, 160, 20, "Send Tweet" + name));
	    str = "Yes";
	    if(image == false){
	    	str = "No";
	    }
	    this.buttonList.add(new GuiButton(2, this.width - 160, 10, 150, 20, "Screenshot: " + str));
        super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui(){
		super.initGui();
        this.text = new GuiTextField(1, this.fontRendererObj, 15, this.height/2-25, this.width - 30, 20);
	    if(this.image){
	    	text.setMaxStringLength(116);
	    }else{
	    	text.setMaxStringLength(140);
	    }
        text.setText("");
        this.text.setFocused(true);
        
	}
    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.mouseClicked(x, y, btn);
    }
	
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.textboxKeyTyped(par1, par2);
        if(par1 == '\r'){
        	sendTweet();
        }
        
    }
	public boolean doesGuiPauseGame()
    {
        return false;
    }
	public void sendTweet(){
		try{
			//update.setMedia(file);

        	mc.thePlayer.closeScreen();
        	if(this.image){
        		Events.height = this.height;
        		Events.width = this.width;
        		Events.tick = mc.theWorld.getTotalWorldTime() + 6;
        		Events.text = this.text.getText();
        		Events.check = true;
    		}else{

        		mc.thePlayer.addChatComponentMessage(new ChatComponentText("Posted following tweet:"));
    			StatusUpdate update = new StatusUpdate(this.text.getText());
        		Main.twitter.updateStatus(update);
        		mc.thePlayer.addChatComponentMessage(new ChatComponentText("@" + Main.twitter.getScreenName() + ": " + this.text.getText()));
        		mc.thePlayer.closeScreen();
    		}
    		
    		
    	}catch(Exception e){
    		mc.thePlayer.addChatComponentMessage(new ChatComponentText("Error sending tweet! Check logs for more information."));
    		e.printStackTrace();
    	}
	}
	protected void actionPerformed(GuiButton guibutton) {
        //id is the id you give your button
        switch(guibutton.id) {
        case 1:
        	sendTweet();
        	break;
        case 2:
        	this.image = !this.image;
        	break;
        }
        
	}
	public void updateScreen()
    {
        super.updateScreen();
        this.text.updateCursorCounter();
    }
}
