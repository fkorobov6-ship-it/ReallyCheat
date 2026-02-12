package ru.reallyworld.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.reallyworld.client.gui.components.*;
import ru.reallyworld.client.gui.themes.DarkTheme;
import ru.reallyworld.client.modules.Module;
import ru.reallyworld.client.modules.ModuleManager;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    private final List<Frame> frames = new ArrayList<>();
    private final DarkTheme theme = new DarkTheme();

    public ClickGUI() {
        super(Text.literal("ReallyWorld GUI"));

        int x = 10;
        int y = 10;
        int width = 120;

        frames.add(new Frame("Combat", x, y, width, 20, theme));
        frames.add(new Frame("Movement", x + width + 5, y, width, 20, theme));
        frames.add(new Frame("Player", x + (width + 5) * 2, y, width, 20, theme));
        frames.add(new Frame("Macros", x + (width + 5) * 3, y, width, 20, theme));
        frames.add(new Frame("Config", x + (width + 5) * 4, y, width, 20, theme));

        // Заполняем модулями
        for (Module module : ModuleManager.getModules()) {
            for (Frame frame : frames) {
                if (frame.getTitle().equalsIgnoreCase(module.getCategory())) {
                    frame.addComponent(new Checkbox(module.getName(), module.isEnabled(), module::toggle));
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        frames.forEach(frame -> frame.render(matrices, mouseX, mouseY, delta));
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        frames.forEach(frame -> frame.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
