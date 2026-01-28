package net.reallycheat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod("reallycheat")
public class ReallyCheatMod {
    private static final List<Module> modules = new ArrayList<>();
    private static boolean guiEnabled = false;
    
    public ReallyCheatMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        // Регистрация модулей
        modules.add(new KillAuraModule());
        modules.add(new FlightModule());
        modules.add(new SpeedModule());
        modules.add(new ESPModule());
        modules.add(new XRayModule());
        modules.add(new NoFallModule());
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ModuleManager());
    }
    
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().options.keyShift.isDown()) {
            int y = 10;
            for (Module module : modules) {
                if (module.isEnabled()) {
                    Minecraft.getInstance().font.draw(event.getMatrixStack(), 
                        module.getName(), 10, y, 0xFFFFFF);
                    y += 10;
                }
            }
        }
    }
    
    public static List<Module> getModules() { return modules; }
    public static boolean isGuiEnabled() { return guiEnabled; }
    public static void toggleGui() { guiEnabled = !guiEnabled; }
}
