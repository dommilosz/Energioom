package net.mcreator.energioom.procedures;

import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.potion.EnergioomEffectPotionEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class KillEnderDragonEventProcedure {
    @Mod.EventBusSubscriber
    private static class GlobalTrigger {
        @SubscribeEvent
        public static void onEntityDeath(LivingDeathEvent event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                Entity sourceentity = event.getSource().getTrueSource();
                double i = entity.getPosX();
                double j = entity.getPosY();
                double k = entity.getPosZ();
                World world = entity.world;

                if (sourceentity instanceof PlayerEntity) {
                    if (entity.getType().equals(EntityType.ENDER_DRAGON) && ForgeUtils.hasPotionEffect(sourceentity, EnergioomEffectPotionEffect.potion)) {
                        ForgeUtils.AddAchievement(sourceentity, new ResourceLocation("energioom:win_under_influence"));
                    }
                }

                if (entity instanceof PlayerEntity) {
                    if(event.getSource().damageType.equals("energioom_overdose")){
                        ForgeUtils.AddAchievement(entity,new ResourceLocation("energioom:iwantmore"));
                    }
                }
            }
        }
    }
}
