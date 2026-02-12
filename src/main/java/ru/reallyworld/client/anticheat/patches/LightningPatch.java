package ru.reallyworld.client.anticheat.patches;

import net.minecraft.client.MinecraftClient;
import ru.reallyworld.client.utils.RandomUtils;

public class LightningPatch {

    // Lightning AntiCheat: обход проверки Ground
    public static boolean spoofGround() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return true;

        // При падении — иногда говорим, что на земле
        if (!mc.player.isOnGround() && mc.player.fallDistance > 1.5f) {
            return RandomUtils.nextDouble(0, 1) < 0.4;
        }
        return mc.player.isOnGround();
    }

    // Обход анти-скорости (симуляция слайд-хопа)
    public static double getMotionY(double original) {
        if (original < -0.08) {
            return -0.078; // чуть меньше лимита
        }
        return original;
    }
}
