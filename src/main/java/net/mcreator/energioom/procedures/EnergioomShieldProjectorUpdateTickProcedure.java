package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.block.LivingEnergioomBlockBlock;
import net.mcreator.energioom.BlockUtils;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.EnergioomMod;

import java.util.Map;

public class EnergioomShieldProjectorUpdateTickProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				EnergioomMod.LOGGER.warn("Failed to load dependency world for procedure EnergioomShieldProjectorUpdateTick!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				EnergioomMod.LOGGER.warn("Failed to load dependency x for procedure EnergioomShieldProjectorUpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				EnergioomMod.LOGGER.warn("Failed to load dependency y for procedure EnergioomShieldProjectorUpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				EnergioomMod.LOGGER.warn("Failed to load dependency z for procedure EnergioomShieldProjectorUpdateTick!");
			return;
		}
		World world = (World) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");

		BlockPos pos = new BlockPos(x,y,z);
		BlockPos[] blocks = BlockUtils.GetConnectedBlocks(pos, LivingEnergioomBlockBlock.block,world);

		boolean enabled = ForgeUtils.getNBT(pos, world).getBoolean("protection_enabled");
		boolean explosion_enabled = ForgeUtils.getNBT(pos, world).getBoolean("explosionProtection_enabled");

		boolean isExplosionProof = false;
		double energy_usage = 0;

		for(BlockPos _pos: blocks){
			ForgeUtils.setNBTProp(_pos,"projector_loc",pos,world);
			if (explosion_enabled && enabled) {
				isExplosionProof = EnergyUtils.useEnergy(pos, 4, false, world);
				energy_usage += 4;
			}else if(enabled){
				EnergyUtils.useEnergy(pos, 1, false, world);
				energy_usage += 1;
			}
		}

		ForgeUtils.setNBTProp(pos,"connectedBlocks",blocks,world);
		ForgeUtils.setNBTProp(pos,"energy_usage",energy_usage,world);
		ForgeUtils.setNBTProp(pos,"isExplosionProof",isExplosionProof,world);
	}
}
