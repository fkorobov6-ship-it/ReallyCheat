package net.reallycheat;

public abstract class Module {
    private String name;
    private int key;
    private boolean enabled;
    
    public Module(String name, int key) {
        this.name = name;
        this.key = key;
        this.enabled = false;
    }
    
    public String getName() { return name; }
    public int getKey() { return key; }
    public boolean isEnabled() { return enabled; }
    
    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }
    
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onUpdate();
}
