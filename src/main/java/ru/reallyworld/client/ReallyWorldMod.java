package ru.reallyworld.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import ru.reallyworld.client.config.ConfigManager;
import ru.reallyworld.client.gui.ClickGUI;
import ru.reallyworld.client.macro.MacroManager;
import ru.reallyworld.client.modules.ModuleManager;
import ru.reallyworld.client.packet.PacketInjector;

public class ReallyWorldMod implements ClientModInitializer {
    public static final String MOD_ID = "reallyworld";
    public static final String MOD_NAME = "ReallyWorld Client";
    public static final String VERSION = "1.0.0";

    public static ReallyWorldMod INSTANCE;
    public static ModuleManager moduleManager;
    public static PacketInjector packetInjector;
    public static MacroManager macroManager;
    public static ConfigManager configManager;
    public static ClickGUI clickGUI;

    private static KeyBinding guiKeyBind;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        moduleManager = new ModuleManager();
        packetInjector = new PacketInjector();
        macroManager = new MacroManager();
        configManager = new ConfigManager();
        clickGUI = new ClickGUI();

        guiKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reallyworld.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RSHIFT,
                "category.reallyworld"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (guiKeyBind.wasPressed()) {
                client.setScreen(clickGUI);
            }
        });

        configManager.load();
        moduleManager.loadModules();
        packetInjector.initialize();
        macroManager.initialize();

        System.out.println(MOD_NAME + " v" + VERSION + " initialized.");
    }
}
