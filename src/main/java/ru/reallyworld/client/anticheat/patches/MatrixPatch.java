package ru.reallyworld.client.anticheat.patches;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import ru.reallyworld.client.utils.RandomUtils;

public class MatrixPatch {

    // Matrix BadPackets: рандомизация интервалов
    public static long getRandomizedDelay() {
        return RandomUtils.nextLong(45, 55); // вместо 50 мс
    }

    // Matrix NoSlow: подмена UseItem пакета
    public static boolean shouldSpoofUseItem() {
        return true;
    }

    // Обход Timer check
    public static float getTimerSpeed(float original) {
        return 1.0f; // Не меняем таймер, симулируем растяжку через пакеты
    }

    // Matrix Fly check — имитация падения
    public static PlayerMoveC2SPacket spoofGroundState(PlayerMoveC2SPacket packet) {
        if (!packet.isOnGround() && RandomUtils.nextDouble(0, 1) < 0.3) {
            return new PlayerMoveC2SPacket.PositionAndOnGround(
                    packet.getX(0),
                    packet.getY(0) - 0.0001,
                    packet.getZ(0),
                    true
            );
        }
        return packet;
    }
}
