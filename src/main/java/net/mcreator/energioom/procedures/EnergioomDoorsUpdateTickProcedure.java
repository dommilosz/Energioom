package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.block.EnergioomDoorsBlock;
import net.mcreator.energioom.ForgeUtils;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

import static net.minecraft.block.DoorBlock.OPEN;

@EnergioomModElements.ModElement.Tag
public class EnergioomDoorsUpdateTickProcedure extends EnergioomModElements.ModElement {
	public EnergioomDoorsUpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 26);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure EnergioomDoorsUpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure EnergioomDoorsUpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure EnergioomDoorsUpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure EnergioomDoorsUpdateTick!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");

		BlockPos pos = new BlockPos(x,y ,z );

		ForgeUtils.sideBlock[] sides = ForgeUtils.getSidesOf(pos,EnergioomDoorsBlock.block,world);
		boolean hasPower = false;
		for(ForgeUtils.sideBlock side:sides){
			if(EnergyUtils.getEnergy(side.pos,world)>=4500){
				hasPower = true;
			}
		}
		if(EnergyUtils.getEnergy(pos,world)>=4500){
			EnergyUtils.removeEnergy(pos,2,world);
			hasPower = true;
		}

		ForgeUtils.setProp(pos,OPEN,hasPower,world);

	}
}
