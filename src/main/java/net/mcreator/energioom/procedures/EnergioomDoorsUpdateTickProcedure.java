package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.EnergioomDoorsBlock;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

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

		forgeUtils.sideBlock[] sides = forgeUtils.getSidesOf(pos,EnergioomDoorsBlock.block,world);
		boolean hasPower = false;
		for(forgeUtils.sideBlock side:sides){
			if(energyUtils.getEnergy(side.pos,world)>=4500){
				hasPower = true;
			}
		}
		if(energyUtils.getEnergy(pos,world)>=4500){
			energyUtils.removeEnergy(pos,2,world);
			hasPower = true;
		}

		forgeUtils.setProp(pos,OPEN,hasPower,world);

	}
}
