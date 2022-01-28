package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.EnergioomBatteryBlock;
import net.mcreator.energioom.block.RedstoneDiodeINBlock;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.energioom.EnergioomModElements;

import java.util.concurrent.atomic.AtomicInteger;
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

		int energy = (int) energyUtils.getEnergy(pos,world);
		int maxEnergy = (int) energyUtils.getMaxEnergy(pos,world);

		try {
			forgeUtils.setProp(pos, EnergioomBatteryBlock.POWER_LVL, (int) Math.floor((float) energy / maxEnergy * 8), world);
		}catch (Exception ignored){}

		energyUtils.distributeEnergy(new BlockPos(x,y,z),1000,world);
	}
}
