package net.mcreator.energioom.procedures;

import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.mcreator.energioom.upgradeHandler;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;

import net.mcreator.energioom.item.EnergioomCrystalItem;
import net.mcreator.energioom.EnergioomModElements;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;
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

		forgeUtils.setNBTProp(pos,"upgradeable",true,world);
		int speedCount = (forgeUtils.getNBT(pos, world).getInt("uprgrade_speed"));
		int effCount= (forgeUtils.getNBT(pos, world).getInt("uprgrade_eff"));
		double progress= (forgeUtils.getNBT(pos, world).getDouble("progress"));
		if(progress>=1){
			double progressChange = upgradeHandler.getSpeed(speedCount);
			if(progress+progressChange>101)progressChange=101-progress;
			progress+= progressChange*upgradeHandler.getEff(effCount);
			energyUtils.generateEnergyCacheDecimal(pos,progressChange*20,false,world);
		}
		if(progress>=100.95){
			progress=0;
		}
		if(progress<=0){
			if(forgeUtils.itemUtils.consumeItem(pos, EnergioomCrystalItem.block,0,world)){
				progress=1;
			}

		}
		forgeUtils.setNBTProp(pos, "progress", progress,world);
		energyUtils.sendEnergy(pos,new BlockPos(x,y-1,z),1000,world);

	}
}
