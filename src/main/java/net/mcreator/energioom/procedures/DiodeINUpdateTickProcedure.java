package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergioomModElements;
import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.block.DiodeOUTBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class DiodeINUpdateTickProcedure extends EnergioomModElements.ModElement {
	public DiodeINUpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 7);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure Diode!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure Diode!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure Diode!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure Diode!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");

		ForgeUtils.sideBlock[] sides = ForgeUtils.getSidesOf(new BlockPos(x,y,z), DiodeOUTBlock.block,world);
		for(ForgeUtils.sideBlock side:sides){
			EnergyUtils.sendEnergyNBL(new BlockPos(x,y,z),side.pos,1000,world);
		}

	}
}
