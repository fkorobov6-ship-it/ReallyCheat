package net.reallycheat.modules;

import net.reallycheat.Module;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;

public class ESPModule extends Module {
    public ESPModule() {
        super("ESP", GLFW.GLFW_KEY_X);
    }
    
    @Override public void onEnable() {}
    @Override public void onDisable() {}
    @Override public void onUpdate() {}
}
