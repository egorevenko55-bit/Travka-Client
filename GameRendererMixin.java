package com.travka.client.modules.combat;

import com.travka.client.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAuraModule extends Module {

    // Range in blocks
    private final float range = 4.0f;
    // Attack only players, or mobs too?
    private boolean attackPlayers = true;
    private boolean attackMobs = true;

    private int tickCounter = 0;
    // Attack cooldown: attack every N ticks (20 ticks = 1 second)
    private final int attackDelay = 10;

    public KillAuraModule() {
        super("KillAura", "Автоматична атака ворогів навколо тебе", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return; // Don't attack when GUI open

        tickCounter++;
        if (tickCounter < attackDelay) return;
        tickCounter = 0;

        // Check attack cooldown
        if (client.player.getAttackCooldownProgress(0f) < 1.0f) return;

        List<Entity> targets = client.world.getEntitiesByClass(LivingEntity.class,
                client.player.getBoundingBox().expand(range),
                entity -> {
                    if (entity == client.player) return false;
                    if (!entity.isAlive()) return false;
                    if (attackPlayers && entity instanceof PlayerEntity) return true;
                    if (attackMobs && entity instanceof HostileEntity) return true;
                    return false;
                }
        );

        if (targets.isEmpty()) return;

        // Attack closest
        targets.sort(Comparator.comparingDouble(e -> client.player.squaredDistanceTo(e)));
        Entity target = targets.get(0);

        // Look at target (optional - smoother feel)
        client.player.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,
                target.getPos().add(0, target.getHeight() / 2.0, 0));

        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }
}
