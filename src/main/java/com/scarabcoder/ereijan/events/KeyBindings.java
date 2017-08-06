package com.scarabcoder.ereijan.events;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding auth;

    public static void init() {
        // Define the "ping" binding, with (unlocalized) name "key.ping" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 24 ("O", LWJGL constant: Keyboard.KEY_O)
        auth = new KeyBinding("key.auth", Keyboard.KEY_O, "key.categories.sem");

        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(auth);
    }
}
