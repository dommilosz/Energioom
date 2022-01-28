package net.mcreator.energioom.procedures;

import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.VariableUtils;
import net.mcreator.energioom.potion.EnergioomEffectPotionEffect;
import net.minecraft.entity.Entity;

import net.mcreator.energioom.EnergioomMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class PowderedEnergioomFoodEatenProcedure {

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            if (!dependencies.containsKey("entity"))
                EnergioomMod.LOGGER.warn("Failed to load dependency entity for procedure PowderedEnergioomFoodEaten!");
            return;
        }
        Entity entity = (Entity) dependencies.get("entity");

        VariableUtils.GetPlayerVariables((PlayerEntity) entity).energioom_eaten += 1;
        int energioomEaten = (int) VariableUtils.GetPlayerVariables((PlayerEntity) entity).energioom_eaten;
        if (energioomEaten >= 4) {
            entity.attackEntityFrom(new DamageSource("energioom_overdose").setDamageBypassesArmor(), (float) 20000);
        }
        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.ABSORPTION, 120*20, 4));
        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.REGENERATION, 20*20, 1));
        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 300*20, 0));
        ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 300*20, 0));
        ((LivingEntity) entity).addPotionEffect(new EffectInstance(EnergioomEffectPotionEffect.potion, 300*20, 0));
        VariableUtils.SyncPlayerVariables((PlayerEntity) entity);
    }
}
