package net.mcreator.energioom.procedures;

import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.world.GameType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.Minecraft;

import net.mcreator.energioom.EnergioomModElements;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;
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

        if(energyUtils.hasEnergyStorage(new BlockPos(x,y,z),world)){
            if (forgeUtils.getGameMode(entity)!=1){
                forgeUtils.damageItemStack(itemstack,1);
            }
            double energy = energyUtils.getEnergy(new BlockPos(x,y,z),world);
            double maxEnergy = energyUtils.getMaxEnergy(new BlockPos(x,y,z),world);
            forgeUtils.sendMessage(entity,"Energioom: "+energy+"/"+maxEnergy + " - ("+Math.floor(energy/maxEnergy*100)+"%)",false);
        }



    }

}
