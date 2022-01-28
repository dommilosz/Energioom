package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.EnergioomShieldProjectorBlock;
import net.mcreator.energioom.block.LivingEnergioomBlockBlock;
import net.mcreator.energioom.blockUtils;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.world.BlockEvent;

import net.minecraft.world.IWorld;
import net.minecraft.entity.Entity;

import java.util.*;

import static net.mcreator.energioom.block.LivingEnergioomBlockBlock.explosionProof;

public class LivingEnergioomBlockBlockDestroyedByPlayerProcedure {
    @Mod.EventBusSubscriber
    private static class GlobalTrigger {
        public static BlockPos checkProjector(BlockState state, BlockPos pos, World world) {
            if (!state.getBlock().equals(LivingEnergioomBlockBlock.block)) return null;
            BlockPos projLoc = forgeUtils.getNBTBlockPos(pos, "projector_loc", world);
            if (projLoc != null) {
                if (world.getBlockState(projLoc).getBlock() == EnergioomShieldProjectorBlock.block) {
                    List<BlockPos> array = new ArrayList<>(Arrays.asList(forgeUtils.getNBTBlockPosArray(projLoc, "connectedBlocks", world)));
                    if (array != null && array.contains(pos)) {
                        boolean isEnabled = forgeUtils.getNBT(projLoc,world).getBoolean("protection_enabled");
                        boolean hasPower = energyUtils.useEnergy(projLoc,4,true,world);
                        if(isEnabled&&hasPower){
                            return projLoc;
                        }
                    }
                }
            }
            return null;
        }

        public static BlockPos checkProjectorExp(BlockState state, BlockPos _pos, World world) {
            BlockPos projLoc = checkProjector(state,_pos,world);
            if(projLoc==null) return null;
            if(forgeUtils.getNBT(projLoc,world).getBoolean("isExplosionProof")){
                return projLoc;
            }
            return null;
        }

        public static BlockPos checkProjector(BlockPos _pos, World world) {
            return checkProjector(world.getBlockState(_pos), _pos, world);
        }

        public static BlockPos checkProjectorExp(BlockPos _pos, World world) {
            return checkProjectorExp(world.getBlockState(_pos), _pos, world);
        }

        public static boolean checkOwner(BlockPos pos, Entity entity, World world){
            if(forgeUtils.getNBT(pos,world).getBoolean("selfPrevent_enabled")){
                return true;
            }else return !entity.getUniqueID().toString().equals(forgeUtils.getNBT(pos, world).getString("owner-id"));
        }

        @SubscribeEvent
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            PlayerEntity entity = event.getPlayer();
            World world = (World) event.getWorld();
            BlockState state = event.getState();
            BlockPos pos = event.getPos();

            if (forgeUtils.getGameMode(entity) == 1) return;
            BlockPos _pos = checkProjector(state, pos, world);
            if (_pos != null && energyUtils.useEnergy(_pos, 10, false, world)) {
                if(!checkOwner(_pos,entity,world))return;
                event.setCanceled(true);
                forgeUtils.AddAchievement(entity,new ResourceLocation("energioom:how_am_i_supposed_to_open_it"));
            }
        }

        @SubscribeEvent
        public static void onLivingDestroyBlock(BlockEvent.BreakEvent event) {
            World world = (World) event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = event.getState();
            PlayerEntity entity = event.getPlayer();

            if (forgeUtils.getGameMode(entity) == 1) return;

            BlockPos _pos = checkProjector(state, pos, world);
            if (_pos != null && energyUtils.useEnergy(_pos, 10, false, world)) {
                if(!checkOwner(_pos,entity,world))return;
                event.setCanceled(true);
                forgeUtils.AddAchievement(entity,new ResourceLocation("energioom:how_am_i_supposed_to_open_it"));
            }
        }

        @SubscribeEvent
        public static void onDetonate(ExplosionEvent.Detonate event) {
            List<BlockPos> affectedBlocks = event.getAffectedBlocks();
            List<BlockPos> toRemove = new LinkedList<>();
            for (BlockPos pos : affectedBlocks) {
                BlockPos _pos = checkProjectorExp(pos, event.getWorld());
                if (_pos != null && energyUtils.useEnergy(_pos, 50, false, event.getWorld())) {
                    toRemove.add(pos);
                    //event.setCanceled(true);
                }
            }
            affectedBlocks.removeAll(toRemove);
            //affectedBlocks.clear();
        }
    }
}
