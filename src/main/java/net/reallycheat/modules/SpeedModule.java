package net.reallycheat.modules;

import net.reallycheat.Module;
import net.minecraft.client.Minecraft;

public class SpeedModule extends Module {
    public SpeedModule() {
        super("Speed", GLFW.GLFW_KEY_R);
    }
    
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    
    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && isEnabled()) {
            double speed = 0.2;
            if (mc.player.isSprinting()) speed = 0.3;
            
            double motionX = mc.player.getDeltaMovement().x * speed;
            double motionZ = mc.player.getDeltaMovement().z * speed;
            mc.player.setDeltaMovement(motionX, mc.player.getDeltaMovement().y, motionZ);
        }
    }
}
