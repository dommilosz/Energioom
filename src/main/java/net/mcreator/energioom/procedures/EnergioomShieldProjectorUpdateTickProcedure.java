package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.LivingEnergioomBlockBlock;
import net.mcreator.energioom.blockUtils;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

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
		BlockPos[] blocks = blockUtils.GetConnectedBlocks(pos, LivingEnergioomBlockBlock.block,world);

		boolean enabled = forgeUtils.getNBT(pos, world).getBoolean("protection_enabled");
		boolean explosion_enabled = forgeUtils.getNBT(pos, world).getBoolean("explosionProtection_enabled");

		boolean isExplosionProof = false;
		double energy_usage = 0;

		for(BlockPos _pos: blocks){
			forgeUtils.setNBTProp(_pos,"projector_loc",pos,world);
			if (explosion_enabled && enabled) {
				isExplosionProof = energyUtils.useEnergy(pos, 4, false, world);
				energy_usage += 4;
			}else if(enabled){
				energyUtils.useEnergy(pos, 1, false, world);
				energy_usage += 1;
			}
		}

		forgeUtils.setNBTProp(pos,"connectedBlocks",blocks,world);
		forgeUtils.setNBTProp(pos,"energy_usage",energy_usage,world);
		forgeUtils.setNBTProp(pos,"isExplosionProof",isExplosionProof,world);
	}
}
