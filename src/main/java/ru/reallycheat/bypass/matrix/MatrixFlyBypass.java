package ru.reallycheat.core.bypass.matrix;

import ru.reallycheat.core.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;

public class MatrixFlyBypass {
    private static MatrixFlyBypass instance;
    private int flyTicks = 0;
    private double lastY = 0;
    private boolean isFlying = false;
    private Mode currentMode = Mode.PACKET_SPAM;
    
    public enum Mode {
        PACKET_SPAM,
        MOTION_MODIFY,
        GLIDE_COMBINATION,
        VANILLA_SIMULATION
    }
    
    public static MatrixFlyBypass getInstance() {
        if (instance == null) {
            instance = new MatrixFlyBypass();
        }
        return instance;
    }
    
    public void onFly() {
        if (!isFlying) {
            isFlying = true;
            flyTicks = 0;
        }
        
        flyTicks++;
        
        switch (currentMode) {
            case PACKET_SPAM:
                packetSpamBypass();
                break;
            case MOTION_MODIFY:
                motionModifyBypass();
                break;
            case GLIDE_COMBINATION:
                glideCombinationBypass();
                break;
            case VANILLA_SIMULATION:
                vanillaSimulationBypass();
                break;
        }
    }
    
    private void packetSpamBypass() {
        // Отправка спама пакетами для сбивания проверок
        if (flyTicks % 3 == 0) {
            sendPositionPacket(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ, false);
        }
        if (flyTicks % 7 == 0) {
            sendPositionPacket(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, true);
        }
    }
    
    private void motionModifyBypass() {
        // Модификация движения для обхода
        double yMotion = 0.05 * Math.sin(flyTicks * 0.5);
        mc.thePlayer.motionY = yMotion;
        
        if (flyTicks % 20 == 0) {
            mc.thePlayer.motionY = 0.4;
        }
    }
    
    private void glideCombinationBypass() {
        // Комбинация глида и рывков
        if (flyTicks < 10) {
            mc.thePlayer.motionY = 0.3;
        } else if (flyTicks < 30) {
            mc.thePlayer.motionY = -0.05;
        } else {
            mc.thePlayer.motionY = 0;
            flyTicks = 0;
        }
    }
    
    private void vanillaSimulationBypass() {
        // Симуляция ванильного полета
        if (mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.isFlying = false;
        }
        
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY = 0.3;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY = -0.3;
        }
    }
    
    private void sendPositionPacket(double x, double y, double z, boolean onGround) {
        C03PacketPlayer.C04PacketPlayerPosition packet = 
            new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, onGround);
        mc.getNetHandler().addToSendQueue(packet);
    }
    
    public void setMode(Mode mode) {
        this.currentMode = mode;
    }
    
    public void onDisable() {
        isFlying = false;
        flyTicks = 0;
    }
}
