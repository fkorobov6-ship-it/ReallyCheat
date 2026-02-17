package ru.reallycheat.modules.combat;

import ru.reallycheat.modules.*;
import ru.reallycheat.utils.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import java.util.*;

public class KillAura extends Module {
    private Entity target;
    private List<Entity> targets = new ArrayList<>();
    private long lastAttack = 0;
    private int cps = 12;
    private double range = 4.2;
    private Mode mode = Mode.SINGLE;
    private Priority priority = Priority.DISTANCE;
    private boolean autoBlock = true;
    private boolean throughWalls = false;
    private boolean players = true;
    private boolean mobs = false;
    private boolean animals = false;
    private boolean invisibles = false;
    private boolean teams = false;
    
    public enum Mode { SINGLE, MULTI, SWITCH }
    public enum Priority { DISTANCE, HEALTH, ARMOR, HURTTIME }
    
    public KillAura() {
        super("KillAura", "Автоматическая атака врагов", Category.COMBAT);
        this.setKey(19); // R по умолчанию
    }
    
    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        // Поиск целей
        findTargets();
        
        // Атака в зависимости от режима
        switch (mode) {
            case SINGLE:
                attackSingle();
                break;
            case MULTI:
                attackMulti();
                break;
            case SWITCH:
                attackSwitch();
                break;
        }
        
        // Авто-блокировка
        if (autoBlock && target != null) {
            block();
        }
    }
    
    private void findTargets() {
        targets.clear();
        
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) o;
            
            if (!isValidEntity(entity)) continue;
            
            double dist = mc.thePlayer.getDistanceToEntity(entity);
            if (dist <= range) {
                targets.add(entity);
            }
        }
        
        // Сортировка по приоритету
        sortTargets();
    }
    
    private boolean isValidEntity(Entity entity) {
        if (entity == mc.thePlayer) return false;
        if (entity.isDead) return false;
        if (!throughWalls && !mc.thePlayer.canEntityBeSeen(entity)) return false;
        if (entity instanceof EntityPlayer) {
            if (!players) return false;
            if (!invisibles && entity.isInvisible()) return false;
            if (teams && isTeam((EntityPlayer) entity)) return false;
        } else if (entity instanceof EntityMob) {
            if (!mobs) return false;
        } else if (entity instanceof EntityAnimal) {
            if (!animals) return false;
        } else {
            return false;
        }
        return true;
    }
    
    private void sortTargets() {
        switch (priority) {
            case DISTANCE:
                targets.sort((e1, e2) -> 
                    Double.compare(mc.thePlayer.getDistanceToEntity(e1), 
                                  mc.thePlayer.getDistanceToEntity(e2)));
                break;
            case HEALTH:
                targets.sort((e1, e2) -> 
                    Float.compare(getHealth(e1), getHealth(e2)));
                break;
            case ARMOR:
                targets.sort((e1, e2) -> 
                    Float.compare(getArmorValue(e2), getArmorValue(e1)));
                break;
            case HURTTIME:
                targets.sort((e1, e2) -> 
                    Integer.compare(e2.hurtResistantTime, e1.hurtResistantTime));
                break;
        }
    }
    
    private void attackSingle() {
        if (targets.isEmpty()) {
            target = null;
            return;
        }
        
        target = targets.get(0);
        
        if (canAttack()) {
            attack(target);
        }
    }
    
    private void attackMulti() {
        if (targets.isEmpty()) return;
        
        for (Entity entity : targets) {
            if (canAttack()) {
                attack(entity);
            }
        }
    }
    
    private void attackSwitch() {
        if (targets.isEmpty()) return;
        
        // Ротация целей
        int index = targets.indexOf(target);
        index = (index + 1) % targets.size();
        target = targets.get(index);
        
        if (canAttack()) {
            attack(target);
        }
    }
    
    private boolean canAttack() {
        long now = System.currentTimeMillis();
        long delay = 1000 / cps;
        
        if (now - lastAttack >= delay) {
            lastAttack = now;
            return true;
        }
        
        return false;
    }
    
    private void attack(Entity entity) {
        // Авто-выбор оружия
        if (mc.thePlayer.getCurrentEquippedItem() == null || 
            !isWeapon(mc.thePlayer.getCurrentEquippedItem().getItem())) {
            selectBestWeapon();
        }
        
        // Атака
        mc.playerController.attackEntity(mc.thePlayer, entity);
        mc.thePlayer.swingItem();
        
        // Критические удары
        if (Criticals.getInstance().isEnabled()) {
            doCriticalHit();
        }
    }
    
    private void block() {
        if (mc.thePlayer.getCurrentEquippedItem() != null && 
            mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, 
                                           mc.thePlayer.getCurrentEquippedItem());
        }
    }
    
    private float getHealth(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            return ((EntityLivingBase) entity).getHealth();
        }
        return 20.0f;
    }
    
    private float getArmorValue(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer) entity).getTotalArmorValue();
        }
        return 0.0f;
    }
    
    private boolean isTeam(EntityPlayer player) {
        // Проверка на команду по цвету скина или префиксу
        return false;
    }
    
    private boolean isWeapon(Item item) {
        return item instanceof ItemSword || 
               item instanceof ItemAxe || 
               item instanceof ItemBow;
    }
    
    private void selectBestWeapon() {
        int bestSlot = -1;
        float bestDamage = 0;
        
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) continue;
            
            float damage = getItemDamage(stack.getItem());
            if (damage > bestDamage) {
                bestDamage = damage;
                bestSlot = i;
            }
        }
        
        if (bestSlot != -1) {
            mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }
    
    private float getItemDamage(Item item) {
        if (item instanceof ItemSword) {
            return ((ItemSword) item).getDamageVsEntity() + 4;
        } else if (item instanceof ItemAxe) {
            return ((ItemAxe) item).getDamageVsEntity() + 3;
        }
        return 1;
    }
    
    private void doCriticalHit() {
        if (mc.thePlayer.onGround && !mc.thePlayer.isInWater()) {
            mc.thePlayer.motionY = 0.1;
            mc.thePlayer.fallDistance = 0.1f;
            mc.thePlayer.onGround = false;
        }
    }
}
