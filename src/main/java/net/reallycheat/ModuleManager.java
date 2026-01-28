package net.reallycheat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ModuleManager {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            ReallyCheatMod.toggleGui();
            return;
        }
        
        for (Module module : ReallyCheatMod.getModules()) {
            if (event.getKey() == module.getKey()) {
                module.toggle();
            }
        }
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (Module module : ReallyCheatMod.getModules()) {
                if (module.isEnabled()) {
                    module.onUpdate();
                }
            }
        }
    }
}
