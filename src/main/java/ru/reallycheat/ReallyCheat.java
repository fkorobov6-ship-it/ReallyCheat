package ru.reallycheat;

import ru.reallycheat.core.*;
import ru.reallycheat.modules.*;
import ru.reallycheat.gui.*;
import ru.reallycheat.utils.*;

public class ReallyCheat {
    private static ReallyCheat instance;
    private ReallyCheatCore core;
    private ModuleManager moduleManager;
    private ClickGUI clickGUI;
    private ConfigManager configManager;
    private boolean isInitialized = false;
    
    public static ReallyCheat getInstance() {
        if (instance == null) {
            instance = new ReallyCheat();
        }
        return instance;
    }
    
    public void init() {
        if (isInitialized) return;
        
        System.out.println("[ReallyCheat] Initializing...");
        
        // Инициализация ядра
        core = new ReallyCheatCore();
        core.init();
        
        // Загрузка менеджера модулей
        moduleManager = new ModuleManager();
        moduleManager.loadModules();
        
        // Инициализация GUI
        clickGUI = new ClickGUI();
        
        // Загрузка конфигурации
        configManager = new ConfigManager();
        configManager.loadDefaultConfig();
        
        // Инжект в Minecraft
        injectIntoMinecraft();
        
        isInitialized = true;
        System.out.println("[ReallyCheat] Initialized successfully!");
    }
    
    private void injectIntoMinecraft() {
        // Инжектирование через ASM
        core.getInjectionManager().inject();
    }
    
    public ReallyCheatCore getCore() { return core; }
    public ModuleManager getModuleManager() { return moduleManager; }
    public ClickGUI getClickGUI() { return clickGUI; }
    public ConfigManager getConfigManager() { return configManager; }
}
