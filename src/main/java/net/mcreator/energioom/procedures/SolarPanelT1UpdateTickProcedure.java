package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.RedstoneDiodeINBlock;
import net.mcreator.energioom.block.SolarPanelT1Block;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.mcreator.energioom.upgradeHandler;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class SolarPanelT1UpdateTickProcedure extends EnergioomModElements.ModElement {
	public SolarPanelT1UpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 14);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure SolarPanelT1UpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure SolarPanelT1UpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure SolarPanelT1UpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure SolarPanelT1UpdateTick!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");
		BlockPos pos = new BlockPos(x,y ,z );
		forgeUtils.setNBTProp(pos,"upgradeable",true,world);
		int speedCount = (forgeUtils.getNBT(pos, world).getInt("uprgrade_speed"));
		int effCount= (forgeUtils.getNBT(pos, world).getInt("uprgrade_eff"));

		boolean enabled = ((world.canBlockSeeSky(new BlockPos((int) x, (int) y, (int) z))) && (world.isDaytime()));
		double generation = 1* upgradeHandler.getSpeed(speedCount);
		if(enabled){
			energyUtils.generateEnergyCacheDecimal(pos,(generation),false,world);
		}
		forgeUtils.setProp(pos, SolarPanelT1Block.ENABLED,enabled,world);
		energyUtils.sendEnergy(pos,new BlockPos(x,y-1,z),200,world);
	}
}
