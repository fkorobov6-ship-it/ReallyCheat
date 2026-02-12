package ru.reallyworld.client.injection.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.reallyworld.client.ReallyWorldMod;
import ru.reallyworld.client.anticheat.spoofers.PositionSpoofer;
import ru.reallyworld.client.anticheat.spoofers.TransactionSpoofer;
import ru.reallyworld.client.modules.player.AntiTP;
import ru.reallyworld.client.packet.PacketModifier;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (!ReallyWorldMod.moduleManager.isModuleEnabled("PacketInjector")) return;

        Packet<?> modified = PacketModifier.modifyOutgoing(packet);

        if (modified == null) {
            ci.cancel();
            return;
        }

        // Специфичные модификации
        if (packet instanceof PlayerMoveC2SPacket) {
            modified = PositionSpoofer.spoof((PlayerMoveC2SPacket) packet);
        }

        if (packet instanceof ClickWindowC2SPacket || packet instanceof CreativeInventoryActionC2SPacket) {
            modified = TransactionSpoofer.spoof((Packet<?>) packet);
        }

        // Тут технически нужно подменить пакет в оригинальном методе,
        // но для краткости демонстрируем логику перехвата.
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerPositionLookS2CPacket) {
            if (ReallyWorldMod.moduleManager.getModule(AntiTP.class).isEnabled()) {
                ci.cancel(); // Блокируем серверную телепортацию
            }
        }
    }
}
