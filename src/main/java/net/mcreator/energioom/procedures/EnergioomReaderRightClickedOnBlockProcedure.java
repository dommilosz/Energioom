package net.mcreator.energioom.procedures;

import net.mcreator.energioom.EnergyUtils;
import net.mcreator.energioom.ForgeUtils;
import net.minecraft.world.World;

import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class EnergioomReaderRightClickedOnBlockProcedure extends EnergioomModElements.ModElement {
    public EnergioomReaderRightClickedOnBlockProcedure(EnergioomModElements instance) {
        super(instance, 4);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("entity") == null) {
            if (!dependencies.containsKey("entity"))
                System.err.println("Failed to load dependency entity for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        if (dependencies.get("itemstack") == null) {
            if (!dependencies.containsKey("itemstack"))
                System.err.println("Failed to load dependency itemstack for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        if (dependencies.get("x") == null) {
            if (!dependencies.containsKey("x"))
                System.err.println("Failed to load dependency x for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        if (dependencies.get("y") == null) {
            if (!dependencies.containsKey("y"))
                System.err.println("Failed to load dependency y for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        if (dependencies.get("z") == null) {
            if (!dependencies.containsKey("z"))
                System.err.println("Failed to load dependency z for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        if (dependencies.get("world") == null) {
            if (!dependencies.containsKey("world"))
                System.err.println("Failed to load dependency world for procedure EnergioomReaderRightClickedOnBlock!");
            return;
        }
        Entity entity = (Entity) dependencies.get("entity");
        ItemStack itemstack = (ItemStack) dependencies.get("itemstack");
        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
        World world = (World) dependencies.get("world");

        if(EnergyUtils.hasEnergyStorage(new BlockPos(x,y,z),world)){
            if (ForgeUtils.getGameMode(entity)!=1){
                ForgeUtils.damageItemStack(itemstack,1);
            }
            double energy = EnergyUtils.getEnergy(new BlockPos(x,y,z),world);
            double maxEnergy = EnergyUtils.getMaxEnergy(new BlockPos(x,y,z),world);
            ForgeUtils.sendMessage(entity,"Energioom: "+energy+"/"+maxEnergy + " - ("+Math.floor(energy/maxEnergy*100)+"%)",false);
        }



    }

}
