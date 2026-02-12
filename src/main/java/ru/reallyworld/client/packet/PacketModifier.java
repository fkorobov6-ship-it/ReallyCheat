package ru.reallyworld.client.packet;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import ru.reallyworld.client.modules.ModuleManager;
import ru.reallyworld.client.modules.movement.NoSlow;
import ru.reallyworld.client.modules.movement.Speed;
import ru.reallyworld.client.utils.RandomUtils;

public class PacketModifier {

    public static Packet<?> modifyOutgoing(Packet<?> packet) {
        if (packet instanceof PlayerMoveC2SPacket) {
            return modifyMovement((PlayerMoveC2SPacket) packet);
        }

        if (packet instanceof PlayerInteractBlockC2SPacket) {
            return modifyInteraction((PlayerInteractBlockC2SPacket) packet);
        }

        return packet;
    }

    private static Packet<?> modifyMovement(PlayerMoveC2SPacket packet) {
        if (ModuleManager.isModuleEnabled("Speed")) {
            // Рандомизация таймингов + сглаживание
            return new PlayerMoveC2SPacket.PositionAndOnGround(
                    packet.getX(0),
                    packet.getY(0) + RandomUtils.nextDouble(0.0001, 0.0003),
                    packet.getZ(0),
                    packet.isOnGround()
            );
        }
        return packet;
    }

    private static Packet<?> modifyInteraction(PlayerInteractBlockC2SPacket packet) {
        if (ModuleManager.isModuleEnabled("NoSlow")) {
            // Симуляция использования предмета без замедления
            return new PlayerInteractBlockC2SPacket(packet.getHand(), packet.getBlockHitResult(), 0);
        }
        return packet;
    }
}
