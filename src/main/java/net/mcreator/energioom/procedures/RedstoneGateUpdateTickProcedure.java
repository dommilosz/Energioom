package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.DiodeINBlock;
import net.mcreator.energioom.block.DiodeOUTBlock;
import net.mcreator.energioom.block.RedstoneDiodeINBlock;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class RedstoneGateUpdateTickProcedure extends EnergioomModElements.ModElement {
	public RedstoneGateUpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 12);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure RedstoneGateUpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure RedstoneGateUpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure RedstoneGateUpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure RedstoneGateUpdateTick!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");

		BlockPos pos = new BlockPos(x,y,z);

		forgeUtils.setProp(pos,RedstoneDiodeINBlock.REDSTONE,forgeUtils.isBlockPowered(pos,world),world);

		if(forgeUtils.isBlockPowered(pos,world)){
			forgeUtils.sideBlock[] sides = forgeUtils.getSidesOf(new BlockPos(x,y,z), DiodeOUTBlock.block,world);
			for(forgeUtils.sideBlock side:sides){
				energyUtils.sendEnergyNBL(new BlockPos(x,y,z),side.pos,1000,world);
			}
		}
	}
}
