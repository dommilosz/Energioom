package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.block.DiodeOUTBlock;
import net.mcreator.energioom.block.EnergioomExtractorBlock;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class EnergioomExtractorUpdateTickProcedure extends EnergioomModElements.ModElement {
	public EnergioomExtractorUpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 19);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure EnergioomExtractorUpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure EnergioomExtractorUpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure EnergioomExtractorUpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure EnergioomExtractorUpdateTick!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");

		BlockPos pos = new BlockPos(x,y,z);

		ForgeUtils.setProp(pos, EnergioomExtractorBlock.REDSTONE, ForgeUtils.isBlockPowered(pos,world),world);

		if(ForgeUtils.isBlockPowered(pos,world)){
			ForgeUtils.sideBlock[] sides = ForgeUtils.getSidesOf(new BlockPos(x,y,z), DiodeOUTBlock.block,world);
			ForgeUtils.sideBlock[] sides2 = EnergyUtils.getEnergySides(new BlockPos(x,y,z),world);
			for(ForgeUtils.sideBlock side:sides){
				EnergyUtils.sendEnergyNBL(new BlockPos(x,y,z),side.pos,1000,world);
			}

			for(ForgeUtils.sideBlock side2:sides2){
				EnergyUtils.sendEnergyInvert(side2.pos,new BlockPos(x,y,z),1000,world);
			}
		}
	}
}
