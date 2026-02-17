package ru.reallycheat.modules.visuals;

import ru.reallycheat.modules.*;
import ru.reallycheat.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import java.awt.*;

public class ESP extends Module {
    private Mode mode = Mode.BOX_2D;
    private boolean healthBar = true;
    private boolean nameTags = true;
    private boolean distance = true;
    private boolean armor = true;
    private boolean throughWalls = true;
    private Color playerColor = Color.RED;
    private Color friendColor = Color.GREEN;
    private Color enemyColor = Color.ORANGE;
    
    public enum Mode {
        BOX_2D, BOX_3D, CORNER_BOX, CIRCLE, NONE
    }
    
    public ESP() {
        super("ESP", "Подсветка игроков сквозь стены", Category.VISUALS);
        this.setKey(33); // F по умолчанию
    }
    
    @Override
    public void onRenderWorld(float partialTicks) {
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) o;
            
            if (entity == mc.thePlayer) continue;
            if (!(entity instanceof EntityPlayer)) continue;
            
            renderESP((EntityPlayer) entity, partialTicks);
        }
    }
    
    private void renderESP(EntityPlayer player, float partialTicks) {
        double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        
        double minX = x - 0.35;
        double minY = y;
        double minZ = z - 0.35;
        double maxX = x + 0.35;
        double maxY = y + player.height + 0.2;
        double maxZ = z + 0.35;
        
        Color color = getPlayerColor(player);
        
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        
        switch (mode) {
            case BOX_2D:
                render2DBox(minX, minY, minZ, maxX, maxY, maxZ, color);
                break;
            case BOX_3D:
                render3DBox(minX, minY, minZ, maxX, maxY, maxZ, color);
                break;
            case CORNER_BOX:
                renderCornerBox(minX, minY, minZ, maxX, maxY, maxZ, color);
                break;
            case CIRCLE:
                renderCircle(x, y + player.height / 2, z, player, color);
                break;
        }
        
        if (healthBar) {
            renderHealthBar(minX - 0.2, minY, minZ, player);
        }
        
        if (nameTags) {
            renderNameTag(x, maxY + 0.3, z, player);
        }
        
        if (distance) {
            renderDistance(x, minY - 0.2, z, player);
        }
        
        if (armor) {
            renderArmor(x, minY - 0.4, z, player);
        }
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }
    
    private void render2DBox(double minX, double minY, double minZ, 
                             double maxX, double maxY, double maxZ, Color color) {
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 0.3f);
        
        // Заливка
        GL11.glBegin(GL11.GL_QUADS);
        // Лицевая сторона
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glEnd();
        
        // Контур
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 1f);
        GL11.glLineWidth(2f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glEnd();
    }
    
    private void render3DBox(double minX, double minY, double minZ,
                             double maxX, double maxY, double maxZ, Color color) {
        // Полный 3D бокс со всеми гранями
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 0.2f);
        
        // Рисование всех 6 граней
        renderQuads(minX, minY, minZ, maxX, maxY, maxZ);
        
        // Контур
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 1f);
        GL11.glLineWidth(2f);
        renderOutline(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    private void renderCornerBox(double minX, double minY, double minZ,
                                 double maxX, double maxY, double maxZ, Color color) {
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 1f);
        GL11.glLineWidth(2f);
        
        double cornerSize = 0.2;
        
        // Углы
        GL11.glBegin(GL11.GL_LINES);
        // Нижние углы
        renderCorner(minX, minY, minZ, cornerSize, Axis.X);
        renderCorner(minX, minY, minZ, cornerSize, Axis.Y);
        renderCorner(minX, minY, minZ, cornerSize, Axis.Z);
        
        renderCorner(maxX, minY, minZ, cornerSize, Axis.X_NEG);
        renderCorner(maxX, minY, minZ, cornerSize, Axis.Y);
        renderCorner(maxX, minY, minZ, cornerSize, Axis.Z);
        
        renderCorner(minX, minY, maxZ, cornerSize, Axis.X);
        renderCorner(minX, minY, maxZ, cornerSize, Axis.Y);
        renderCorner(minX, minY, maxZ, cornerSize, Axis.Z_NEG);
        
        renderCorner(maxX, minY, maxZ, cornerSize, Axis.X_NEG);
        renderCorner(maxX, minY, maxZ, cornerSize, Axis.Y);
        renderCorner(maxX, minY, maxZ, cornerSize, Axis.Z_NEG);
        
        // Верхние углы
        renderCorner(minX, maxY, minZ, cornerSize, Axis.X);
        renderCorner(minX, maxY, minZ, cornerSize, Axis.Y_NEG);
        renderCorner(minX, maxY, minZ, cornerSize, Axis.Z);
        
        renderCorner(maxX, maxY, minZ, cornerSize, Axis.X_NEG);
        renderCorner(maxX, maxY, minZ, cornerSize, Axis.Y_NEG);
        renderCorner(maxX, maxY, minZ, cornerSize, Axis.Z);
        
        renderCorner(minX, maxY, maxZ, cornerSize, Axis.X);
        renderCorner(minX, maxY, maxZ, cornerSize, Axis.Y_NEG);
        renderCorner(minX, maxY, maxZ, cornerSize, Axis.Z_NEG);
        
        renderCorner(maxX, maxY, maxZ, cornerSize, Axis.X_NEG);
        renderCorner(maxX, maxY, maxZ, cornerSize, Axis.Y_NEG);
        renderCorner(maxX, maxY, maxZ, cornerSize, Axis.Z_NEG);
        GL11.glEnd();
    }
    
    private void renderCircle(double x, double y, double z, 
                              EntityPlayer player, Color color) {
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 0.3f);
        
        int segments = 32;
        double radius = 0.5;
        
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex3d(x, y, z);
        
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double dx = Math.sin(angle) * radius;
            double dz = Math.cos(angle) * radius;
            GL11.glVertex3d(x + dx, y, z + dz);
        }
        GL11.glEnd();
        
        GL11.glColor4f(color.getRed()/255f, color.getGreen()/255f, 
                       color.getBlue()/255f, 1f);
        GL11.glLineWidth(2f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        
        for (int i = 0; i <= segments; i++) {
            double angle = 2 * Math.PI * i / segments;
            double dx = Math.sin(angle) * radius;
            double dz = Math.cos(angle) * radius;
            GL11.glVertex3d(x + dx, y, z + dz);
        }
        GL11.glEnd();
    }
    
    private void renderHealthBar(double x, double y, double z, EntityPlayer player) {
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float percentage = health / maxHealth;
        
        Color healthColor = percentage > 0.6 ? Color.GREEN : 
                           percentage > 0.3 ? Color.YELLOW : Color.RED;
        
        double barWidth = 0.8;
        double barHeight = 0.1;
        double filledWidth = barWidth * percentage;
        
        GL11.glColor4f(0, 0, 0, 0.5f);
        renderBar(x - barWidth/2, y, z - 0.5, x + barWidth/2, y + barHeight, z - 0.5);
        
        GL11.glColor4f(healthColor.getRed()/255f, healthColor.getGreen()/255f,
                       healthColor.getBlue()/255f, 1f);
        renderBar(x - barWidth/2, y, z - 0.5, 
                  x - barWidth/2 + filledWidth, y + barHeight, z - 0.5);
    }
    
    private void renderNameTag(double x, double y, double z, EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        int width = mc.fontRendererObj.getStringWidth(name);
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glNormal3f(0, 1, 0);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1, 0, 0);
        GL11.glScalef(-0.03f, -0.03f, 0.03f);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        
        // Фон
        RenderUtils.drawRect(-width/2 - 2, -10, width/2 + 2, 5, 
                             new Color(0, 0, 0, 100).getRGB());
        
        // Текст
        mc.fontRendererObj.drawStringWithShadow(name, -width/2, -8, 0xFFFFFF);
        
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
    private void renderDistance(double x, double y, double z, EntityPlayer player) {
        double distance = mc.thePlayer.getDistanceToEntity(player);
        String distText = String.format("%.1fm", distance);
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glNormal3f(0, 1, 0);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1, 0, 0);
        GL11.glScalef(-0.02f, -0.02f, 0.02f);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.fontRendererObj.drawStringWithShadow(distText, -20, 0, 0xAAAAAA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glPopMatrix();
    }
    
    private void renderArmor(double x, double y, double z, EntityPlayer player) {
        int armorValue = player.getTotalArmorValue();
        String armorText = "Armor: " + armorValue;
        
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glNormal3f(0, 1, 0);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GL11.glRotatef(mc.getRenderManager().playerViewX, 1, 0, 0);
        GL11.glScalef(-0.02f, -0.02f, 0.02f);
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.fontRendererObj.drawStringWithShadow(armorText, -25, 15, 0x55FF55);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glPopMatrix();
    }
    
    private void renderBar(double x1, double y1, double z1, 
                           double x2, double y2, double z2) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y1, z1);
        GL11.glVertex3d(x2, y2, z1);
        GL11.glVertex3d(x1, y2, z1);
        GL11.glEnd();
    }
    
    private void renderCorner(double x, double y, double z, 
                              double size, Axis axis) {
        switch (axis) {
            case X:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x + size, y, z);
                break;
            case X_NEG:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x - size, y, z);
                break;
            case Y:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x, y + size, z);
                break;
            case Y_NEG:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x, y - size, z);
                break;
            case Z:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x, y, z + size);
                break;
            case Z_NEG:
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(x, y, z - size);
                break;
        }
    }
    
    private enum Axis { X, X_NEG, Y, Y_NEG, Z, Z_NEG }
    
    private void renderQuads(double minX, double minY, double minZ,
                             double maxX, double maxY, double maxZ) {
        GL11.glBegin(GL11.GL_QUADS);
        // Front
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(minX, maxY, minZ);
        // Back
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        // Left
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, minZ);
        // Right
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        // Bottom
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        // Top
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glEnd();
    }
    
    private void renderOutline(double minX, double minY, double minZ,
                               double maxX, double maxY, double maxZ) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        // Bottom
        GL11.glVertex3d(minX, minY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(minX, minY, minZ);
        // Top
        GL11.glVertex3d(minX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, maxY, minZ);
        // Connections
        GL11.glVertex3d(minX, maxY, maxZ);
        GL11.glVertex3d(minX, minY, maxZ);
        GL11.glVertex3d(maxX, minY, maxZ);
        GL11.glVertex3d(maxX, maxY, maxZ);
        GL11.glVertex3d(maxX, maxY, minZ);
        GL11.glVertex3d(maxX, minY, minZ);
        GL11.glEnd();
    }
    
    private Color getPlayerColor(EntityPlayer player) {
        if (FriendManager.getInstance().isFriend(player.getName())) {
            return friendColor;
        }
        if (player == KillAura.getInstance().getTarget()) {
            return enemyColor;
        }
        return playerColor;
    }
}
