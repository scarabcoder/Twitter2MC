package com.scarabcoder.ereijan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.scarabcoder.ereijan.ScarabUtil.ScarabUtil;
import com.scarabcoder.ereijan.ScarabUtil.Strings;
import com.scarabcoder.ereijan.events.Events;
import com.scarabcoder.ereijan.events.KeyBindings;
import com.scarabcoder.ereijan.proxy.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@Mod(modid = Strings.id, name = Strings.name, version = Strings.version)
public class Main {
	@SidedProxy(clientSide = Strings.clientProxyClass, serverSide = Strings.commonProxyClass)
	
	
	public static CommonProxy proxy;
	
	public static boolean showChat = true;
	
	public static boolean isAuth = false;
	
	public static boolean isDoneAuth = false;
	
	public static Twitter twitter;
	
	public static String url = "";
	
	public static boolean showTweets = true;
	
	public static ArrayList<String> dms = new ArrayList<String>();
	
	public static Configuration config;
	
	public static Thread thread = Thread.currentThread();
	
	public static RequestToken token;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new Events());
		KeyBindings.init();
        
	}
	
	

	@EventHandler
	public void init(FMLInitializationEvent event){
		File file = new File("twitter.txt");
		if(!file.exists()){
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
		}
		// The name of the file to open.
        String fileName = "twitter.txt";

        // This will reference one line at a time
        String line = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        try{
        	this.twitter = TwitterFactory.getSingleton();
     	    twitter.setOAuthConsumer("5XyaOfX7xqbKEossNMtpAMjdA", "ydEpJf3r9VlFcyIMdD399zlePH6POAh5zj63g7tL2JwoEiF1yE");
     	    this.token = twitter.getOAuthRequestToken();
     	    this.url = token.getAuthorizationURL();
     	    twitter.setOAuthAccessToken(new AccessToken(list.get(0), list.get(1)));
     	    System.out.println(twitter.getScreenName());
     	    this.isAuth = true;
     	    this.isDoneAuth = true;
     	    Events.onAuth();
        	
        }catch(Exception e){
        	
        }
	}
    
		
	
	

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
		
	}

	public static void screenshotPost(String text, int width, int height){
		Minecraft mc = Minecraft.getMinecraft();
		try{
			
			
			
			
			
		    System.out.println(System.getProperty("user.dir"));
  			File file = new File(System.getProperty("user.dir") + "\\");
  			if(file.exists()){
  				file.delete();
  			}
  			ScreenShotHelper.saveScreenshot(file, "screenshot.png", width, height, Minecraft.getMinecraft().getFramebuffer());
  			StatusUpdate update = new StatusUpdate(text);
  			update.setMedia(new File(System.getProperty("user.dir") + "\\screenshots\\screenshot.png"));
      		mc.thePlayer.addChatComponentMessage(new ChatComponentText("Posted following tweet with screenshot:"));
    		mc.thePlayer.addChatComponentMessage(new ChatComponentText("@" + Main.twitter.getScreenName() + ": " + text));
    		twitter.updateStatus(update);
		    }catch(Exception e){
		    	e.printStackTrace();
	    		mc.thePlayer.addChatComponentMessage(new ChatComponentText("Error sending tweet! Check logs for more information."));
		    }
	}
	
	
	public static final UserStreamListener listener = new UserStreamListener() {
        @Override
        public void onStatus(Status status) {
        	if(showTweets){
        		try {
					if(status.getUser().getId() != twitter.getId()){
					ScarabUtil.chat("@" + status.getUser().getScreenName() + ": " + status.getText());
					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }

        

        @Override
        public void onFavorite(User source, User target, Status favoritedStatus) {
            ScarabUtil.chat("@" + source.getName() + " liked your Tweet: " + favoritedStatus.getText().substring(0, 20) + "...");
        }


        @Override
        public void onFollow(User source, User followedUser) {
            ScarabUtil.chat(source.getScreenName() + " is now following you!");
        }

        
        @Override
        public void onDirectMessage(DirectMessage directMessage) {
        	try{
        	if(directMessage.getSender().getId() != twitter.getId()){
	            ScarabUtil.chat("Direct message from " + directMessage.getSenderScreenName() + ": " + directMessage.getText());
	            if(!(dms.get(0) == directMessage.getSender().getScreenName())){
	            	if(dms.contains(directMessage.getSender().getScreenName())){
	            		dms.set(dms.indexOf(directMessage.getSender().getScreenName()), dms.get(0));
	            		dms.set(0, directMessage.getSender().getScreenName());
	            	}else{
	            		dms.add(0, directMessage.getSender().getScreenName());
	            		dms.remove(2);
	            	}
	            }
        	}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }



        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
            System.out.println("onException:" + ex.getMessage());
        }



		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onTrackLimitationNotice(int arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onBlock(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onDeletionNotice(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onFavoritedRetweet(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onFriendList(long[] arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onQuotedTweet(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onRetweetedRetweet(User arg0, User arg1, Status arg2) {
			ScarabUtil.chat(arg0.getScreenName() + " retweeted your Tweet: " + arg2.getText().substring(0, 15) + "...");
			
		}



		@Override
		public void onUnblock(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUnfavorite(User arg0, User arg1, Status arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUnfollow(User arg0, User arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserDeletion(long arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListCreation(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListDeletion(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserListUpdate(User arg0, UserList arg1) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserProfileUpdate(User arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onUserSuspension(long arg0) {
			// TODO Auto-generated method stub
			
		}
    };


	private void storeAccessToken(long id, AccessToken accessToken) {
		
	}
		
}
