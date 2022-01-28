package net.mcreator.energioom.procedures;

import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.block.EnergioomFurnaceBlock;
import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.UpgradeHandler;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class EnergioomFurnaceUpdateTickProcedure extends EnergioomModElements.ModElement {
	public EnergioomFurnaceUpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 37);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure EnergioomFurnaceUpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure EnergioomFurnaceUpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure EnergioomFurnaceUpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure EnergioomFurnaceUpdateTick!");
			return;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		World world = (World) dependencies.get("world");
		BlockPos pos = new BlockPos(x,y ,z );
		ForgeUtils.setNBTProp(pos,"upgradeable",true,world);
		int speedCount = (ForgeUtils.getNBT(pos, world).getInt("uprgrade_speed"));
		int effCount= (ForgeUtils.getNBT(pos, world).getInt("uprgrade_eff"));

		double energy = EnergyUtils.getEnergy(pos,world);
		double maxEnergy = EnergyUtils.getMaxEnergy(pos,world);
		double maxProgress = 2* UpgradeHandler.getSpeed(speedCount);
		double progress = maxProgress;
		double prevProgress = (ForgeUtils.getNBT(pos,world).getDouble("progress"));
		double percentPower = (energy/maxEnergy)*2;
		if(energy<(maxEnergy/2)){
			progress = (int) Math.ceil(maxProgress*percentPower);
		}
		if(energy<20)progress=-1;

		boolean success = ForgeUtils.itemUtils.smeltItem(pos,0,1,true,world);
		if(progress+prevProgress>15)progress=15-prevProgress;
		if(success&&progress>0){
			EnergyUtils.removeEnergy(pos,(int)Math.floor(progress*20* UpgradeHandler.getEff(effCount)),world);
		}else{
			progress = -1;
		}
		double actualProgress = prevProgress+progress;
		if(actualProgress<0)actualProgress=0;
		if(prevProgress==15){
			ForgeUtils.itemUtils.smeltItem(pos,0,1,false,world);
			actualProgress = 0;
			ForgeUtils.setNBTProp(pos, "progress", actualProgress,world);
		}
		ForgeUtils.setNBTProp(pos, "progress", actualProgress,world);
		ForgeUtils.setProp(pos, EnergioomFurnaceBlock.ACTIVE, success&&energy>=20,world);
	}
}
