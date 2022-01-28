package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.mcreator.energioom.UpgradeHandler;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;

import net.mcreator.energioom.item.EnergioomCrystalItem;
import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class ItemEnergioomoniser3000UpdateTickProcedure extends EnergioomModElements.ModElement {
	public ItemEnergioomoniser3000UpdateTickProcedure(EnergioomModElements instance) {
		super(instance, 2);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				System.err.println("Failed to load dependency x for procedure ItemEnergioomoniser3000UpdateTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				System.err.println("Failed to load dependency y for procedure ItemEnergioomoniser3000UpdateTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				System.err.println("Failed to load dependency z for procedure ItemEnergioomoniser3000UpdateTick!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure ItemEnergioomoniser3000UpdateTick!");
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
		double progress= (ForgeUtils.getNBT(pos, world).getDouble("progress"));
		if(progress>=1){
			double progressChange = UpgradeHandler.getSpeed(speedCount);
			if(progress+progressChange>101)progressChange=101-progress;
			progress+= progressChange* UpgradeHandler.getEff(effCount);
			EnergyUtils.generateEnergyCacheDecimal(pos,progressChange*20,false,world);
		}
		if(progress>=100.95){
			progress=0;
		}
		if(progress<=0){
			if(ForgeUtils.itemUtils.consumeItem(pos, EnergioomCrystalItem.block,0,world)){
				progress=1;
			}

		}
		ForgeUtils.setNBTProp(pos, "progress", progress,world);
		EnergyUtils.sendEnergy(pos,new BlockPos(x,y-1,z),1000,world);

	}
}
