package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.EnergioomShieldProjectorBlock;
import net.mcreator.energioom.block.LivingEnergioomBlockBlock;
import net.mcreator.energioom.block.RedstoneDiodeINBlock;
import net.mcreator.energioom.blockUtils;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.energioom.EnergioomMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.mcreator.energioom.block.LivingEnergioomBlockBlock.explosionProof;

public class LivingEnergioomBlockUpdateTickProcedure {

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("world") == null) {
            if (!dependencies.containsKey("world"))
                EnergioomMod.LOGGER.warn("Failed to load dependency world for procedure LivingEnergioomBlockUpdateTick!");
            return;
        }
        if (dependencies.get("x") == null) {
            if (!dependencies.containsKey("x"))
                EnergioomMod.LOGGER.warn("Failed to load dependency x for procedure LivingEnergioomBlockUpdateTick!");
            return;
        }
        if (dependencies.get("y") == null) {
            if (!dependencies.containsKey("y"))
                EnergioomMod.LOGGER.warn("Failed to load dependency y for procedure LivingEnergioomBlockUpdateTick!");
            return;
        }
        if (dependencies.get("z") == null) {
            if (!dependencies.containsKey("z"))
                EnergioomMod.LOGGER.warn("Failed to load dependency z for procedure LivingEnergioomBlockUpdateTick!");
            return;
        }
        World world = (World) dependencies.get("world");
        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");

        BlockPos pos = new BlockPos(x, y, z);
        BlockPos projLoc = forgeUtils.getNBTBlockPos(pos,"projector_loc",world);
        if(projLoc!=null){
            if(world.getBlockState(projLoc).getBlock()==EnergioomShieldProjectorBlock.block){
                List<BlockPos> array = new ArrayList<>(Arrays.asList(forgeUtils.getNBTBlockPosArray(projLoc, "connectedBlocks", world)));
                if(array!=null && array.contains(pos)){
                    boolean isEnabled = forgeUtils.getNBT(projLoc,world).getBoolean("protection_enabled");
                    boolean hasPower = energyUtils.useEnergy(projLoc,4,true,world);
                    forgeUtils.setProp(pos, LivingEnergioomBlockBlock.ENABLED,isEnabled && hasPower,world);
                    forgeUtils.setProp(pos,explosionProof,forgeUtils.getNBT(projLoc,world).getBoolean("isExplosionProof"),world);
                }else{
                    forgeUtils.setProp(pos, LivingEnergioomBlockBlock.ENABLED,false,world);
                    forgeUtils.setProp(pos,explosionProof,false,world);
                }
            }else{
                forgeUtils.setProp(pos, LivingEnergioomBlockBlock.ENABLED,false,world);
                forgeUtils.setProp(pos,explosionProof,false,world);
            }
        }else{
            forgeUtils.setProp(pos, LivingEnergioomBlockBlock.ENABLED,false,world);
            forgeUtils.setProp(pos,explosionProof,false,world);
        }
    }
}
