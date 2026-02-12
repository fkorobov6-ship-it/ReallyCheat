package ru.reallyworld.client.macro;

import java.util.ArrayList;
import java.util.List;

public class Macro {
    private String name;
    private int keyBind;
    private List<MacroAction> actions = new ArrayList<>();
    private boolean repeating = false;

    public Macro(String name) {
        this.name = name;
    }

    public void execute() {
        actions.forEach(MacroAction::execute);
    }

    // Геттеры/сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getKeyBind() { return keyBind; }
    public void setKeyBind(int keyBind) { this.keyBind = keyBind; }
    public List<MacroAction> getActions() { return actions; }
    public boolean isRepeating() { return repeating; }
    public void setRepeating(boolean repeating) { this.repeating = repeating; }
}
