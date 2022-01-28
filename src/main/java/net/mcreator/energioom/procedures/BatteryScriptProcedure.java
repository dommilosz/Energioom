package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.block.EnergioomBatteryBlock;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class BatteryScriptProcedure extends EnergioomModElements.ModElement {
	public BatteryScriptProcedure(EnergioomModElements instance) {
		super(instance, 5);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure BatteryScript!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure BatteryScript!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure BatteryScript!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure BatteryScript!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");
		BlockPos pos = new BlockPos(x,y ,z );

		int energy = (int) EnergyUtils.getEnergy(pos,world);
		int maxEnergy = (int) EnergyUtils.getMaxEnergy(pos,world);

		try {
			ForgeUtils.setProp(pos, EnergioomBatteryBlock.POWER_LVL, (int) Math.floor((float) energy / maxEnergy * 8), world);
		}catch (Exception ignored){}

		EnergyUtils.distributeEnergy(new BlockPos(x,y,z),1000,world);
	}
}
