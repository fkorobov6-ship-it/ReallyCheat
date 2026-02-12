package ru.reallyworld.client.macro;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.reallyworld.client.ReallyWorldMod;

public class MacroGUI extends Screen {
    private Macro selectedMacro = null;

    public MacroGUI() {
        super(Text.literal("ReallyWorld Macros"));
    }

    @Override
    protected void init() {
        int y = 30;
        for (Macro macro : ReallyWorldMod.macroManager.getMacros()) {
            this.addDrawableChild(new ButtonWidget(10, y, 100, 20, 
                    Text.literal(macro.getName()), 
                    button -> selectMacro(macro)
            ));
            y += 25;
        }

        this.addDrawableChild(new ButtonWidget(10, height - 30, 100, 20,
                Text.literal("New Macro"),
                button -> ReallyWorldMod.macroManager.createMacro()
        ));

        this.addDrawableChild(new ButtonWidget(120, height - 30, 100, 20,
                Text.literal("Record"),
                button -> ReallyWorldMod.macroManager.startRecording()
        ));

        this.addDrawableChild(new ButtonWidget(230, height - 30, 100, 20,
                Text.literal("Save All"),
                button -> ReallyWorldMod.configManager.save()
        ));
    }

    private void selectMacro(Macro macro) {
        this.selectedMacro = macro;
        // Здесь открывается редактор действий
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
