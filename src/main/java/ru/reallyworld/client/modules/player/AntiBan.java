package ru.reallyworld.client.modules.player;

import net.minecraft.client.MinecraftClient;
import ru.reallyworld.client.modules.Module;

public class AntiBan extends Module {
    private long lastStaffCheck = 0;

    public AntiBan() {
        super("AntiBan", "Автовыход при появлении админа", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (MinecraftClient.getInstance().player == null) return;

        // Простейшая детекция: игроки с правами
        MinecraftClient.getInstance().world.getPlayers().forEach(player -> {
            if (player.hasPermissionLevel(2) && !player.equals(MinecraftClient.getInstance().player)) {
                if (System.currentTimeMillis() - lastStaffCheck > 5000) {
                    MinecraftClient.getInstance().player.networkHandler.getConnection().disconnect(
                            net.minecraft.text.Text.literal("AntiBan: staff detected")
                    );
                    lastStaffCheck = System.currentTimeMillis();
                }
            }
        });
    }
}
