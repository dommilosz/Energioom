package net.mcreator.energioom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import net.mcreator.energioom.block.DiodeOUTBlock;
import net.mcreator.energioom.block.ItemEnergioomoniser3000Block;
import net.mcreator.energioom.block.SolarPanelT1Block;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtils {
    public static Block[] getBlacklistedBlocks() {
        return new Block[] { DiodeOUTBlock.block, ItemEnergioomoniser3000Block.block, SolarPanelT1Block.block };
    }

    public static boolean checkBlacklistedBlock(Block block) {
        for (Block b : getBlacklistedBlocks()) {
            if (b == block)
                return true;
        }
        return false;
    }

    public static double getEnergy(BlockPos pos, World world) {
        IEnergyStorage energyCap = getEnergyCapability(pos, world);
        if (energyCap == null)
            return 0.0D;
        return energyCap.getEnergyStored();
    }

    public static double getMaxEnergy(BlockPos pos, World world) {
        IEnergyStorage energyCap = getEnergyCapability(pos, world);
        if (energyCap == null)
            return 0.0D;
        return energyCap.getMaxEnergyStored();
    }

    public static boolean hasEnergyStorage(BlockPos pos, World world) {
        return (getMaxEnergy(pos, world) > 0.0D);
    }

    public static void sendEnergyNBL(BlockPos from, BlockPos to, int amount, World world) {
        IEnergyStorage energyCapA = getEnergyCapability(from, world);
        IEnergyStorage energyCapB = getEnergyCapability(to, world);
        if (energyCapA == null)
            return;
        if (energyCapB == null)
            return;
        int energySIMExtracted = energyCapA.extractEnergy(amount, true);
        int energySIMSended = energyCapB.receiveEnergy(amount, true);
        int energyToSend = Math.min(energySIMExtracted, energySIMSended);
        energyCapA.extractEnergy(energyToSend, false);
        energyCapB.receiveEnergy(energyToSend, false);
    }

    public static void sendEnergy(BlockPos from, BlockPos to, int amount, World world) {
        if (checkBlacklistedBlock(world.getBlockState(to).getBlock()))
            return;
        sendEnergyNBL(from, to, amount, world);
    }

    public static void sendEnergyInvert(BlockPos from, BlockPos to, int amount, World world) {
        if (checkBlacklistedBlock(world.getBlockState(from).getBlock()))
            return;
        sendEnergyNBL(from, to, amount, world);
    }

    public static boolean sendEnergyIfLower(BlockPos from, BlockPos to, int amount, World world) {
        IEnergyStorage energyCapB = getEnergyCapability(to, world);
        IEnergyStorage energyCapA = getEnergyCapability(from, world);
        if (energyCapA == null)
            return false;
        if (getEnergy(from, world) > energyCapB.getEnergyStored()) {
            sendEnergy(from, to, amount, world);
            return true;
        }
        return false;
    }

    public static boolean useEnergy(BlockPos pos, int amount, boolean simulate, World world) {
        if (getEnergy(pos, world) >= amount) {
            if (!simulate)
                removeEnergy(pos, amount, world);
            return true;
        }
        return false;
    }

    public static boolean generateEnergy(BlockPos pos, int amount, boolean simulate, World world) {
        if (getEnergy(pos, world) + amount <= getMaxEnergy(pos, world)) {
            if (!simulate)
                addEnergy(pos, amount, world);
            return true;
        }
        return false;
    }

    public static boolean generateEnergyCacheDecimal(BlockPos pos, double amount, boolean simulate, World world) {
        if (getEnergy(pos, world) + amount <= getMaxEnergy(pos, world)) {
            if (!simulate) {
                double generationAdd = ForgeUtils.getNBT(pos, world).getDouble("generation_add");
                double add = amount - Math.floor(amount);
                if (generationAdd > 1.0D) {
                    amount += Math.floor(generationAdd);
                    generationAdd -= Math.floor(generationAdd);
                }
                ForgeUtils.setNBTProp(pos, "generation_add", generationAdd + add, world);
                addEnergy(pos, (int)Math.floor(amount), world);
            }
            return true;
        }
        return false;
    }

    public static void addEnergy(BlockPos pos, int amount, World world) {
        getEnergyCapability(pos, world).receiveEnergy(amount, false);
    }

    public static void removeEnergy(BlockPos pos, int amount, World world) {
        getEnergyCapability(pos, world).extractEnergy(amount, false);
    }

    public static IEnergyStorage getEnergyCapability(BlockPos pos, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);
        AtomicReference<IEnergyStorage> capability = new AtomicReference<>(null);
        if (tileEntity != null)
            tileEntity.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability::set);
        return capability.get();
    }

    public static void distributeEnergy(BlockPos pos, int amount, World world) {
        ForgeUtils.sideBlock[] sides = getEnergySides(pos, world);
        if (sides.length < 1)
            return;
        int powerPerSide = (int)Math.min(Math.floorDiv(amount, sides.length), getEnergy(pos, world));
        int powerLeft = amount;
        while (powerLeft > 0) {
            for (ForgeUtils.sideBlock side : sides) {
                sendEnergyIfLower(pos, side.pos, 1, world);
                powerLeft--;
                if (powerLeft <= 0)
                    break;
            }
        }
    }

    public static ForgeUtils.sideBlock[] getEnergySides(BlockPos pos, World world) {
        ArrayList<ForgeUtils.sideBlock> sides = new ArrayList<>();
        IEnergyStorage energyCapNX = getEnergyCapability(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), world);
        IEnergyStorage energyCapPX = getEnergyCapability(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), world);
        IEnergyStorage energyCapNY = getEnergyCapability(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), world);
        IEnergyStorage energyCapPY = getEnergyCapability(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), world);
        IEnergyStorage energyCapNZ = getEnergyCapability(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), world);
        IEnergyStorage energyCapPZ = getEnergyCapability(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), world);
        ForgeUtils.sideBlock blockNX = new ForgeUtils.sideBlock(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), world);
        ForgeUtils.sideBlock blockPX = new ForgeUtils.sideBlock(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), world);
        ForgeUtils.sideBlock blockNY = new ForgeUtils.sideBlock(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), world);
        ForgeUtils.sideBlock blockPY = new ForgeUtils.sideBlock(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), world);
        ForgeUtils.sideBlock blockNZ = new ForgeUtils.sideBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), world);
        ForgeUtils.sideBlock blockPZ = new ForgeUtils.sideBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), world);
        if (energyCapNX != null)
            sides.add(blockNX);
        if (energyCapPX != null)
            sides.add(blockPX);
        if (energyCapNY != null)
            sides.add(blockNY);
        if (energyCapPY != null)
            sides.add(blockPY);
        if (energyCapNZ != null)
            sides.add(blockNZ);
        if (energyCapPZ != null)
            sides.add(blockPZ);
        ForgeUtils.sideBlock[] sidesArr = new ForgeUtils.sideBlock[sides.size()];
        for (int i = 0; i < sides.size(); i++)
            sidesArr[i] = sides.get(i);
        return sidesArr;
    }

    public static void distributeEnergyBL(BlockPos pos, int amount, World world, Block[] bl) {
        ForgeUtils.sideBlock[] sides = getEnergySides(pos, world);
        if (sides.length < 1)
            return;
        int powerPerSide = (int)Math.min(Math.floorDiv(amount, sides.length), getEnergy(pos, world));
        for (ForgeUtils.sideBlock side : sides) {
            if (!Arrays.<Block>asList(bl).contains(side.block))
                sendEnergyIfLower(pos, side.pos, powerPerSide, world);
        }
    }

    public static void spreadEnergyBL(BlockPos pos, int amount, World world, Block[] bl) {
        ForgeUtils.sideBlock[] sides = getEnergySides(pos, world);
        if (sides.length < 1)
            return;
        int powerPerSide = (int)Math.min(Math.floorDiv(amount, sides.length), getEnergy(pos, world));
        for (ForgeUtils.sideBlock side : sides) {
            if (!Arrays.<Block>asList(bl).contains(side.block))
                sendEnergy(pos, side.pos, powerPerSide, world);
        }
    }

    public static ArrayList<BlockPos> getNetworkMap(BlockPos pos, World world) {
        ArrayList<BlockPos> networkMap = new ArrayList<>();
        recursiveGetNetworkNodes(pos, world, networkMap, 500);
        return networkMap;
    }

    public static double getEnergyInNetwork(BlockPos pos, World world) {
        try {
            ArrayList<BlockPos> networkMap = getNetworkMap(pos, world);
            double totalEnergy = 0.0D;
            for (BlockPos bpos : networkMap)
                totalEnergy += getEnergy(bpos, world);
            return totalEnergy;
        } catch (Exception ex) {
            return 0.0D;
        }
    }

    static void recursiveGetNetworkNodes(BlockPos pos, World world, ArrayList<BlockPos> networkMap, int limit) {
        for (ForgeUtils.sideBlock side : getEnergySides(pos, world)) {
            if (!networkMap.contains(side.pos)) {
                networkMap.add(side.pos);
                recursiveGetNetworkNodes(side.pos, world, networkMap, limit);
                limit--;
                if (limit <= 0)
                    return;
            }
        }
    }

    public static double getMaxEnergyInNetwork(BlockPos pos, World world) {
        try {
            ArrayList<BlockPos> networkMap = getNetworkMap(pos, world);
            double totalEnergy = 0.0D;
            for (BlockPos bpos : networkMap)
                totalEnergy += getMaxEnergy(bpos, world);
            return totalEnergy;
        } catch (Exception ex) {
            return 0.0D;
        }
    }

    public static void generateEnergyFromItem(BlockPos pos, Item item, int slot, int amount, World world) {
        if (ForgeUtils.itemUtils.equals(pos, slot, item, world) &&
                ForgeUtils.itemUtils.getItemCount(pos, slot, world) > 0 &&
                generateEnergy(pos, 100, false, world))
            ForgeUtils.itemUtils.changeStackAmountRelative(pos, slot, -1, world);
    }

    public static void generateEnergyFromItemCacheDecimal(BlockPos pos, Item item, int slot, double amount, World world) {
        if (ForgeUtils.itemUtils.equals(pos, slot, item, world) &&
                ForgeUtils.itemUtils.getItemCount(pos, slot, world) > 0 &&
                generateEnergyCacheDecimal(pos, 100.0D, false, world))
            ForgeUtils.itemUtils.changeStackAmountRelative(pos, slot, -1, world);
    }
}
