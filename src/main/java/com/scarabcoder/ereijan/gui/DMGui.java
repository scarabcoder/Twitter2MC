package com.scarabcoder.ereijan.gui;

import java.io.IOException;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import twitter4j.TwitterException;

public class DMGui extends GuiScreen{
	
	private GuiTextField text;
	
	private GuiTextField text2;
	public boolean doesGuiPauseGame()
    {
        return false;
    }
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){

        this.drawDefaultBackground();
        
        this.buttonList.clear();
        int x = 1;
        String str2 = "";
        if(this.text.getText().length() > 2){
        	str2 = " to " + this.text.getText();
        }
        this.buttonList.add(new GuiButton(4, this.width / 2 - 68, this.height / 2 + 30, 137, 20, "Send" + str2));
        for(String str : Main.dms){
        	this.buttonList.add(new GuiButton(x, this.width - 110, this.height / 6 + (30*x), 100, 20, str));
        	x = x + 1;
        }
        this.fontRendererObj.drawString("Recent", this.width - 90, this.height/6 + 20, 0xFFFFFF);
        this.fontRendererObj.drawString("To:", this.width / 2 - 90, this.height/4 + 7,0xFFFFFF);
        this.fontRendererObj.drawString("Message:", this.width / 2 - 120, this.height /2 + 7, 0xFFFFFF);
        this.text.drawTextBox();
        this.text2.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void initGui()
    {
        this.text = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 68, this.height/4, 137, 20);
        text.setMaxStringLength(500);
        text.setText("");
        
        this.text2 = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 68, this.height/2, 137, 20);
        text2.setMaxStringLength(500);
        text2.setText("");
    }
    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.text.mouseClicked(x, y, btn);
        this.text2.mouseClicked(x, y, btn);
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
        this.text2.textboxKeyTyped(par1, par2);
    }


        public void updateScreen()
        {
            super.updateScreen();
            this.text.updateCursorCounter();
            this.text2.updateCursorCounter();
        }
        
        protected void actionPerformed(GuiButton guibutton) {
            //id is the id you give your button
            if(guibutton.id < 4){
            	this.text.setText(Main.dms.get(guibutton.id - 1));
            }
            if(guibutton.id == 4){
            	try {
					Main.twitter.sendDirectMessage(this.text.getText(), this.text2.getText());
					ScarabUtil.chat("Sent message to @" + this.text.getText());
				} catch (TwitterException e) {
					ScarabUtil.chat("Error sending message!");
					e.printStackTrace();
				}
            	mc.thePlayer.closeScreen();
            }
        }
	

}
