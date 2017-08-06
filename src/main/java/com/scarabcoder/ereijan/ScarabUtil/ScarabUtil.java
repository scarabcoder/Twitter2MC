package com.scarabcoder.ereijan.ScarabUtil;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

public class ScarabUtil {
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	/**
	 * Opens a webpage with the URL specified.
	 * 
	 * 
	 * 
	 * @param string
	 */
	public static void openWebpage(String string) {
		
		
	    try {
	    	URL url = new URL(string);
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void chat(String str){
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString(str));
	}
	
	public static String fileToString(File file){
		Scanner myScanner = null;
		String contents = "";
		try
		{
		    try {
				myScanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    contents = myScanner.useDelimiter("\\Z").next(); 
		}
		finally
		{
		    if(myScanner != null)
		    {
		        myScanner.close(); 
		    }
		}
		return contents;
	}
}
