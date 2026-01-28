package net.reallycheat.modules;

import net.reallycheat.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPlayerPacket;

public class NoFallModule extends Module {
    public NoFallModule() {
        super("NoFall", GLFW.GLFW_KEY_N);
    }
    
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    
    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && isEnabled() && mc.player.fallDistance > 2.0f) {
            mc.player.connection.send(new CPlayerPacket.OnGroundPacket(true));
            mc.player.fallDistance = 0;
        }
    }
}
