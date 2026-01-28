package net.reallycheat.modules;

import net.reallycheat.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;

public class KillAuraModule extends Module {
    public KillAuraModule() {
        super("KillAura", GLFW.GLFW_KEY_K);
    }
    
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    
    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof PlayerEntity && entity != mc.player) {
                PlayerEntity target = (PlayerEntity) entity;
                if (mc.player.distanceTo(target) < 5.0f) {
                    mc.gameMode.attack(mc.player, target);
                    mc.player.swing(Hand.MAIN_HAND);
                    break;
                }
            }
        }
    }
}
