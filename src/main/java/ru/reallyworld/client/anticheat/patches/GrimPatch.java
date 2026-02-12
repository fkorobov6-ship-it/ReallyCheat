package ru.reallyworld.client.anticheat.patches;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import ru.reallyworld.client.utils.RandomUtils;
import ru.reallyworld.client.utils.RotationUtils;

public class GrimPatch {

    private static int grimFlagCounter = 0;
    private static long lastFlagTime = 0;

    // Обход проверки Fly/AirJump в Grim
    public static boolean shouldCancelJump(PlayerEntity player) {
        if (player.isOnGround()) return false;

        // Симуляция легитимного "залипания" на блоке
        long timeSinceFlag = System.currentTimeMillis() - lastFlagTime;
        if (timeSinceFlag < 3000) {
            grimFlagCounter++;
            if (grimFlagCounter > 5) {
                // Принудительный ground spoof
                MinecraftClient.getInstance().player.setOnGround(true);
                lastFlagTime = System.currentTimeMillis();
                grimFlagCounter = 0;
                return true;
            }
        }
        return false;
    }

    // Обход проверки Reach (досягаемости)
    public static double getReach(double original) {
        if (!MinecraftClient.getInstance().player.isSprinting()) {
            return Math.min(original, 3.05 + RandomUtils.nextDouble(0, 0.03));
        }
        return Math.min(original, 3.1);
    }

    // Обход Velocity (анти-кикбэк)
    public static boolean shouldCancelVelocity() {
        return RandomUtils.nextInt(0, 100) < 65; // 65% шанс отмены
    }
}
