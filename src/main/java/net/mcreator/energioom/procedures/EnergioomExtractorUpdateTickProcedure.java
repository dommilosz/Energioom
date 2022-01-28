package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.DiodeOUTBlock;
import net.mcreator.energioom.block.EnergioomExtractorBlock;
import net.mcreator.energioom.block.RedstoneDiodeINBlock;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

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

		forgeUtils.setProp(pos, EnergioomExtractorBlock.REDSTONE,forgeUtils.isBlockPowered(pos,world),world);

		if(forgeUtils.isBlockPowered(pos,world)){
			forgeUtils.sideBlock[] sides = forgeUtils.getSidesOf(new BlockPos(x,y,z), DiodeOUTBlock.block,world);
			forgeUtils.sideBlock[] sides2 = energyUtils.getEnergySides(new BlockPos(x,y,z),world);
			for(forgeUtils.sideBlock side:sides){
				energyUtils.sendEnergyNBL(new BlockPos(x,y,z),side.pos,1000,world);
			}

			for(forgeUtils.sideBlock side2:sides2){
				energyUtils.sendEnergyInvert(side2.pos,new BlockPos(x,y,z),1000,world);
			}
		}
	}
}
