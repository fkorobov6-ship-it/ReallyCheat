package net.reallycheat.modules;

import net.reallycheat.Module;
import net.minecraft.client.Minecraft;

public class FlightModule extends Module {
    public FlightModule() {
        super("Flight", GLFW.GLFW_KEY_F);
    }
    
    @Override
    public void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.abilities.mayfly = true;
            mc.player.abilities.flying = true;
        }
    }
    
    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.abilities.mayfly = false;
            mc.player.abilities.flying = false;
        }
    }
    
    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && isEnabled()) {
            mc.player.abilities.mayfly = true;
        }
    }
}
