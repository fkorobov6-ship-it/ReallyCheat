package net.reallycheat.modules;

import net.reallycheat.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

import java.util.Arrays;
import java.util.List;

public class XRayModule extends Module {
    private static final List<Block> IMPORTANT_BLOCKS = Arrays.asList(
        Blocks.DIAMOND_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, 
        Blocks.COAL_ORE, Blocks.EMERALD_ORE, Blocks.REDSTONE_ORE,
        Blocks.LAPIS_ORE, Blocks.ANCIENT_DEBRIS, Blocks.CHEST
    );
    
    public XRayModule() {
        super("XRay", GLFW.GLFW_KEY_Z);
    }
    
    @Override
    public void onEnable() {
        for (Block block : IMPORTANT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(block, RenderType.translucent());
        }
        Minecraft.getInstance().levelRenderer.allChanged();
    }
    
    @Override
    public void onDisable() {
        for (Block block : Block.BLOCK_STATE_REGISTRY) {
            RenderTypeLookup.setRenderLayer(block, RenderTypeLookup.getChunkRenderType(block.defaultBlockState()));
        }
        Minecraft.getInstance().levelRenderer.allChanged();
    }
    
    @Override public void onUpdate() {}
}
