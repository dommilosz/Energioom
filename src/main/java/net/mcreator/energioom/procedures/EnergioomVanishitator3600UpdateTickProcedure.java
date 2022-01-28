package net.mcreator.energioom.procedures;

import net.mcreator.energioom.block.EnergioomVanishitator3600Block;
import net.mcreator.energioom.energyUtils;
import net.mcreator.energioom.forgeUtils;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.energioom.EnergioomModElements;

import java.util.Map;

@EnergioomModElements.ModElement.Tag
public class EnergioomVanishitator3600UpdateTickProcedure extends EnergioomModElements.ModElement {
    public EnergioomVanishitator3600UpdateTickProcedure(EnergioomModElements instance) {
        super(instance, 18);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {
        if (dependencies.get("x") == null) {
            if (!dependencies.containsKey("x"))
                System.err.println("Failed to load dependency x for procedure EnergioomVanishitator3600UpdateTick!");
            return;
        }
        if (dependencies.get("y") == null) {
            if (!dependencies.containsKey("y"))
                System.err.println("Failed to load dependency y for procedure EnergioomVanishitator3600UpdateTick!");
            return;
        }
        if (dependencies.get("z") == null) {
            if (!dependencies.containsKey("z"))
                System.err.println("Failed to load dependency z for procedure EnergioomVanishitator3600UpdateTick!");
            return;
        }
        if (dependencies.get("world") == null) {
            if (!dependencies.containsKey("world"))
                System.err.println("Failed to load dependency world for procedure EnergioomVanishitator3600UpdateTick!");
            return;
        }
        double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
        double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
        double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
        World world = (World) dependencies.get("world");
        BlockPos pos = new BlockPos(x, y, z);

        forgeUtils.setProp(pos, EnergioomVanishitator3600Block.REDSTONE, forgeUtils.isBlockPowered(pos, world), world);
        if (forgeUtils.isBlockPowered(pos, world)) {
            energyUtils.removeEnergy(pos, 1000, world);
        }

    }
}
