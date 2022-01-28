package net.mcreator.energioom.procedures;

import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import net.mcreator.energioom.EnergioomMod;

import java.util.Map;

public class EnergioomShieldProjectorOnBlockRightClickedProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				EnergioomMod.LOGGER.warn("Failed to load dependency world for procedure EnergioomShieldProjectorOnBlockRightClicked!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				EnergioomMod.LOGGER.warn("Failed to load dependency x for procedure EnergioomShieldProjectorOnBlockRightClicked!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				EnergioomMod.LOGGER.warn("Failed to load dependency y for procedure EnergioomShieldProjectorOnBlockRightClicked!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				EnergioomMod.LOGGER.warn("Failed to load dependency z for procedure EnergioomShieldProjectorOnBlockRightClicked!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				EnergioomMod.LOGGER.warn("Failed to load dependency entity for procedure EnergioomShieldProjectorOnBlockRightClicked!");
			return;
		}
		World world = (World) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");

		BlockPos pos = new BlockPos(x,y,z);
		if(forgeUtils.getNBT(pos,world).getString("owner-id").length()<1){
			forgeUtils.setNBTProp(pos,"owner-id",entity.getUniqueID().toString(),world);
			forgeUtils.setNBTProp(pos,"owner-nick",entity.getName().getString(),world);
		}
	}
}
