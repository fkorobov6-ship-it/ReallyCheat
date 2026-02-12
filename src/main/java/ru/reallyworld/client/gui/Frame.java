package ru.reallyworld.client.gui;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import ru.reallyworld.client.gui.components.Component;
import ru.reallyworld.client.gui.themes.Theme;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private final String title;
    private int x, y, width, height;
    private boolean dragging;
    private int dragX, dragY;
    private boolean expanded = true;
    private final Theme theme;
    private final List<Component> components = new ArrayList<>();

    public Frame(String title, int x, int y, int width, int height, Theme theme) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.theme = theme;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Рамка
        DrawableHelper.fill(matrices, x, y, x + width, y + height, theme.getPrimaryColor());
        DrawableHelper.fill(matrices, x + 1, y + 1, x + width - 1, y + height - 1, theme.getSecondaryColor());

        // Заголовок
        DrawableHelper.fill(matrices, x, y, x + width, y + height, theme.getAccentColor());
        client.textRenderer.drawWithShadow(matrices, title, x + 5, y + 5, 0xFFFFFF);

        // Компоненты
        if (expanded) {
            int offsetY = y + height + 2;
            for (Component component : components) {
                component.setPosition(x + 2, offsetY);
                component.setWidth(width - 4);
                component.render(matrices, mouseX, mouseY, delta);
                offsetY += component.getHeight() + 2;
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY, x, y, width, height)) {
            if (button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            } else if (button == 1) {
                expanded = !expanded;
            }
        }

        if (expanded) {
            components.forEach(component -> component.mouseClicked(mouseX, mouseY, button));
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    private boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public String getTitle() {
        return title;
    }
}
