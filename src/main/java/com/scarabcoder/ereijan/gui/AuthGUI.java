package com.scarabcoder.ereijan.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.scarabcoder.ereijan.Main;
import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.events.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class AuthGUI extends GuiScreen{
	Minecraft mc = Minecraft.getMinecraft();
	
	private boolean showText = false;
	
	private GuiTextField text;
	
	
	public static final int GUI_ID = 20;
	
	
	public AuthGUI(){
		
	}
	
	public Twitter twitter;
	
	private AccessToken accessToken;
	
	private RequestToken requestToken;

	
	private boolean showTweet = false;

	private boolean showTweetBox;
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){

        this.drawDefaultBackground();
		if(this.showText){
			this.text.drawTextBox();
		}
		if(!Main.isDoneAuth){
		this.fontRendererObj.drawString("Press get PIN, then in web browser authorize app.", this.width / 2 - 120, this.height / 2, 0xFFFFFF);
		this.fontRendererObj.drawString("Paste pin in text box and press enter.", this.width / 2 - 120, this.height / 2 + 20, 0xFFFFFF);
		}
		 
		
			//this.text2 = new GuiTextField(2, this.fontRendererObj, 0, this.height/2-46, this.width, 20);
	        
	        
		this.buttonList.clear();
	     if(!Main.isDoneAuth){
				this.buttonList.add(new GuiButton(1,this.width / 2 - 68, this.height/5, 137, 20, "Get PIN"));
				}
				if(Main.isDoneAuth && !this.showTweet){
				this.buttonList.add(new GuiButton(2, 10, 10, 100, 20, "Send Tweet"));
				this.buttonList.add(new GuiButton(5, this.width / 2 - 50, 10, 100, 20, "Direct Message"));
				String onOff = "Off";
				if(Main.showTweets){
					onOff = "On";
				}
				this.buttonList.add(new GuiButton(4, this.width - 110, 10, 100, 20, "Show Tweets: " + onOff));
				try {
					this.buttonList.add(new GuiButton(6, this.width - 170, this.height - 30, 160, 20, "Log out (" + Main.twitter.getScreenName() + ")"));
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		if(this.showText){
			this.buttonList.add(new GuiButton(3, this.width / 2 - 70, this.height/6 * 4, 140, 20, "Authenticate"));
		}
			

		super.drawScreen(mouseX, mouseY, partialTicks);
		
			
	}

	@Override
	public void initGui(){
		super.initGui();
		 this.text = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 68, this.height/2-46, 137, 20);
			
	     text.setMaxStringLength(7);
	     text.setText("");
	     this.text.setFocused(true);
	     
			
			
	}
	public boolean doesGuiPauseGame()
    {
        return false;
    }
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(par1);
        this.text.textboxKeyTyped(par1, par2);
        
        
        
        
        //Pressed enter
        
    }
	  public void updateScreen()
	    {
	        super.updateScreen();
	        this.text.updateCursorCounter();
	        
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
	    
	    
	    
	    protected void actionPerformed(GuiButton guibutton) {
            //id is the id you give your button
            switch(guibutton.id) {
            case 1:
            	
        		ScarabUtil.openWebpage(Main.url);
     	        this.twitter = Main.twitter;
            	this.requestToken = Main.token;
            	this.showText = true;
        		
            	break;
            case 2:
            	mc.thePlayer.closeScreen();
            	mc.displayGuiScreen(new TweetGUI());
            	break;
            case 3:
            	if(this.text != null){
            	   try{
             	        accessToken = Main.twitter.getOAuthAccessToken(requestToken, this.text.getText());
             	        System.out.println(twitter.getScreenName());
             	        mc.thePlayer.addChatComponentMessage(new ChatComponentText("Sucessfully connected to Twitter account \"" + twitter.getScreenName() + "\"!"));
             	        TwitterStream twitterStream = new TwitterStreamFactory().getInstance(twitter.getAuthorization());
             	        
             			twitterStream.addListener(Main.listener);
             			twitterStream.user();
             	        Main.isDoneAuth = true;
             	       
             	       File file = new File("twitter.txt");
             	       if(file.exists()){
             	    	   file.delete();
             	       }
             	       try {
						Events.onAuth();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
             	       try {
           				FileWriter fileWriter = new FileWriter(file.getName());
           				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
           				bufferedWriter.write(this.accessToken.getToken());
           				bufferedWriter.newLine();
           				bufferedWriter.write(this.accessToken.getTokenSecret());
           				bufferedWriter.close();
           			} catch (IOException e) {
           				// TODO Auto-generated catch block
           				e.printStackTrace();
           			}
             	       
             	      } catch (TwitterException te) {
             	    	mc.thePlayer.addChatComponentMessage(new ChatComponentText("Error connecting to Twitter! Try restarting Minecraft and trying again."));
             	        if(401 == te.getStatusCode()){
             	          System.out.println("Unable to get the access token.");
             	        }else{
             	          te.printStackTrace();
             	        }
             	      }
             	    
               	mc.thePlayer.closeScreen();
            	}
            	break;
            case 4:
            	Main.showTweets = !Main.showTweets;
            	break;
            case 5:
            	mc.thePlayer.closeScreen();
            	mc.displayGuiScreen(new DMGui());
            	break;
            case 6:
        		File file = new File("twitter.txt");
            	if(file.exists()){
        			file.delete();
        		}
            	try {
    				FileWriter fileWriter = new FileWriter(file.getName());
    				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    				bufferedWriter.write("none");
    				bufferedWriter.newLine();
    				bufferedWriter.write("none");
    				bufferedWriter.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	mc.thePlayer.closeScreen();
            	ScarabUtil.chat("Restart Minecraft for logout to take effect.");
            	break;
            }
            
            
          
    }
	
}
